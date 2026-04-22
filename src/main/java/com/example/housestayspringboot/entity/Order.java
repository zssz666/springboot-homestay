package com.example.housestayspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer orderId;
    private Integer homestayId;
    private Integer userId;
    private Integer landlordId;
    private Integer reviewId;
    private String orderNo;
    private LocalDate inDate;
    private LocalDate outDate;
    private String contactName;
    private String contactPhone;
    private BigDecimal depositAmount;
    private BigDecimal paidAmount;
    private BigDecimal totalAmount;
    private String discountAmount;
    private Integer orderStatus;
    private Integer checkIncode;
    private String cancelReason;
    private Integer isReviewed;
    private LocalDateTime orderUptime;
    private LocalDateTime createTime;
}
