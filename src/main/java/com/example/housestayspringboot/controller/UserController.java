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

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<User> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error(401, "未登录");
        }

        String token = authHeader.substring(7);
        if (!jwtUtils.validateToken(token)) {
            return Result.error(401, "Token无效或已过期");
        }

        Long userId = jwtUtils.getUserId(token);
        User user = userService.findById(userId);

        if (user == null) {
            return Result.error(404, "用户不存在");
        }

        // 不返回密码
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/info")
    public Result<User> updateUserInfo(@RequestHeader("Authorization") String authHeader,
                                       @RequestBody Map<String, String> updateData) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error(401, "未登录");
        }

        String token = authHeader.substring(7);
        if (!jwtUtils.validateToken(token)) {
            return Result.error(401, "Token无效或已过期");
        }

        Long userId = jwtUtils.getUserId(token);
        User user = userService.findById(userId);

        if (user == null) {
            return Result.error(404, "用户不存在");
        }

        // 更新昵称
        if (updateData.containsKey("nickname") && updateData.get("nickname") != null) {
            user.setNickname(updateData.get("nickname"));
        }

        // 更新头像
        if (updateData.containsKey("avatar") && updateData.get("avatar") != null) {
            user.setAvatar(updateData.get("avatar"));
        }

        // 更新邮箱
        if (updateData.containsKey("email") && updateData.get("email") != null) {
            user.setEmail(updateData.get("email"));
        }

        // 更新真实姓名（实名认证）
        if (updateData.containsKey("realName") && updateData.get("realName") != null) {
            user.setRealName(updateData.get("realName"));
        }

        // 更新身份证号（实名认证）
        if (updateData.containsKey("idCard") && updateData.get("idCard") != null) {
            user.setIdCard(updateData.get("idCard"));
        }

        // 更新手机号
        if (updateData.containsKey("phone") && updateData.get("phone") != null) {
            // 检查手机号是否已被其他用户使用
            User existUser = userService.findByPhone(updateData.get("phone"));
            if (existUser != null && !existUser.getUserId().equals(userId)) {
                return Result.error(400, "该手机号已被其他用户绑定");
            }
            user.setPhone(updateData.get("phone"));
        }

        user = userService.update(user);

        // 不返回密码
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<Void> changePassword(@RequestHeader("Authorization") String authHeader,
                                        @RequestBody Map<String, String> passwordData) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error(401, "未登录");
        }

        String token = authHeader.substring(7);
        if (!jwtUtils.validateToken(token)) {
            return Result.error(401, "Token无效或已过期");
        }

        Long userId = jwtUtils.getUserId(token);
        User user = userService.findById(userId);

        if (user == null) {
            return Result.error(404, "用户不存在");
        }

        String oldPassword = passwordData.get("oldPassword");
        String newPassword = passwordData.get("newPassword");

        if (oldPassword == null || oldPassword.isEmpty()) {
            return Result.error(400, "请输入原密码");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            return Result.error(400, "请输入新密码");
        }
        if (newPassword.length() < 6) {
            return Result.error(400, "新密码长度不能少于6位");
        }

        // 验证原密码
        if (!userService.verifyPassword(oldPassword, user.getPassword())) {
            return Result.error(400, "原密码错误");
        }

        userService.updatePassword(userId, newPassword);
        return Result.success("密码修改成功", null);
    }

    /**
     * 发送换绑手机号验证码
     */
    @PostMapping("/phone/code")
    public Result<Map<String, String>> sendPhoneChangeCode(@RequestHeader("Authorization") String authHeader,
                                                            @RequestBody Map<String, String> request) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error(401, "未登录");
        }

        String token = authHeader.substring(7);
        if (!jwtUtils.validateToken(token)) {
            return Result.error(401, "Token无效或已过期");
        }

        String phone = request.get("phone");
        if (phone == null || phone.isEmpty()) {
            return Result.error(400, "手机号不能为空");
        }

        // 检查手机号是否已被使用
        User existUser = userService.findByPhone(phone);
        if (existUser != null) {
            return Result.error(400, "该手机号已被其他用户绑定");
        }

        // 模拟发送验证码
        Map<String, String> data = new HashMap<>();
        data.put("code", "123456");
        return Result.success("验证码已发送", data);
    }
}
