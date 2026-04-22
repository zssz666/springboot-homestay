package com.example.housestayspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("landlord")
public class Landlord {
    @TableId(type = IdType.AUTO)
    private Integer landlordId;
    private Integer adminId;
    private String phone;
    private String password;
    private String landlordName;
    private String landlordAvatar;
    private String landlordIntroduce;
    private Integer landlordTags;
    private String landlordRealname;
    private String landlordIdcard;
    private String idCardFront;
    private String idCardBack;
    private Integer auditStatus;
    private String auditReason;
    private Integer landlordStatus;
    private LocalDateTime landlordUptime;
}
