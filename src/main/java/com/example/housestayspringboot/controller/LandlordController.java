package com.example.housestayspringboot.controller;

import com.example.housestayspringboot.common.Result;
import com.example.housestayspringboot.entity.Landlord;
import com.example.housestayspringboot.mapper.LandlordMapper;
import com.example.housestayspringboot.service.LandlordService;
import com.example.housestayspringboot.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/landlord")
public class LandlordController {

    @Autowired
    private LandlordService landlordService;

    @Autowired
    private LandlordMapper landlordMapper;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Result<Landlord> register(@RequestBody Map<String, String> data) {
        String phone = data.get("phone");
        String password = data.get("password");

        Landlord existing = landlordService.findByPhone(phone);
        if (existing != null) {
            return Result.error(400, "该手机号已注册为房东");
        }
        Landlord landlord = landlordService.register(phone, password,
                data.get("landlordName"), data.get("realName"), data.get("idCard"),
                data.get("idCardFront"), data.get("idCardBack"),
                data.get("landlordAvatar"), data.get("landlordIntroduce"),
                data.get("city"),
                data.get("houseType") != null && !data.get("houseType").isEmpty()
                        ? Integer.parseInt(data.get("houseType")) : null);
        return Result.success("注册成功", landlord);
    }

    @GetMapping("/info")
    public Result<Landlord> info(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Integer userId = jwtUtils.getUserId(token);

        Landlord landlord = landlordService.findById(userId);
        if (landlord == null) {
            return Result.<Landlord>error(400, "您还不是房东");
        }
        return Result.success(landlord);
    }

    @PutMapping("/profile")
    public Result<Landlord> updateProfile(@RequestBody Map<String, String> data,
                                          @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Integer userId = jwtUtils.getUserId(token);

        Landlord landlord = landlordService.findById(userId);
        if (landlord == null) {
            return Result.<Landlord>error(400, "您还不是房东");
        }

        if (data.containsKey("landlordAvatar")) {
            landlord.setLandlordAvatar(data.get("landlordAvatar"));
        }
        if (data.containsKey("landlordIntroduce")) {
            landlord.setLandlordIntroduce(data.get("landlordIntroduce"));
        }
        if (data.containsKey("landlordName")) {
            landlord.setLandlordName(data.get("landlordName"));
        }

        landlordMapper.updateById(landlord);
        return Result.success("更新成功", landlord);
    }
}
