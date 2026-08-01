package com.heima.jifenquanyi.user.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.heima.jifenquanyi.common.constants.RedisKeyConstant;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class SessionService {

    private final StringRedisTemplate stringRedisTemplate;

    public SessionService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public String createSession(Long userId) {
        String token = IdUtil.simpleUUID();
        stringRedisTemplate.opsForValue().set(RedisKeyConstant.session(token), String.valueOf(userId), Duration.ofDays(7));
        return token;
    }

    public void destroy(String token) {
        if (StrUtil.isNotBlank(token)) {
            stringRedisTemplate.delete(RedisKeyConstant.session(token));
        }
    }
}
