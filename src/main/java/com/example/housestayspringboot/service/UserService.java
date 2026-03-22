package com.example.housestayspringboot.service;

import com.example.housestayspringboot.entity.User;

public interface UserService {

    /**
     * 根据手机号查询用户
     */
    User findByPhone(String phone);

    /**
     * 注册用户
     */
    User register(String phone, String password, String nickname);

    /**
     * 验证密码
     */
    boolean verifyPassword(String rawPassword, String encodedPassword);

    /**
     * 根据ID查询用户
     */
    User findById(Long userId);

    /**
     * 更新用户信息
     */
    User update(User user);

    /**
     * 修改密码
     */
    void updatePassword(Long userId, String newPassword);
}
