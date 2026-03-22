package com.example.housestayspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.housestayspringboot.entity.Landlord;
import com.example.housestayspringboot.mapper.LandlordMapper;
import com.example.housestayspringboot.service.LandlordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class LandlordServiceImpl implements LandlordService {
    @Autowired
    private LandlordMapper landlordMapper;

    @Override
    public Landlord findByUserId(Long userId) {
        LambdaQueryWrapper<Landlord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Landlord::getUserId, userId);
        return landlordMapper.selectOne(wrapper);
    }

    @Override
    public Landlord findById(Long landlordId) {
        return landlordMapper.selectById(landlordId);
    }

    @Override
    public Landlord apply(Long userId, String idCardFront, String idCardBack, String propertyCert) {
        Landlord existing = findByUserId(userId);
        if (existing != null) {
            existing.setIdCardFront(idCardFront);
            existing.setIdCardBack(idCardBack);
            existing.setPropertyCert(propertyCert);
            existing.setAuditStatus(0);
            existing.setUpdatedAt(LocalDateTime.now());
            landlordMapper.updateById(existing);
            return existing;
        }
        Landlord landlord = new Landlord();
        landlord.setUserId(userId);
        landlord.setIdCardFront(idCardFront);
        landlord.setIdCardBack(idCardBack);
        landlord.setPropertyCert(propertyCert);
        landlord.setAuditStatus(0);
        landlord.setCreatedAt(LocalDateTime.now());
        landlord.setUpdatedAt(LocalDateTime.now());
        landlordMapper.insert(landlord);
        return landlord;
    }

    @Override
    public void updateAuditStatus(Long landlordId, Integer auditStatus, String auditReason) {
        Landlord landlord = findById(landlordId);
        if (landlord != null) {
            landlord.setAuditStatus(auditStatus);
            landlord.setAuditReason(auditReason);
            landlord.setUpdatedAt(LocalDateTime.now());
            landlordMapper.updateById(landlord);
        }
    }
}
