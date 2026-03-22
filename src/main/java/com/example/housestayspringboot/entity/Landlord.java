package com.example.housestayspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_landlord")
public class Landlord {
    @TableId(type = IdType.AUTO)
    private Long landlordId;
    private Long userId;
    private String idCardFront;
    private String idCardBack;
    private String propertyCert;
    private Integer auditStatus; // 0-待审 1-通过 2-驳回
    private String auditReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
