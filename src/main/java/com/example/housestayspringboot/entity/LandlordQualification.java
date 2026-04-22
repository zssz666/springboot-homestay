package com.example.housestayspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("landlord_qualification")
public class LandlordQualification {
    @TableId(type = IdType.AUTO)
    private Integer qualificationId;
    private Integer adminId;
    private Integer landlordId;
    private Integer qualificationType;
    private String certificateNo;
    private String certificateImage;
    private String qualificationAddress;
    private BigDecimal qualificationArea;
    private Integer qualificationStatus;
    private String qualificationReason;
    private LocalDateTime uptimeAt;
}
