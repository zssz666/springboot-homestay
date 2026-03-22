package com.example.housestayspringboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.housestayspringboot.entity.Order;
import java.time.LocalDate;

public interface OrderService {
    Order create(Order order);
    Order findById(Long orderId);
    Page<Order> findByUserId(Long userId, int page, int size);
    Page<Order> findByLandlordId(Long landlordId, int page, int size);
    void updateStatus(Long orderId, Integer status);
    void cancel(Long orderId, String reason);
    void pay(Long orderId);
    void confirm(Long orderId);
    void checkIn(Long orderId);
    void checkOut(Long orderId);
}
