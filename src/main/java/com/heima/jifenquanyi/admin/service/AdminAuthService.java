package com.heima.jifenquanyi.admin.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.jifenquanyi.admin.entity.AdminUser;
import com.heima.jifenquanyi.admin.mapper.AdminUserMapper;
import com.heima.jifenquanyi.common.constants.RedisKeyConstant;
import com.heima.jifenquanyi.common.exception.BizException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class AdminAuthService {

    private final AdminUserMapper adminUserMapper;
    private final StringRedisTemplate stringRedisTemplate;

    public AdminAuthService(AdminUserMapper adminUserMapper, StringRedisTemplate stringRedisTemplate) {
        this.adminUserMapper = adminUserMapper;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public String login(String username, String password) {
        AdminUser admin = adminUserMapper.selectOne(
                new LambdaQueryWrapper<AdminUser>().eq(AdminUser::getUsername, username));
        if (admin == null) {
            throw new BizException("账号或密码错误");
        }
        if (!DigestUtil.md5Hex(password).equals(admin.getPassword())) {
            throw new BizException("账号或密码错误");
        }
        if (admin.getStatus() == null || admin.getStatus() != 1) {
            throw new BizException("账号已禁用");
        }
        String token = IdUtil.simpleUUID();
        stringRedisTemplate.opsForValue()
                .set(RedisKeyConstant.adminSession(token), String.valueOf(admin.getId()), Duration.ofDays(7));
        return token;
    }

    public AdminUser current(String token) {
        String adminId = stringRedisTemplate.opsForValue().get(RedisKeyConstant.adminSession(token));
        if (StrUtil.isBlank(adminId)) {
            throw new BizException(401, "登录已过期");
        }
        AdminUser admin = adminUserMapper.selectById(Long.valueOf(adminId));
        if (admin == null || admin.getStatus() == null || admin.getStatus() != 1) {
            throw new BizException(401, "账号不可用");
        }
        return admin;
    }

    public void logout(String token) {
        if (StrUtil.isNotBlank(token)) {
            stringRedisTemplate.delete(RedisKeyConstant.adminSession(token));
        }
    }
}
