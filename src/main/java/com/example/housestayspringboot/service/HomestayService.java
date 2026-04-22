package com.example.housestayspringboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.housestayspringboot.entity.Homestay;
import java.util.List;

public interface HomestayService {
    Homestay create(Homestay homestay);
    Homestay update(Homestay homestay);
    void delete(Integer homestayId);
    Homestay findById(Integer homestayId);
    List<Homestay> findByLandlordId(Integer landlordId);
    Page<Homestay> findByCity(String city, int page, int size);
    Page<Homestay> findAll(int page, int size);
    Page<Homestay> search(String keyword, String city, Integer minPrice, Integer maxPrice, Integer status, int page, int size);
    void updateStatus(Integer homestayId, Integer status);
    void updateAuditStatus(Integer homestayId, Integer auditStatus);
}
