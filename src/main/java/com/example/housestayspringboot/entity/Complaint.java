package com.example.housestayspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("complaint")
public class Complaint {
    @TableId(type = IdType.AUTO)
    private Integer complaintId;
    private Integer orderId;
    private Integer adminId;
    private Integer complaintType;
    private String complaintContent;
    private String evidenceImages;
    private BigDecimal claimAmount;
    private String landlordResponse;
    private LocalDateTime landlordResponsetime;
    private LocalDateTime adminHandledTime;
    private String adminRemark;
    private Integer complaintStatus;
    private Integer result;
    private BigDecimal refundAmount;
    private LocalDateTime complainTime;
}
