package com.heima.jifenquanyi.admin.interceptor;

import cn.hutool.core.util.StrUtil;
import com.heima.jifenquanyi.common.constants.RedisKeyConstant;
import com.heima.jifenquanyi.common.exception.BizException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)) {
            throw new BizException(401, "未登录");
        }
        String adminId = stringRedisTemplate.opsForValue().get(RedisKeyConstant.adminSession(token));
        if (StrUtil.isBlank(adminId)) {
            throw new BizException(401, "登录已过期");
        }
        request.setAttribute("adminId", Long.valueOf(adminId));
        return true;
    }
}
