package com.example.housestayspringboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.housestayspringboot.common.Result;
import com.example.housestayspringboot.entity.Homestay;
import com.example.housestayspringboot.entity.Landlord;
import com.example.housestayspringboot.service.HomestayService;
import com.example.housestayspringboot.service.LandlordService;
import com.example.housestayspringboot.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/homestay")
public class HomestayController {
    @Autowired
    private HomestayService homestayService;
    @Autowired
    private LandlordService landlordService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/create")
    public Result<Homestay> create(@RequestBody Homestay homestay, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Integer userId = jwtUtils.getUserId(token);

        Landlord landlord = landlordService.findById(userId);
        if (landlord == null || landlord.getAuditStatus() != 1) {
            return Result.<Homestay>error(400, "请先完成房东认证");
        }
        homestay.setLandlordId(landlord.getLandlordId());
        Homestay created = homestayService.create(homestay);
        return Result.success("房源创建成功", created);
    }

    @PutMapping("/update")
    public Result<Homestay> update(@RequestBody Homestay homestay) {
        Homestay updated = homestayService.update(homestay);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        homestayService.delete(id);
        return Result.<Void>success("删除成功", null);
    }

    @GetMapping("/detail/{id}")
    public Result<Homestay> detail(@PathVariable Integer id) {
        Homestay homestay = homestayService.findById(id);
        return Result.success(homestay);
    }

    @GetMapping("/list")
    public Result<Page<Homestay>> list(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        Page<Homestay> list = homestayService.findAll(page, size);
        return Result.success(list);
    }

    @GetMapping("/my-list")
    public Result<List<Homestay>> myList(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Integer userId = jwtUtils.getUserId(token);

        Landlord landlord = landlordService.findById(userId);
        if (landlord == null) {
            return Result.<List<Homestay>>success("暂无房源", null);
        }
        List<Homestay> list = homestayService.findByLandlordId(landlord.getLandlordId());
        return Result.success(list);
    }

    @GetMapping("/city/{city}")
    public Result<Page<Homestay>> listByCity(@PathVariable String city,
                                              @RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        Page<Homestay> list = homestayService.findByCity(city, page, size);
        return Result.success(list);
    }

    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(@PathVariable Integer id, @RequestParam Integer status) {
        homestayService.updateStatus(id, status);
        return Result.<Void>success("状态更新成功", null);
    }

    @GetMapping("/search")
    public Result<Page<Homestay>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Homestay> list = homestayService.search(keyword, city, minPrice, maxPrice, status, page, size);
        return Result.success(list);
    }
}
