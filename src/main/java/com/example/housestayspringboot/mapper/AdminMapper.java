package com.example.housestayspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.housestayspringboot.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
}
