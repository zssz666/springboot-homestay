package com.example.housestayspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long userId;
    private String phone;
    private String password;
    private String nickname;
    private String email;
    private String idCard;
    private String realName;
    private String avatar;
    private Integer status; // 0: 正常, 1: 禁用
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 非数据库字段，用于业务处理
    @TableField(exist = false)
    private String role; // guest, host, admin (运行时设置，不存储到数据库)

    @TableField(exist = false)
    private String username; // 用户名 (运行时生成，不存储到数据库)
}
