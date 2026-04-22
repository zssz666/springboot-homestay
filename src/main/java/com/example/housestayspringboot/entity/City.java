package com.example.housestayspringboot.entity;

import lombok.Data;

@Data
public class City {
    private Integer code;
    private String name;
    private Integer parentCode;
    private Integer level;
}
