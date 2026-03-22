package com.example.housestayspringboot.service;

import com.example.housestayspringboot.entity.Landlord;

public interface LandlordService {
    Landlord findByUserId(Long userId);
    Landlord findById(Long landlordId);
    Landlord apply(Long userId, String idCardFront, String idCardBack, String propertyCert);
    void updateAuditStatus(Long landlordId, Integer auditStatus, String auditReason);
}
