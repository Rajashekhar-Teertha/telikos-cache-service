package com.demo.redis.patterns.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class ProductDto implements Serializable {

    private Long id;
    private String description;
    private Double price;


}
