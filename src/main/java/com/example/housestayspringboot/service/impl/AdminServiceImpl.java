package com.example.housestayspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.housestayspringboot.entity.Admin;
import com.example.housestayspringboot.mapper.AdminMapper;
import com.example.housestayspringboot.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin login(String username, String password) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, username)
               .eq(Admin::getPassword, password)
               .eq(Admin::getStatus, 1);
        return adminMapper.selectOne(wrapper);
    }

    @Override
    public Admin findById(Long adminId) {
        return adminMapper.selectById(adminId);
    }
}
