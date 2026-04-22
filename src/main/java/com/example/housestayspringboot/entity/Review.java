package com.example.housestayspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("review")
public class Review {
    @TableId(type = IdType.AUTO)
    private Integer reviewId;
    private Integer homestayId;
    private Integer adminId;
    private Integer userId;
    private Integer orderId;
    private Integer rating;
    private String content;
    private String images;
    private String replyContent;
    private LocalDateTime replyTime;
    private Integer isshow;
    private LocalDateTime reviewTime;
}
