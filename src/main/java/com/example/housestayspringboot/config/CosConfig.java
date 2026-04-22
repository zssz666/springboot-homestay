package com.example.housestayspringboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cos")
public class CosConfig {
    private String secretId;
    private String secretKey;
    private String bucket;
    private String region;
    private String baseUrl;

    public String getSecretId() { return secretId; }
    public void setSecretId(String secretId) { this.secretId = secretId; }
    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
    public String getBucket() { return bucket; }
    public void setBucket(String bucket) { this.bucket = bucket; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
}
