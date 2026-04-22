package com.example.housestayspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("homestay")
public class Homestay {
    @TableId(type = IdType.AUTO)
    private Integer homestayId;
    private Integer adminId;
    private Integer landlordId;
    private String title;
    private String description;
    private Integer houseType;
    private String city;
    private String address;
    private BigDecimal price;
    private Integer area;
    private Integer roomCount;
    private Integer bedCount;
    private Integer maxGuests;
    private String facilities;
    private BigDecimal basePrice;
    private BigDecimal holidayPrice;
    private BigDecimal cleaningFee;
    private Integer depositFee;
    private Integer homestayStatus;
    private String homestayReason;
    private Integer udStatus;
    private LocalDateTime homestayUptime;
}
