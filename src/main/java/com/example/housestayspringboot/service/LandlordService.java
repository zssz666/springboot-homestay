package com.example.housestayspringboot.service;

import com.example.housestayspringboot.entity.Landlord;

public interface LandlordService {
    Landlord findByUserId(Integer userId);
    Landlord findById(Integer landlordId);
    Landlord findByPhone(String phone);
    Landlord register(String phone, String password, String landlordName, String realName, String idCard,
                      String idCardFront, String idCardBack,
                      String landlordAvatar, String landlordIntroduce,
                      String city, Integer houseType);
    void updateAuditStatus(Integer landlordId, Integer auditStatus, String auditReason);
}
