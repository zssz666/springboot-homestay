package com.example.housestayspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("homestay_images")
public class HomestayImages {
    @TableId(type = IdType.AUTO)
    private Integer imageId;
    private Integer homestayId;
    private String imageUrl;
    private Integer sortOrder;
}
