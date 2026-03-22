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
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
        String phone = loginData.get("phone");
        String password = loginData.get("password");

        if (phone == null || phone.isEmpty()) {
            return Result.error(400, "手机号不能为空");
        }
        if (password == null || password.isEmpty()) {
            return Result.error(400, "密码不能为空");
        }

        User user = userService.findByPhone(phone);
        if (user == null) {
            return Result.error(401, "用户不存在");
        }

        if (!userService.verifyPassword(password, user.getPassword())) {
            return Result.error(401, "密码错误");
        }

        if (user.getStatus() == 1) {
            return Result.error(401, "账号已被禁用");
        }

        String token = jwtUtils.generateToken(user.getUserId(), user.getPhone(), user.getRole());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);

        return Result.success(data);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result<Map<String, Object>> register(@RequestBody Map<String, String> registerData) {
        String phone = registerData.get("phone");
        String password = registerData.get("password");
        String nickname = registerData.get("nickname");

        if (phone == null || phone.isEmpty()) {
            return Result.error(400, "手机号不能为空");
        }
        if (password == null || password.isEmpty()) {
            return Result.error(400, "密码不能为空");
        }
        if (nickname == null || nickname.isEmpty()) {
            nickname = "用户" + phone.substring(7);
        }

        User existUser = userService.findByPhone(phone);
        if (existUser != null) {
            return Result.error(400, "该手机号已注册");
        }

        User user = userService.register(phone, password, nickname);
        String token = jwtUtils.generateToken(user.getUserId(), user.getPhone(), user.getRole());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);

        return Result.success(data);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    public Result<User> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
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
     * 登出
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success("登出成功", null);
    }

    /**
     * 发送验证码（模拟）
     */
    @PostMapping("/code")
    public Result<Map<String, String>> sendVerifyCode(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        if (phone == null || phone.isEmpty()) {
            return Result.error(400, "手机号不能为空");
        }
        // 模拟发送验证码，实际项目中需要集成短信服务
        Map<String, String> data = new HashMap<>();
        data.put("code", "123456");
        return Result.success("验证码已发送", data);
    }

    /**
     * 验证码登录
     */
    @PostMapping("/login/code")
    public Result<Map<String, Object>> loginByCode(@RequestBody Map<String, String> loginData) {
        String phone = loginData.get("phone");
        String code = loginData.get("code");

        if (phone == null || phone.isEmpty()) {
            return Result.error(400, "手机号不能为空");
        }
        if (code == null || code.isEmpty()) {
            return Result.error(400, "验证码不能为空");
        }

        // 验证码校验（模拟：123456）
        if (!"123456".equals(code)) {
            return Result.error(401, "验证码错误");
        }

        User user = userService.findByPhone(phone);
        if (user == null) {
            return Result.error(401, "用户不存在，请先注册");
        }

        if (user.getStatus() == 1) {
            return Result.error(401, "账号已被禁用");
        }

        String token = jwtUtils.generateToken(user.getUserId(), user.getPhone(), user.getRole());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);

        return Result.success(data);
    }
}
