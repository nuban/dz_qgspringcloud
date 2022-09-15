package com.example.goods;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsSkuDTO {
    private Long id;
    private Long store;
    private String skuNo;
    private String name;
    private String size;
    private String color;
    private BigDecimal price;
}
