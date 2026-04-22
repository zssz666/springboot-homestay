package com.example.housestayspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.housestayspringboot.entity.Admin;
import com.example.housestayspringboot.mapper.AdminMapper;
import com.example.housestayspringboot.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Admin login(String username, String password) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getAdminName, username)
               .eq(Admin::getAdminStatus, 1);
        Admin admin = adminMapper.selectOne(wrapper);
        if (admin != null && passwordEncoder.matches(password, admin.getAdminPassword())) {
            return admin;
        }
        return null;
    }

    @Override
    public Admin findById(Integer adminId) {
        return adminMapper.selectById(adminId);
    }
}
