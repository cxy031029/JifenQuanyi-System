package com.heima.jifenquanyi.exchange.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_exchange_record")
public class ExchangeRecord {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String exchangeNo;
    private Long userId;
    private Long productId;
    private String productName;
    private Integer pointCost;
    private Integer status;
    private LocalDateTime createTime;
}
