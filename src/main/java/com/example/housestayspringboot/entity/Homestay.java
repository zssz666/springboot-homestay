package com.example.housestayspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_homestay")
public class Homestay {
    @TableId(type = IdType.AUTO)
    private Long homestayId;
    private Long landlordId;
    private String title;
    private String description;
    private String address;
    private String cityCode;
    private BigDecimal price;
    private Integer area;
    private Integer roomCount;
    private Integer maxGuests;
    private String facilities; // JSON字符串存储
    private String images; // JSON字符串存储
    private Integer auditStatus; // 0-待审 1-通过 2-驳回
    private Integer status; // 0-下架 1-上架
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
