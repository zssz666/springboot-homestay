package com.example.housestayspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.housestayspringboot.entity.Homestay;
import com.example.housestayspringboot.mapper.HomestayMapper;
import com.example.housestayspringboot.service.HomestayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HomestayServiceImpl implements HomestayService {
    @Autowired
    private HomestayMapper homestayMapper;

    @Override
    public Homestay create(Homestay homestay) {
        homestay.setHomestayStatus(0);
        homestay.setUdStatus(0);
        homestay.setHomestayUptime(LocalDateTime.now());
        homestayMapper.insert(homestay);
        return homestay;
    }

    @Override
    public Homestay update(Homestay homestay) {
        homestay.setHomestayUptime(LocalDateTime.now());
        homestayMapper.updateById(homestay);
        return homestay;
    }

    @Override
    public void delete(Integer homestayId) {
        homestayMapper.deleteById(homestayId);
    }

    @Override
    public Homestay findById(Integer homestayId) {
        return homestayMapper.selectById(homestayId);
    }

    @Override
    public List<Homestay> findByLandlordId(Integer landlordId) {
        LambdaQueryWrapper<Homestay> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Homestay::getLandlordId, landlordId);
        return homestayMapper.selectList(wrapper);
    }

    @Override
    public Page<Homestay> findByCity(String city, int page, int size) {
        Page<Homestay> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Homestay> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Homestay::getCity, city)
               .eq(Homestay::getUdStatus, 1)
               .eq(Homestay::getHomestayStatus, 1);
        return homestayMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public Page<Homestay> findAll(int page, int size) {
        Page<Homestay> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Homestay> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Homestay::getUdStatus, 1)
               .eq(Homestay::getHomestayStatus, 1)
               .orderByDesc(Homestay::getHomestayUptime);
        return homestayMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public void updateStatus(Integer homestayId, Integer status) {
        Homestay homestay = findById(homestayId);
        if (homestay != null) {
            homestay.setUdStatus(status);
            homestay.setHomestayUptime(LocalDateTime.now());
            homestayMapper.updateById(homestay);
        }
    }

    @Override
    public void updateAuditStatus(Integer homestayId, Integer auditStatus) {
        Homestay homestay = findById(homestayId);
        if (homestay != null) {
            homestay.setHomestayStatus(auditStatus);
            homestay.setHomestayUptime(LocalDateTime.now());
            homestayMapper.updateById(homestay);
        }
    }

    @Override
    public Page<Homestay> search(String keyword, String city, Integer minPrice, Integer maxPrice, Integer status, int page, int size) {
        Page<Homestay> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Homestay> wrapper = new LambdaQueryWrapper<>();

        if (status == null) {
            wrapper.eq(Homestay::getUdStatus, 1)
                   .eq(Homestay::getHomestayStatus, 1);
        } else {
            wrapper.eq(Homestay::getUdStatus, status);
        }

        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Homestay::getTitle, keyword)
                    .or().like(Homestay::getAddress, keyword)
                    .or().like(Homestay::getDescription, keyword));
        }

        if (city != null && !city.isEmpty()) {
            wrapper.eq(Homestay::getCity, city);
        }

        if (minPrice != null) {
            wrapper.ge(Homestay::getPrice, minPrice);
        }

        if (maxPrice != null) {
            wrapper.le(Homestay::getPrice, maxPrice);
        }

        wrapper.orderByDesc(Homestay::getHomestayUptime);
        return homestayMapper.selectPage(pageParam, wrapper);
    }
}
