package com.example.housestayspringboot.controller;

import com.example.housestayspringboot.common.Result;
import com.example.housestayspringboot.entity.Landlord;
import com.example.housestayspringboot.mapper.LandlordMapper;
import com.example.housestayspringboot.service.LandlordService;
import com.example.housestayspringboot.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
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

    /**
     * 房东登录（支持验证码或密码）
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> data) {
        String phone = data.get("phone");
        String password = data.get("password");
        String code = data.get("code");

        if (phone == null || phone.isEmpty()) {
            return Result.<Map<String, Object>>error(400, "手机号不能为空");
        }

        // 先查房东记录是否存在
        Landlord landlord = landlordService.findByPhone(phone);
        if (landlord == null) {
            return Result.<Map<String, Object>>error(401, "该手机号尚未注册为房东，请先申请入驻");
        }

        // 验证码登录
        if (code != null && !code.isEmpty()) {
            if (!"123456".equals(code)) {
                return Result.<Map<String, Object>>error(400, "验证码错误");
            }
        } else if (password != null && !password.isEmpty()) {
            // 密码登录
            Landlord loginResult = landlordService.login(phone, password);
            if (loginResult == null) {
                return Result.<Map<String, Object>>error(401, "密码错误");
            }
        } else {
            return Result.<Map<String, Object>>error(400, "请提供密码或验证码");
        }

        // 检查审核状态
        if (landlord.getAuditStatus() == null || landlord.getAuditStatus() == 0) {
            return Result.<Map<String, Object>>error(403, "您的房东入驻申请正在审核中，请耐心等待，审核结果会以短信通知您");
        }
        if (landlord.getAuditStatus() == -1) {
            return Result.<Map<String, Object>>error(403, "您的房东入驻申请未通过审核：" + (landlord.getAuditReason() != null ? landlord.getAuditReason() : ""));
        }
        if (landlord.getLandlordStatus() != null && landlord.getLandlordStatus() == 1) {
            return Result.<Map<String, Object>>error(401, "账号已被禁用");
        }

        String token = jwtUtils.generateLandlordToken(landlord.getLandlordId(), landlord.getPhone());
        landlord.setPassword(null);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("landlord", landlord);

        return Result.success("登录成功", result);
    }

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
    public Result<Landlord> info(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null) return Result.<Landlord>error(401, "未登录");
        String token = authHeader.substring(7);
        Integer landlordId = jwtUtils.getLandlordId(token);

        Landlord landlord = landlordService.findById(landlordId);
        if (landlord == null) {
            return Result.<Landlord>error(400, "您还不是房东");
        }
        landlord.setPassword(null);
        return Result.success(landlord);
    }

    @PutMapping("/profile")
    public Result<Landlord> updateProfile(@RequestBody Map<String, String> data,
                                          @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null) return Result.<Landlord>error(401, "未登录");
        String token = authHeader.substring(7);
        Integer landlordId = jwtUtils.getLandlordId(token);

        Landlord landlord = landlordService.findById(landlordId);
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
        landlord.setPassword(null);
        return Result.success("更新成功", landlord);
    }
}
