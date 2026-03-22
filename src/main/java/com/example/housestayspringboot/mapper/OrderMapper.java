package com.example.housestayspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.housestayspringboot.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
