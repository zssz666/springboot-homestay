package com.example.housestayspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("guest")
public class Guest {
    @TableId(type = IdType.AUTO)
    private Integer guestId;
    private Integer userId;
    private String guestName;
    private String guestIdCard;
    private String guestPhone;
    private Integer isDefault;
}
