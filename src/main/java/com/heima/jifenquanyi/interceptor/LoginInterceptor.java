package com.heima.jifenquanyi.interceptor;

import cn.hutool.core.util.StrUtil;
import com.heima.jifenquanyi.common.constants.RedisKeyConstant;
import com.heima.jifenquanyi.common.exception.BizException;
import com.heima.jifenquanyi.common.util.UserContext;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)) {
            throw new BizException(401, "未登录");
        }
        String userId = stringRedisTemplate.opsForValue().get(RedisKeyConstant.session(token));
        if (StrUtil.isBlank(userId)) {
            throw new BizException(401, "登录已过期");
        }
        stringRedisTemplate.expire(RedisKeyConstant.session(token), Duration.ofDays(7));
        UserContext.setUserId(Long.valueOf(userId));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
}
