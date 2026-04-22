package com.example.housestayspringboot.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    @Value("${jwt.secret:HousestaySecretKey2024VeryLongSecretKeyForJWTTokenGeneration}")
    private String secret;

    @Value("${jwt.expiration:86400}")
    private Long expiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Integer userId, String phone, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("phone", phone);
        claims.put("role", role);
        return createToken(claims, userId.toString());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Integer getUserId(String token) {
        Claims claims = parseToken(token);
        Object userId = claims.get("userId");
        if (userId instanceof Integer) {
            return (Integer) userId;
        } else if (userId instanceof Long) {
            return ((Long) userId).intValue();
        } else if (userId instanceof Number) {
            return ((Number) userId).intValue();
        }
        return Integer.valueOf(userId.toString());
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getRole(String token) {
        Claims claims = parseToken(token);
        return claims.get("role", String.class);
    }

    public String generateAdminToken(Integer adminId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("adminId", adminId);
        claims.put("username", username);
        return createToken(claims, adminId.toString());
    }

    public Integer getAdminId(String token) {
        Claims claims = parseToken(token);
        Object adminId = claims.get("adminId");
        if (adminId instanceof Integer) {
            return (Integer) adminId;
        } else if (adminId instanceof Long) {
            return ((Long) adminId).intValue();
        } else if (adminId instanceof Number) {
            return ((Number) adminId).intValue();
        }
        return Integer.valueOf(adminId.toString());
    }
}
