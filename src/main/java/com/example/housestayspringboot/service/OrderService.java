package com.example.housestayspringboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.housestayspringboot.entity.Order;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface OrderService {
    Order create(Order order, BigDecimal totalAmount);
    Order findById(Integer orderId);
    Page<Order> findByUserId(Integer userId, int page, int size);
    Page<Order> findByLandlordId(Integer landlordId, int page, int size);
    void updateStatus(Integer orderId, Integer status);
    void cancel(Integer orderId, String reason);
    void pay(Integer orderId);
    void confirm(Integer orderId);
    void checkIn(Integer orderId);
    void checkOut(Integer orderId);
}
