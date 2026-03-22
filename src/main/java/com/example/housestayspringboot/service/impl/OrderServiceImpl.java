package com.example.housestayspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.housestayspringboot.entity.Order;
import com.example.housestayspringboot.mapper.OrderMapper;
import com.example.housestayspringboot.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    
    @Override
    public Order create(Order order) {
        order.setOrderNo(generateOrderNo());
        order.setStatus(0);
        order.setPayStatus(0);
        order.setCreatedAt(LocalDateTime.now());
        orderMapper.insert(order);
        return order;
    }
    
    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6);
    }
    
    @Override
    public Order findById(Long orderId) {
        return orderMapper.selectById(orderId);
    }
    
    @Override
    public Page<Order> findByUserId(Long userId, int page, int size) {
        Page<Order> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId).orderByDesc(Order::getCreatedAt);
        return orderMapper.selectPage(pageParam, wrapper);
    }
    
    @Override
    public Page<Order> findByLandlordId(Long landlordId, int page, int size) {
        Page<Order> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getLandlordId, landlordId).orderByDesc(Order::getCreatedAt);
        return orderMapper.selectPage(pageParam, wrapper);
    }
    
    @Override
    public void updateStatus(Long orderId, Integer status) {
        Order order = findById(orderId);
        if (order != null) {
            order.setStatus(status);
            orderMapper.updateById(order);
        }
    }
    
    @Override
    public void cancel(Long orderId, String reason) {
        Order order = findById(orderId);
        if (order != null && order.getStatus() == 0) {
            order.setStatus(5);
            order.setCancelReason(reason);
            orderMapper.updateById(order);
        }
    }
    
    @Override
    public void pay(Long orderId) {
        Order order = findById(orderId);
        if (order != null && order.getStatus() == 0) {
            order.setStatus(1);
            order.setPayStatus(1);
            order.setPayTime(LocalDateTime.now());
            orderMapper.updateById(order);
        }
    }
    
    @Override
    public void confirm(Long orderId) {
        Order order = findById(orderId);
        if (order != null && order.getStatus() == 1) {
            order.setStatus(2);
            order.setConfirmTime(LocalDateTime.now());
            orderMapper.updateById(order);
        }
    }
    
    @Override
    public void checkIn(Long orderId) {
        Order order = findById(orderId);
        if (order != null && order.getStatus() == 2) {
            order.setStatus(3);
            orderMapper.updateById(order);
        }
    }
    
    @Override
    public void checkOut(Long orderId) {
        Order order = findById(orderId);
        if (order != null && order.getStatus() == 3) {
            order.setStatus(4);
            orderMapper.updateById(order);
        }
    }
}
