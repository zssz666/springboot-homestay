package com.example.housestayspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.housestayspringboot.entity.Landlord;
import com.example.housestayspringboot.mapper.LandlordMapper;
import com.example.housestayspringboot.service.LandlordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class LandlordServiceImpl implements LandlordService {
    @Autowired
    private LandlordMapper landlordMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Landlord findByUserId(Integer userId) {
        LambdaQueryWrapper<Landlord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Landlord::getLandlordId, userId);
        return landlordMapper.selectOne(wrapper);
    }

    @Override
    public Landlord findById(Integer landlordId) {
        return landlordMapper.selectById(landlordId);
    }

    @Override
    public Landlord findByPhone(String phone) {
        LambdaQueryWrapper<Landlord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Landlord::getPhone, phone);
        return landlordMapper.selectOne(wrapper);
    }

    @Override
    public Landlord login(String phone, String password) {
        Landlord landlord = findByPhone(phone);
        if (landlord == null) {
            return null;
        }
        if (!passwordEncoder.matches(password, landlord.getPassword())) {
            return null;
        }
        return landlord;
    }

    @Override
    public Landlord register(String phone, String password, String landlordName, String realName, String idCard,
                             String idCardFront, String idCardBack,
                             String landlordAvatar, String landlordIntroduce,
                             String city, Integer houseType) {
        Landlord landlord = new Landlord();
        landlord.setPhone(phone);
        landlord.setPassword(passwordEncoder.encode(password));
        landlord.setLandlordName(landlordName);
        landlord.setLandlordRealname(realName);
        landlord.setLandlordIdcard(idCard);
        landlord.setIdCardFront(idCardFront);
        landlord.setIdCardBack(idCardBack);
        landlord.setLandlordAvatar(landlordAvatar);
        landlord.setLandlordIntroduce(landlordIntroduce);
        landlord.setLandlordTags(houseType);
        landlord.setAuditStatus(0);
        landlord.setLandlordUptime(LocalDateTime.now());
        landlordMapper.insert(landlord);
        return landlord;
    }

    @Override
    public void updateAuditStatus(Integer landlordId, Integer auditStatus, String auditReason) {
        Landlord landlord = findById(landlordId);
        if (landlord != null) {
            landlord.setAuditStatus(auditStatus);
            landlord.setAuditReason(auditReason);
            landlord.setLandlordUptime(LocalDateTime.now());
            landlordMapper.updateById(landlord);
        }
    }
}
