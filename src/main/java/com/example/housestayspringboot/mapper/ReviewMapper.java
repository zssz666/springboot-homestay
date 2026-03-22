package com.example.housestayspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.housestayspringboot.entity.Review;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReviewMapper extends BaseMapper<Review> {
}
