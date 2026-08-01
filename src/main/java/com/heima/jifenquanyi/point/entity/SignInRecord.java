package com.heima.jifenquanyi.point.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("t_sign_in_record")
public class SignInRecord {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private LocalDate signDate;
    private Integer point;
    private LocalDateTime createTime;
}
