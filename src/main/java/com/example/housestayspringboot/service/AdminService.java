package com.example.housestayspringboot.service;

import com.example.housestayspringboot.entity.Admin;

public interface AdminService {
    Admin login(String username, String password);
    Admin findById(Long adminId);
}
