package com.example.housestayspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("landlord_account")
public class LandlordAccount {
    @TableId(type = IdType.AUTO)
    private Integer accountId;
    private Integer landlordId;
    private String bankName;
    private String bankCardNo;
    private Integer accountStatus;
}
