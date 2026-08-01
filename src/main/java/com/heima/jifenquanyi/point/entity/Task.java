package com.heima.jifenquanyi.point.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_task")
public class Task {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String taskCode;
    private String taskName;
    private Integer point;
    private Integer dailyLimit;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
