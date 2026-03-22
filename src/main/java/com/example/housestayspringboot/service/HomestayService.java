package com.example.housestayspringboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.housestayspringboot.entity.Homestay;
import java.util.List;

public interface HomestayService {
    Homestay create(Homestay homestay);
    Homestay update(Homestay homestay);
    void delete(Long homestayId);
    Homestay findById(Long homestayId);
    List<Homestay> findByLandlordId(Long landlordId);
    Page<Homestay> findByCity(String cityCode, int page, int size);
    Page<Homestay> findAll(int page, int size);
    Page<Homestay> search(String keyword, String cityCode, Integer minPrice, Integer maxPrice, Integer status, int page, int size);
    void updateStatus(Long homestayId, Integer status);
    void updateAuditStatus(Long homestayId, Integer auditStatus);
}
