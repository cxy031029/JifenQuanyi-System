package com.heima.jifenquanyi.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_seckill_activity")
public class SeckillActivity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String activityName;
    private Long productId;
    private Integer pointCost;
    private Integer totalStock;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
