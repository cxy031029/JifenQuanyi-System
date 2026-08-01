package com.heima.jifenquanyi.point.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_point_account")
public class PointAccount {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Integer totalPoint;
    private Integer frozenPoint;
    private Integer expiredPoint;
    private Integer version;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
