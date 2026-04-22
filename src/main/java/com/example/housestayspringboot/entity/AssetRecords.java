package com.example.housestayspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("asset_records")
public class AssetRecords {
    @TableId(type = IdType.AUTO)
    private Integer recordsId;
    private Integer landlordId;
    private Integer recordsType;
    private Integer reasonType;
    private Integer sourceId;
    private BigDecimal recordsAmount;
    private LocalDateTime recordsTime;
}
