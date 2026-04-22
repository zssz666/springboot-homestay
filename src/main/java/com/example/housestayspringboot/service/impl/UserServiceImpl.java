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
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User register(String phone, String password, String nickname, Integer gender, String birthday) {
        User user = new User();
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(nickname != null && !nickname.isEmpty() ? nickname : "用户" + phone.substring(7));
        user.setAvatar("https://i.pravatar.cc/150?u=" + phone);
        user.setUserStatus(0);
        user.setCreateTime(LocalDateTime.now());
        if (gender != null) user.setGender(gender);
        if (birthday != null && !birthday.isEmpty()) {
            user.setBirthday(java.time.LocalDateTime.parse(birthday + "T00:00:00"));
        }
        userMapper.insert(user);
        return user;
    }

    @Override
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public User findById(Integer userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public User update(User user) {
        userMapper.updateById(user);
        return user;
    }

    @Override
    public void updatePassword(Integer userId, String newPassword) {
        User user = new User();
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
    }

    @Override
    public int getCertStatus(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user == null) return -1;
        if (user.getRealName() != null && !user.getRealName().isEmpty()
                && user.getIdCard() != null && !user.getIdCard().isEmpty()) {
            return 1; // 已认证
        }
        return 0; // 未认证
    }

    @Override
    public User submitRealNameCert(Integer userId, String realName, String idCard) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new RuntimeException("用户不存在");
        user.setRealName(realName);
        user.setIdCard(idCard);
        userMapper.updateById(user);
        return user;
    }

    @Override
    public int getStudentCertStatus(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user == null) return -1;
        Integer status = user.getStudentStatus();
        if (status == null || status == 0) return 0;
        return 1;
    }

    @Override
    public User submitStudentCert(Integer userId, String studentCardUrl) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new RuntimeException("用户不存在");
        user.setStudentCard(studentCardUrl);
        user.setStudentStatus(1);
        userMapper.updateById(user);
        return user;
    }

    @Override
    public int calculateAge(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user == null || user.getBirthday() == null) return -1;
        java.time.LocalDate birth = user.getBirthday().toLocalDate();
        java.time.LocalDate now = java.time.LocalDate.now();
        int age = now.getYear() - birth.getYear();
        if (now.getDayOfYear() < birth.getDayOfYear()) age--;
        return age;
    }
}
