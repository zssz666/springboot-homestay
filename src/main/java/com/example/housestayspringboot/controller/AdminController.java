package com.example.housestayspringboot.controller;

import com.example.housestayspringboot.common.Result;
import com.example.housestayspringboot.entity.Admin;
import com.example.housestayspringboot.service.AdminService;
import com.example.housestayspringboot.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");
        
        Admin admin = adminService.login(username, password);
        if (admin == null) {
            return Result.error("用户名或密码错误");
        }
        
        String token = jwtUtils.generateAdminToken(admin.getAdminId(), admin.getUsername());
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("adminId", admin.getAdminId());
        result.put("username", admin.getUsername());
        result.put("realName", admin.getRealName());
        result.put("role", admin.getRole());
        
        return Result.success("登录成功", result);
    }

    @GetMapping("/info")
    public Result<Admin> info(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long adminId = jwtUtils.getAdminId(token);
        
        Admin admin = adminService.findById(adminId);
        return Result.success(admin);
    }
}
