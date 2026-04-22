package com.example.housestayspringboot.controller;

import com.example.housestayspringboot.common.Result;
import com.example.housestayspringboot.config.CosConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    private CosConfig cosConfig;

    @PostMapping("/image")
    public Result<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "avatar") String folder) {
        if (file.isEmpty()) {
            return Result.<Map<String, String>>error(400, "请选择图片");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return Result.<Map<String, String>>error(400, "无效的文件");
        }

        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String key = folder + "/" + UUID.randomUUID().toString().replace("-", "") + ext;

        File localFile = null;
        try {
            localFile = File.createTempFile("upload-", ext);
            file.transferTo(localFile);

            BasicCOSCredentials cred = new BasicCOSCredentials(cosConfig.getSecretId(), cosConfig.getSecretKey());
            ClientConfig clientConfig = new ClientConfig(new Region(cosConfig.getRegion()));
            COSClient cosClient = new COSClient(cred, clientConfig);
            try {
                PutObjectRequest putObjectRequest = new PutObjectRequest(
                        cosConfig.getBucket(), key, localFile);
                cosClient.putObject(putObjectRequest);
            } finally {
                cosClient.shutdown();
            }

            String url = cosConfig.getBaseUrl() + "/" + key;
            Map<String, String> data = new HashMap<>();
            data.put("url", url);
            return Result.success(data);

        } catch (IOException e) {
            return Result.<Map<String, String>>error(500, "上传失败: " + e.getMessage());
        } finally {
            if (localFile != null && localFile.exists()) {
                localFile.delete();
            }
        }
    }
}
