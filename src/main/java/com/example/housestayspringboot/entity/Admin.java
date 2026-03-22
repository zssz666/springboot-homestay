package com.example.housestayspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_admin")
public class Admin {
    @TableId(type = IdType.AUTO)
    private Long adminId;
    private String username;
    private String password;
    private String realName;
    private Integer role; // 1-运营 2-财务 3-超管
    private Integer status; // 0-禁用 1-启用
    private LocalDateTime createdAt;
}
