package com.example.housestayspringboot.controller;

import com.example.housestayspringboot.common.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/area")
public class AreaController {

    private static final String EXTERNAL_API = "https://cn.apihz.cn/api/other/xzqh.php";
    private static final String API_ID = "10015807";
    private static final String API_KEY = "3ecbcc6289294a5817575acf17b54dde";

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/provinces")
    public Result<Map<String, Object>> getProvinces() {
        String url = EXTERNAL_API + "?id=" + API_ID + "&key=" + API_KEY + "&type=1";
        try {
            String resp = restTemplate.getForObject(url, String.class);
            return parseResult(resp, null);
        } catch (Exception e) {
            return Result.<Map<String, Object>>error(500, "获取省份失败: " + e.getMessage());
        }
    }

    @GetMapping("/cities")
    public Result<Map<String, Object>> getCities(@RequestParam String province) {
        try {
            String url = EXTERNAL_API + "?id=" + API_ID + "&key=" + API_KEY + "&type=2&sheng=" + province;
            System.out.println(url);
            String resp = restTemplate.getForObject(url, String.class);
            return parseResult(resp, province);
        } catch (Exception e) {
            return Result.<Map<String, Object>>error(500, "获取城市失败: " + e.getMessage());
        }
    }

    @GetMapping("/districts")
    public Result<Map<String, Object>> getDistricts(@RequestParam String province, @RequestParam String city) {
        try {
            String url = EXTERNAL_API + "?id=" + API_ID + "&key=" + API_KEY + "&type=3&sheng=" + province + "&shi=" + city;
            String resp = restTemplate.getForObject(url, String.class);
            return parseResult(resp, city);
        } catch (Exception e) {
            return Result.<Map<String, Object>>error(500, "获取区县失败: " + e.getMessage());
        }
    }

    private Result<Map<String, Object>> parseResult(String resp, String parent) {
        if (resp == null || resp.isEmpty()) {
            return Result.<Map<String, Object>>error(500, "接口返回为空");
        }
        com.fasterxml.jackson.databind.JsonNode json;
        try {
            json = new com.fasterxml.jackson.databind.ObjectMapper().readTree(resp);
        } catch (Exception e) {
            return Result.<Map<String, Object>>error(500, "解析返回数据失败");
        }
        Integer code = json.has("code") ? json.get("code").asInt() : null;
        if (code == null || code != 200) {
            String msg = json.has("msg") ? json.get("msg").asText() : "获取地区失败";
            return Result.<Map<String, Object>>error(400, msg);
        }
        String msg = json.has("msg") ? json.get("msg").asText() : "";
        List<Map<String, Object>> list = new ArrayList<>();
        if (msg != null && !msg.isEmpty()) {
            String[] parts = msg.split("\\|");
            for (String name : parts) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", name.trim());
                item.put("parent", parent);
                list.add(item);
            }
        }
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        return Result.success(data);
    }
}
