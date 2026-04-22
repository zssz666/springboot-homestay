package com.example.housestayspringboot.controller;

import com.example.housestayspringboot.common.Result;
import com.example.housestayspringboot.entity.User;
import com.example.housestayspringboot.service.UserService;
import com.example.housestayspringboot.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/info")
    public Result<User> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.<User>error(401, "未登录");
        }

        String token = authHeader.substring(7);
        if (!jwtUtils.validateToken(token)) {
            return Result.<User>error(401, "Token无效或已过期");
        }

        Integer userId = jwtUtils.getUserId(token);
        User user = userService.findById(userId);

        if (user == null) {
            return Result.<User>error(404, "用户不存在");
        }

        user.setPassword(null);
        return Result.success(user);
    }

    @PutMapping("/info")
    public Result<User> updateUserInfo(@RequestHeader("Authorization") String authHeader,
                                       @RequestBody Map<String, String> updateData) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.<User>error(401, "未登录");
        }

        String token = authHeader.substring(7);
        if (!jwtUtils.validateToken(token)) {
            return Result.<User>error(401, "Token无效或已过期");
        }

        Integer userId = jwtUtils.getUserId(token);
        User user = userService.findById(userId);

        if (user == null) {
            return Result.<User>error(404, "用户不存在");
        }

        if (updateData.containsKey("name") && updateData.get("name") != null) {
            user.setName(updateData.get("name"));
        }
        if (updateData.containsKey("avatar") && updateData.get("avatar") != null) {
            user.setAvatar(updateData.get("avatar"));
        }
        if (updateData.containsKey("userEmail") && updateData.get("userEmail") != null) {
            user.setUserEmail(updateData.get("userEmail"));
        }
        if (updateData.containsKey("realName") && updateData.get("realName") != null) {
            user.setRealName(updateData.get("realName"));
        }
        if (updateData.containsKey("idCard") && updateData.get("idCard") != null) {
            user.setIdCard(updateData.get("idCard"));
        }
        if (updateData.containsKey("phone") && updateData.get("phone") != null) {
            User existUser = userService.findByPhone(updateData.get("phone"));
            if (existUser != null && !existUser.getUserId().equals(userId)) {
                return Result.<User>error(400, "该手机号已被其他用户绑定");
            }
            user.setPhone(updateData.get("phone"));
        }
        if (updateData.containsKey("gender") && updateData.get("gender") != null) {
            try {
                user.setGender(Integer.parseInt(updateData.get("gender")));
            } catch (Exception ignored) {}
        }
        if (updateData.containsKey("birthday") && updateData.get("birthday") != null) {
            try {
                user.setBirthday(java.time.LocalDateTime.parse(updateData.get("birthday") + "T00:00:00"));
            } catch (Exception ignored) {}
        }

        user = userService.update(user);
        user.setPassword(null);
        return Result.success(user);
    }

    @PutMapping("/password")
    public Result<Void> changePassword(@RequestHeader("Authorization") String authHeader,
                                        @RequestBody Map<String, String> passwordData) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.<Void>error(401, "未登录");
        }

        String token = authHeader.substring(7);
        if (!jwtUtils.validateToken(token)) {
            return Result.<Void>error(401, "Token无效或已过期");
        }

        Integer userId = jwtUtils.getUserId(token);
        User user = userService.findById(userId);

        if (user == null) {
            return Result.<Void>error(404, "用户不存在");
        }

        String oldPassword = passwordData.get("oldPassword");
        String newPassword = passwordData.get("newPassword");

        if (oldPassword == null || oldPassword.isEmpty()) {
            return Result.<Void>error(400, "请输入原密码");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            return Result.<Void>error(400, "请输入新密码");
        }
        if (newPassword.length() < 6) {
            return Result.<Void>error(400, "新密码长度不能少于6位");
        }

        if (!userService.verifyPassword(oldPassword, user.getPassword())) {
            return Result.<Void>error(400, "原密码错误");
        }

        userService.updatePassword(userId, newPassword);
        return Result.<Void>success("密码修改成功", null);
    }

    @PostMapping("/phone/code")
    public Result<Map<String, String>> sendPhoneChangeCode(@RequestHeader("Authorization") String authHeader,
                                                            @RequestBody Map<String, String> request) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.<Map<String, String>>error(401, "未登录");
        }

        String token = authHeader.substring(7);
        if (!jwtUtils.validateToken(token)) {
            return Result.<Map<String, String>>error(401, "Token无效或已过期");
        }

        String phone = request.get("phone");
        if (phone == null || phone.isEmpty()) {
            return Result.<Map<String, String>>error(400, "手机号不能为空");
        }

        User existUser = userService.findByPhone(phone);
        if (existUser != null) {
            return Result.<Map<String, String>>error(400, "该手机号已被其他用户绑定");
        }

        Map<String, String> data = new HashMap<>();
        data.put("code", "123456");
        return Result.success("验证码已发送", data);
    }

    // 实名认证状态
    @GetMapping("/cert/status")
    public Result<Map<String, Object>> getCertStatus(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.<Map<String, Object>>error(401, "未登录");
        }
        String token = authHeader.substring(7);
        if (!jwtUtils.validateToken(token)) {
            return Result.<Map<String, Object>>error(401, "Token无效或已过期");
        }
        Integer userId = jwtUtils.getUserId(token);
        int status = userService.getCertStatus(userId);
        Map<String, Object> data = new HashMap<>();
        data.put("certStatus", status); // 0=未认证, 1=已认证
        return Result.success(data);
    }

    // 提交实名认证
    @PostMapping("/cert")
    public Result<User> submitRealNameCert(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody Map<String, String> certData) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.<User>error(401, "未登录");
        }
        String token = authHeader.substring(7);
        if (!jwtUtils.validateToken(token)) {
            return Result.<User>error(401, "Token无效或已过期");
        }
        Integer userId = jwtUtils.getUserId(token);

        String realName = certData.get("realName");
        String idCard = certData.get("idCard");
        if (realName == null || realName.isEmpty()) {
            return Result.<User>error(400, "真实姓名不能为空");
        }
        if (idCard == null || !idCard.matches("^\\d{17}[\\dXx]$")) {
            return Result.<User>error(400, "请输入正确的18位身份证号");
        }

        // 检查是否已认证
        int currentStatus = userService.getCertStatus(userId);
        if (currentStatus == 1) {
            return Result.<User>error(400, "您已完成实名认证");
        }

        User user = userService.submitRealNameCert(userId, realName, idCard);
        user.setPassword(null);
        return Result.success("实名认证提交成功", user);
    }

    // 学生认证状态（含年龄信息）
    @GetMapping("/student-cert/status")
    public Result<Map<String, Object>> getStudentCertStatus(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.<Map<String, Object>>error(401, "未登录");
        }
        String token = authHeader.substring(7);
        if (!jwtUtils.validateToken(token)) {
            return Result.<Map<String, Object>>error(401, "Token无效或已过期");
        }
        Integer userId = jwtUtils.getUserId(token);

        int certStatus = userService.getCertStatus(userId);
        int studentStatus = userService.getStudentCertStatus(userId);
        int age = userService.calculateAge(userId);

        Map<String, Object> data = new HashMap<>();
        data.put("certStatus", certStatus); // 实名认证状态
        data.put("studentStatus", studentStatus); // 学生认证状态 0=未认证,1=已认证
        data.put("age", age);
        data.put("eligible", age >= 0 && age <= 22 && certStatus == 1);

        return Result.success(data);
    }

    // 提交学生认证
    @PostMapping("/student-cert")
    public Result<User> submitStudentCert(@RequestHeader("Authorization") String authHeader,
                                          @RequestBody Map<String, String> certData) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.<User>error(401, "未登录");
        }
        String token = authHeader.substring(7);
        if (!jwtUtils.validateToken(token)) {
            return Result.<User>error(401, "Token无效或已过期");
        }
        Integer userId = jwtUtils.getUserId(token);

        // 前置条件：实名认证必须通过
        if (userService.getCertStatus(userId) != 1) {
            return Result.<User>error(400, "请先完成实名认证");
        }

        // 年龄校验
        int age = userService.calculateAge(userId);
        if (age < 0) {
            return Result.<User>error(400, "请先设置您的生日");
        }
        if (age > 22) {
            return Result.<User>error(400, "学生认证仅限22岁及以下用户，您当前年龄为" + age + "岁");
        }

        // 检查是否已认证
        if (userService.getStudentCertStatus(userId) == 1) {
            return Result.<User>error(400, "您已完成学生认证");
        }

        String studentCardUrl = certData.get("studentCardUrl");
        if (studentCardUrl == null || studentCardUrl.isEmpty()) {
            return Result.<User>error(400, "请上传学生证照片");
        }

        User user = userService.submitStudentCert(userId, studentCardUrl);
        user.setPassword(null);
        return Result.success("学生认证提交成功", user);
    }
}
