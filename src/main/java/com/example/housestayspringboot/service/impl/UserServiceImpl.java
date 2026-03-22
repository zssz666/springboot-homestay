package com.example.housestayspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.housestayspringboot.entity.User;
import com.example.housestayspringboot.mapper.UserMapper;
import com.example.housestayspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findByPhone(String phone) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        User user = userMapper.selectOne(wrapper);
        if (user != null) {
            // 运行时设置role和username，不存储到数据库
            user.setRole("guest");
            user.setUsername("user_" + user.getUserId());
        }
        return user;
    }

    @Override
    public User register(String phone, String password, String nickname) {
        User user = new User();
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(nickname != null && !nickname.isEmpty() ? nickname : "用户" + phone.substring(7));
        user.setAvatar("https://i.pravatar.cc/150?u=" + phone);
        user.setStatus(0); // 0: 正常
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.insert(user);

        // 运行时设置role和username
        user.setRole("guest");
        user.setUsername("user_" + user.getUserId());
        return user;
    }

    @Override
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public User findById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user != null) {
            // 运行时设置role和username，不存储到数据库
            user.setRole("guest");
            user.setUsername("user_" + user.getUserId());
        }
        return user;
    }

    @Override
    public User update(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
        return user;
    }

    @Override
    public void updatePassword(Long userId, String newPassword) {
        User user = new User();
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
    }
}
