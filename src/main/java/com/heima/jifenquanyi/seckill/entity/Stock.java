package com.heima.jifenquanyi.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_stock")
public class Stock {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer bizType;
    private Long bizId;
    private Integer total;
    private Integer available;
    private Integer version;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
