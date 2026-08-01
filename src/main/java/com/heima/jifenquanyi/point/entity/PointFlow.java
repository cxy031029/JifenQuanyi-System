package com.heima.jifenquanyi.point.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_point_flow")
public class PointFlow {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String flowNo;
    private Long userId;
    private Integer bizType;
    private Integer changePoint;
    private Integer balanceAfter;
    private Integer sourceType;
    private Long sourceId;
    private LocalDateTime expireTime;
    private Integer status;
    private LocalDateTime createTime;
}
