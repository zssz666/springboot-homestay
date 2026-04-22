package com.example.housestayspringboot.service;

import com.example.housestayspringboot.entity.User;

public interface UserService {

    User findByPhone(String phone);

    User register(String phone, String password, String nickname, Integer gender, String birthday);

    boolean verifyPassword(String rawPassword, String encodedPassword);

    User findById(Integer userId);

    User update(User user);

    void updatePassword(Integer userId, String newPassword);

    int getCertStatus(Integer userId);

    User submitRealNameCert(Integer userId, String realName, String idCard);

    int getStudentCertStatus(Integer userId);

    User submitStudentCert(Integer userId, String studentCardUrl);

    int calculateAge(Integer userId);
}
