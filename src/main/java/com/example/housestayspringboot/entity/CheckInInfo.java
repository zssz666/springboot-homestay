package com.example.housestayspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("check_in_info")
public class CheckInInfo {
    @TableId(type = IdType.AUTO)
    private Integer checkInId;
    private Integer orderId;
    private Integer guestId;
}
