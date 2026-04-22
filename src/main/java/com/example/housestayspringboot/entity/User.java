package com.example.housestayspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer userId;
    private String phone;
    private String password;
    private String name;
    private String userEmail;
    private String idCard;
    private String realName;
    private Integer studentStatus;
    private String studentCard;
    private String avatar;
    private Integer userStatus;
    private LocalDateTime createTime;
    private Integer gender;
    private LocalDateTime birthday;
}
