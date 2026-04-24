package com.example.housestayspringboot.filter;

import com.example.housestayspringboot.entity.User;
import com.example.housestayspringboot.service.UserService;
import com.example.housestayspringboot.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtils.validateToken(token)) {
                // 判断 token 类型：优先检查 adminId，再检查 landlordId，最后才是 userId
                Integer adminId = jwtUtils.getAdminIdIfPresent(token);
                Integer landlordId = jwtUtils.getLandlordIdIfPresent(token);
                Integer userId = null;
                String role = null;

                if (adminId != null) {
                    userId = adminId;
                    role = "admin";
                } else if (landlordId != null) {
                    userId = landlordId;
                    role = "landlord";
                } else {
                    userId = jwtUtils.getUserId(token);
                    role = jwtUtils.getRole(token);
                }

                if (userId != null) {
                    User user = userService.findById(userId);
                    if (user != null) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        user,
                                        null,
                                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                                );
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
