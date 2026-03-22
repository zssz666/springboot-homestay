package com.example.housestayspringboot.controller;

import com.example.housestayspringboot.common.Result;
import com.example.housestayspringboot.entity.Landlord;
import com.example.housestayspringboot.entity.User;
import com.example.housestayspringboot.service.LandlordService;
import com.example.housestayspringboot.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/landlord")
public class LandlordController {
    @Autowired
    private LandlordService landlordService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/apply")
    public Result<Landlord> apply(@RequestBody Map<String, String> data, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtUtils.getUserId(token);
        
        String idCardFront = data.get("idCardFront");
        String idCardBack = data.get("idCardBack");
        String propertyCert = data.get("propertyCert");
        
        Landlord landlord = landlordService.apply(userId, idCardFront, idCardBack, propertyCert);
        return Result.success("申请提交成功", landlord);
    }

    @GetMapping("/info")
    public Result<Landlord> getInfo(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtUtils.getUserId(token);
        
        Landlord landlord = landlordService.findByUserId(userId);
        if (landlord == null) {
            return Result.success("未申请房东", null);
        }
        return Result.success(landlord);
    }
}
