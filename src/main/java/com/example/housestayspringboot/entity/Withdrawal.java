package com.example.housestayspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("withdrawal")
public class Withdrawal {
    @TableId(type = IdType.AUTO)
    private Integer withdrawalId;
    private Integer landlordId;
    private Integer adminId;
    private BigDecimal withdrawalAmount;
    private BigDecimal fee;
    private Integer withdrawalStatus;
    private LocalDateTime applyTime;
    private LocalDateTime adminChecktime;
    private String adminReason;
    private String bankTradeNo;
    private LocalDateTime withdrawalUptime;
    private String bankName;
    private String bankCard;
}
