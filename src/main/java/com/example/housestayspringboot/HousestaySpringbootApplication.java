package com.example.housestayspringboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.housestayspringboot.mapper")
public class HousestaySpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(HousestaySpringbootApplication.class, args);
    }

}
