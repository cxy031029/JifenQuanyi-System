package com.heima.jifenquanyi.point.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("t_task_record")
public class TaskRecord {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long taskId;
    private LocalDate taskDate;
    private Integer point;
    private LocalDateTime createTime;
}
