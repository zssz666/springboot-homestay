package com.example.housestayspringboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.housestayspringboot.common.Result;
import com.example.housestayspringboot.entity.Order;
import com.example.housestayspringboot.entity.Homestay;
import com.example.housestayspringboot.entity.Landlord;
import com.example.housestayspringboot.service.OrderService;
import com.example.housestayspringboot.service.HomestayService;
import com.example.housestayspringboot.service.LandlordService;
import com.example.housestayspringboot.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private HomestayService homestayService;
    @Autowired
    private LandlordService landlordService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/create")
    public Result<Order> create(@RequestBody Map<String, Object> data, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Integer userId = jwtUtils.getUserId(token);

        Integer homestayId = Integer.parseInt(data.get("homestayId").toString());
        LocalDate inDate = LocalDate.parse(data.get("checkInDate").toString());
        LocalDate outDate = LocalDate.parse(data.get("checkOutDate").toString());

        Homestay homestay = homestayService.findById(homestayId);
        if (homestay == null) {
            return Result.<Order>error(404, "房源不存在");
        }

        long days = ChronoUnit.DAYS.between(inDate, outDate);
        if (days <= 0) {
            return Result.<Order>error(400, "退房日期必须晚于入住日期");
        }
        BigDecimal totalAmount = homestay.getPrice().multiply(BigDecimal.valueOf(days));

        Order order = new Order();
        order.setUserId(userId);
        order.setHomestayId(homestayId);
        order.setLandlordId(homestay.getLandlordId());
        order.setInDate(inDate);
        order.setOutDate(outDate);
        order.setContactName(data.get("contactName") != null ? data.get("contactName").toString() : null);
        order.setContactPhone(data.get("contactPhone") != null ? data.get("contactPhone").toString() : null);
        order.setTotalAmount(totalAmount);
        order.setDepositAmount(homestay.getDepositFee() != null ? BigDecimal.valueOf(homestay.getDepositFee()) : BigDecimal.ZERO);

        Order created = orderService.create(order, totalAmount);
        return Result.success("订单创建成功", created);
    }

    @GetMapping("/detail/{id}")
    public Result<Order> detail(@PathVariable Integer id) {
        Order order = orderService.findById(id);
        return Result.success(order);
    }

    @GetMapping("/my-list")
    public Result<Page<Order>> myList(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Integer userId = jwtUtils.getUserId(token);
        Page<Order> list = orderService.findByUserId(userId, page, size);
        return Result.success(list);
    }

    @GetMapping("/landlord-list")
    public Result<Page<Order>> landlordList(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Integer userId = jwtUtils.getUserId(token);

        Landlord landlord = landlordService.findById(userId);
        if (landlord == null) {
            return Result.<Page<Order>>error(400, "您还不是房东");
        }

        Page<Order> list = orderService.findByLandlordId(landlord.getLandlordId(), page, size);
        return Result.success(list);
    }

    @PostMapping("/cancel/{id}")
    public Result<Void> cancel(@PathVariable Integer id, @RequestBody Map<String, String> data) {
        String reason = data.getOrDefault("reason", "用户取消");
        orderService.cancel(id, reason);
        return Result.<Void>success("订单已取消", null);
    }

    @PostMapping("/pay/{id}")
    public Result<Void> pay(@PathVariable Integer id) {
        orderService.pay(id);
        return Result.<Void>success("支付成功", null);
    }

    @PostMapping("/confirm/{id}")
    public Result<Void> confirm(@PathVariable Integer id) {
        orderService.confirm(id);
        return Result.<Void>success("已确认", null);
    }

    @PostMapping("/check-in/{id}")
    public Result<Void> checkIn(@PathVariable Integer id) {
        orderService.checkIn(id);
        return Result.<Void>success("入住成功", null);
    }

    @PostMapping("/check-out/{id}")
    public Result<Void> checkOut(@PathVariable Integer id) {
        orderService.checkOut(id);
        return Result.<Void>success("退房成功", null);
    }
}
