package com.example.housestayspringboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.housestayspringboot.common.Result;
import com.example.housestayspringboot.entity.Homestay;
import com.example.housestayspringboot.entity.HomestayImages;
import com.example.housestayspringboot.entity.Landlord;
import com.example.housestayspringboot.service.HomestayService;
import com.example.housestayspringboot.service.HomestayImagesService;
import com.example.housestayspringboot.service.LandlordService;
import com.example.housestayspringboot.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/homestay")
public class HomestayController {
    @Autowired
    private HomestayService homestayService;
    @Autowired
    private HomestayImagesService homestayImagesService;
    @Autowired
    private LandlordService landlordService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/create")
    public Result<Homestay> create(@RequestBody Homestay homestay, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null) return Result.<Homestay>error(401, "未登录");
        String token = authHeader.substring(7);
        Integer landlordId = jwtUtils.getLandlordId(token);

        Landlord landlord = landlordService.findById(landlordId);
        if (landlord == null || landlord.getAuditStatus() != 1) {
            return Result.<Homestay>error(400, "请先完成房东认证");
        }
        homestay.setLandlordId(landlord.getLandlordId());
        if (homestay.getBasePrice() == null) {
            homestay.setBasePrice(homestay.getPrice());
        }
        Homestay created = homestayService.create(homestay);
        return Result.success("房源创建成功，等待管理员审核", created);
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
    public Result<List<Homestay>> myList(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null) return Result.<List<Homestay>>error(401, "未登录");
        String token = authHeader.substring(7);
        Integer landlordId = jwtUtils.getLandlordId(token);

        Landlord landlord = landlordService.findById(landlordId);
        if (landlord == null) {
            return Result.<List<Homestay>>success("暂无房源", List.of());
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

    // ==================== 房源图片管理 ====================

    /**
     * 获取房源图片列表
     */
    @GetMapping("/{id}/images")
    public Result<List<HomestayImages>> getImages(@PathVariable Integer id) {
        List<HomestayImages> images = homestayImagesService.findByHomestayId(id);
        return Result.success(images);
    }

    /**
     * 保存/更新房源图片（提交审核前调用，图片 URL 来自前端上传到 COS）
     */
    @PostMapping("/{id}/images")
    public Result<Void> saveImages(
            @PathVariable Integer id,
            @RequestBody List<String> imageUrls,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null) return Result.<Void>error(401, "未登录");
        String token = authHeader.substring(7);
        Integer landlordId = jwtUtils.getLandlordId(token);

        Homestay homestay = homestayService.findById(id);
        if (homestay == null) {
            return Result.<Void>error(404, "房源不存在");
        }
        if (!homestay.getLandlordId().equals(landlordId)) {
            return Result.<Void>error(403, "无权操作此房源");
        }
        if (homestay.getHomestayStatus() != 0) {
            return Result.<Void>error(400, "只有在审核中或未提交时可更新图片");
        }

        homestayImagesService.saveImages(id, imageUrls);
        return Result.<Void>success("图片保存成功", null);
    }

    @PostMapping("/create-and-submit")
    public Result<Homestay> createAndSubmit(
            @RequestBody Map<String, Object> data,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null) return Result.<Homestay>error(401, "未登录");
        String token = authHeader.substring(7);
        Integer landlordId = jwtUtils.getLandlordId(token);

        Landlord landlord = landlordService.findById(landlordId);
        if (landlord == null || landlord.getAuditStatus() != 1) {
            return Result.<Homestay>error(400, "请先完成房东认证");
        }

        Homestay homestay = new Homestay();
        homestay.setLandlordId(landlord.getLandlordId());
        homestay.setTitle((String) data.get("title"));
        homestay.setDescription((String) data.get("description"));
        homestay.setCity((String) data.get("city"));
        homestay.setAddress((String) data.get("address"));
        homestay.setLocation((String) data.get("location"));
        homestay.setHouseType(data.get("houseType") != null ? ((Number) data.get("houseType")).intValue() : 1);
        homestay.setArea(data.get("area") != null ? ((Number) data.get("area")).intValue() : 0);
        homestay.setRoomCount(data.get("roomCount") != null ? ((Number) data.get("roomCount")).intValue() : 1);
        homestay.setBedCount(data.get("bedCount") != null ? ((Number) data.get("bedCount")).intValue() : 1);
        homestay.setMaxGuests(data.get("maxGuests") != null ? ((Number) data.get("maxGuests")).intValue() : 2);
        homestay.setFacilities((String) data.get("facilities"));
        homestay.setPrice(data.get("price") != null ? new java.math.BigDecimal(data.get("price").toString()) : java.math.BigDecimal.ZERO);
        homestay.setBasePrice(data.get("price") != null ? new java.math.BigDecimal(data.get("price").toString()) : java.math.BigDecimal.ZERO);
        homestay.setHolidayPrice(data.get("holidayPrice") != null ? new java.math.BigDecimal(data.get("holidayPrice").toString()) : null);
        homestay.setDepositFee(data.get("depositFee") != null ? ((Number) data.get("depositFee")).intValue() : 0);
        homestay.setHomestayStatus(0);
        homestay.setUdStatus(0);
        homestay.setHomestayUptime(java.time.LocalDateTime.now());

        homestayService.create(homestay);

        @SuppressWarnings("unchecked")
        List<String> imageUrls = (List<String>) data.get("imageUrls");
        if (imageUrls == null || imageUrls.isEmpty()) {
            return Result.<Homestay>error(400, "请至少上传 1 张房源图片");
        }
        homestayImagesService.saveImages(homestay.getHomestayId(), imageUrls);

        return Result.success("房源已提交审核", homestay);
    }

    /**
     * 提交房源审核（正式提交，后端写入图片并锁定）
     */
    @PostMapping("/{id}/submit")
    public Result<Void> submitForAudit(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> data,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null) return Result.<Void>error(401, "未登录");
        String token = authHeader.substring(7);
        Integer landlordId = jwtUtils.getLandlordId(token);

        Homestay homestay = homestayService.findById(id);
        if (homestay == null) {
            return Result.<Void>error(404, "房源不存在");
        }
        if (!homestay.getLandlordId().equals(landlordId)) {
            return Result.<Void>error(403, "无权操作此房源");
        }
        if (homestay.getHomestayStatus() != 0) {
            return Result.<Void>error(400, "该房源已提交审核，请勿重复提交");
        }

        @SuppressWarnings("unchecked")
        List<String> imageUrls = (List<String>) data.get("imageUrls");
        if (imageUrls == null || imageUrls.isEmpty()) {
            return Result.<Void>error(400, "请至少上传 1 张房源图片");
        }

        homestayImagesService.saveImages(id, imageUrls);
        return Result.<Void>success("房源已提交审核，请等待管理员审批", null);
    }

    /**
     * 获取带图片的房源详情（给前端展示用）
     */
    @GetMapping("/detail-with-images/{id}")
    public Result<Map<String, Object>> detailWithImages(@PathVariable Integer id) {
        Homestay homestay = homestayService.findById(id);
        if (homestay == null) {
            return Result.<Map<String, Object>>error(404, "房源不存在");
        }
        List<HomestayImages> images = homestayImagesService.findByHomestayId(id);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("homestay", homestay);
        result.put("images", images);
        return Result.success(result);
    }
}
