package com.example.housestayspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_review")
public class Review {
    @TableId(type = IdType.AUTO)
    private Long reviewId;
    private Long orderId;
    private Long userId;
    private Long homestayId;
    private Long landlordId;
    private Integer rating;
    private String content;
    private String images;
    private String replyContent;
    private LocalDateTime replyTime;
    private Integer auditStatus; // 0-待审 1-通过 2-驳回
    private LocalDateTime createdAt;

    // 非数据库字段
    @TableField(exist = false)
    private User user;
}
