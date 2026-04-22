package com.example.housestayspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.housestayspringboot.entity.Order;
import com.example.housestayspringboot.mapper.OrderMapper;
import com.example.housestayspringboot.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Order create(Order order, BigDecimal totalAmount) {
        order.setOrderNo(generateOrderNo());
        order.setTotalAmount(totalAmount);
        order.setOrderStatus(0);
        order.setPaidAmount(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO.toString());
        order.setIsReviewed(0);
        order.setCreateTime(LocalDateTime.now());
        order.setOrderUptime(LocalDateTime.now());
        orderMapper.insert(order);
        return order;
    }

    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6);
    }

    @Override
    public Order findById(Integer orderId) {
        return orderMapper.selectById(orderId);
    }

    @Override
    public Page<Order> findByUserId(Integer userId, int page, int size) {
        Page<Order> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId).orderByDesc(Order::getCreateTime);
        return orderMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public Page<Order> findByLandlordId(Integer landlordId, int page, int size) {
        Page<Order> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getLandlordId, landlordId).orderByDesc(Order::getCreateTime);
        return orderMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public void updateStatus(Integer orderId, Integer status) {
        Order order = findById(orderId);
        if (order != null) {
            order.setOrderStatus(status);
            order.setOrderUptime(LocalDateTime.now());
            orderMapper.updateById(order);
        }
    }

    @Override
    public void cancel(Integer orderId, String reason) {
        Order order = findById(orderId);
        if (order != null && order.getOrderStatus() == 0) {
            order.setOrderStatus(5);
            order.setCancelReason(reason);
            order.setOrderUptime(LocalDateTime.now());
            orderMapper.updateById(order);
        }
    }

    @Override
    public void pay(Integer orderId) {
        Order order = findById(orderId);
        if (order != null && order.getOrderStatus() == 0) {
            order.setOrderStatus(1);
            order.setOrderUptime(LocalDateTime.now());
            orderMapper.updateById(order);
        }
    }

    @Override
    public void confirm(Integer orderId) {
        Order order = findById(orderId);
        if (order != null && order.getOrderStatus() == 1) {
            order.setOrderStatus(2);
            order.setOrderUptime(LocalDateTime.now());
            orderMapper.updateById(order);
        }
    }

    @Override
    public void checkIn(Integer orderId) {
        Order order = findById(orderId);
        if (order != null && order.getOrderStatus() == 2) {
            order.setOrderStatus(3);
            order.setOrderUptime(LocalDateTime.now());
            orderMapper.updateById(order);
        }
    }

    @Override
    public void checkOut(Integer orderId) {
        Order order = findById(orderId);
        if (order != null && order.getOrderStatus() == 3) {
            order.setOrderStatus(4);
            order.setOrderUptime(LocalDateTime.now());
            orderMapper.updateById(order);
        }
    }
}
