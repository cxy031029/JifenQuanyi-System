package com.heima.jifenquanyi.exchange.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_product")
public class Product {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String productCode;
    private String name;
    private String cover;
    private Integer pointPrice;
    private Integer stock;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
