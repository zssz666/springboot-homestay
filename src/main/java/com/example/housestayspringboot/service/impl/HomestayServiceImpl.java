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
        homestay.setAuditStatus(0);
        homestay.setStatus(0);
        homestay.setCreatedAt(LocalDateTime.now());
        homestay.setUpdatedAt(LocalDateTime.now());
        homestayMapper.insert(homestay);
        return homestay;
    }

    @Override
    public Homestay update(Homestay homestay) {
        homestay.setUpdatedAt(LocalDateTime.now());
        homestayMapper.updateById(homestay);
        return homestay;
    }

    @Override
    public void delete(Long homestayId) {
        homestayMapper.deleteById(homestayId);
    }

    @Override
    public Homestay findById(Long homestayId) {
        return homestayMapper.selectById(homestayId);
    }

    @Override
    public List<Homestay> findByLandlordId(Long landlordId) {
        LambdaQueryWrapper<Homestay> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Homestay::getLandlordId, landlordId);
        return homestayMapper.selectList(wrapper);
    }

    @Override
    public Page<Homestay> findByCity(String cityCode, int page, int size) {
        Page<Homestay> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Homestay> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Homestay::getCityCode, cityCode)
               .eq(Homestay::getStatus, 1)
               .eq(Homestay::getAuditStatus, 1);
        return homestayMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public Page<Homestay> findAll(int page, int size) {
        Page<Homestay> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Homestay> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Homestay::getStatus, 1)
               .eq(Homestay::getAuditStatus, 1)
               .orderByDesc(Homestay::getCreatedAt);
        return homestayMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public void updateStatus(Long homestayId, Integer status) {
        Homestay homestay = findById(homestayId);
        if (homestay != null) {
            homestay.setStatus(status);
            homestay.setUpdatedAt(LocalDateTime.now());
            homestayMapper.updateById(homestay);
        }
    }

    @Override
    public void updateAuditStatus(Long homestayId, Integer auditStatus) {
        Homestay homestay = findById(homestayId);
        if (homestay != null) {
            homestay.setAuditStatus(auditStatus);
            homestay.setUpdatedAt(LocalDateTime.now());
            homestayMapper.updateById(homestay);
        }
    }

    @Override
    public Page<Homestay> search(String keyword, String cityCode, Integer minPrice, Integer maxPrice, Integer status, int page, int size) {
        Page<Homestay> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Homestay> wrapper = new LambdaQueryWrapper<>();

        // 默认只显示已审核通过且上架的房源
        if (status == null) {
            wrapper.eq(Homestay::getStatus, 1)
                   .eq(Homestay::getAuditStatus, 1);
        } else {
            wrapper.eq(Homestay::getStatus, status);
        }

        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Homestay::getTitle, keyword)
                    .or().like(Homestay::getAddress, keyword)
                    .or().like(Homestay::getDescription, keyword));
        }

        if (cityCode != null && !cityCode.isEmpty()) {
            wrapper.eq(Homestay::getCityCode, cityCode);
        }

        if (minPrice != null) {
            wrapper.ge(Homestay::getPrice, minPrice);
        }

        if (maxPrice != null) {
            wrapper.le(Homestay::getPrice, maxPrice);
        }

        wrapper.orderByDesc(Homestay::getCreatedAt);
        return homestayMapper.selectPage(pageParam, wrapper);
    }
}
