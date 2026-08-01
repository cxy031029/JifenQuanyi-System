package com.heima.jifenquanyi.exchange.dto;

import lombok.Data;

@Data
public class ProductVO {

    private Long id;
    private String productCode;
    private String name;
    private String cover;
    private Integer pointPrice;
    private Integer stock;
}
