package com.example.housestayspringboot.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 生成密码的 BCrypt 哈希
        System.out.println("123456 的 BCrypt 哈希: " + encoder.encode("123456"));
        System.out.println("12345678 的 BCrypt 哈希: " + encoder.encode("12345678"));
        // 验证
        String hash = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi";
        System.out.println("验证 123456: " + encoder.matches("123456", hash));
    }
}
