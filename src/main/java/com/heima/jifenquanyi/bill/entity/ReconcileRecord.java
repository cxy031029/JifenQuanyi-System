package com.heima.jifenquanyi.bill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_reconcile_record")
public class ReconcileRecord {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String batchNo;
    private Long userId;
    private Integer diff;
    private Integer status;
    private LocalDateTime createTime;
}
