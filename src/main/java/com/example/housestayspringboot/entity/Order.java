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
@TableName("t_order")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long orderId;
    private String orderNo;
    private Long userId;
    private Long homestayId;
    private Long landlordId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BigDecimal totalAmount;
    private BigDecimal depositAmount;
    private Integer status; // 0-待支付 1-待确认 2-待入住 3-入住中 4-已完成 5-已取消
    private Integer payStatus;
    private String cancelReason;
    private LocalDateTime createdAt;
    private LocalDateTime payTime;
    private LocalDateTime confirmTime;

    // 非数据库字段
    @TableField(exist = false)
    private Homestay homestayInfo;
}
