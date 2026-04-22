package com.example.housestayspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("payment")
public class Payment {
    @TableId(type = IdType.AUTO)
    private Integer paymentId;
    private Integer orderId;
    private BigDecimal amount;
    private Integer type;
    private String payChannel;
    private String thirdNo;
    private Integer paymentStatus;
    private LocalDateTime paymentTime;
    private LocalDateTime createTime;
}
