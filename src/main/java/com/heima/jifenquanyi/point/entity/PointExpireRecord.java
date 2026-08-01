package com.heima.jifenquanyi.point.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_point_expire_record")
public class PointExpireRecord {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String flowNo;
    private Integer point;
    private LocalDateTime expireTime;
    private Integer status;
    private LocalDateTime createTime;
}
