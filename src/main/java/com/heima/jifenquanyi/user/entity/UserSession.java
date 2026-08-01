package com.heima.jifenquanyi.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_user_session")
public class UserSession {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String token;
    private LocalDateTime expireTime;
    private LocalDateTime createTime;
}
