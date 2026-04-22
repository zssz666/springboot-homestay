package com.example.housestayspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("refund_apply")
public class RefundApply {
    @TableId(type = IdType.AUTO)
    private Integer applyId;
    private Integer adminId;
    private Integer orderId;
    private Integer applyType;
    private Integer reasonType;
    private BigDecimal applyAmount;
    private BigDecimal actualAmount;
    private Integer applyStatus;
    private LocalDateTime landlordDotime;
    private String landlordRemark;
    private LocalDateTime adminDotime;
    private String adminRemark;
    private String evidenceImages;
    private LocalDateTime applyCreateat;
}
