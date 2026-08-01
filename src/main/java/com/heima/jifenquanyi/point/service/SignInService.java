package com.heima.jifenquanyi.point.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.jifenquanyi.common.constants.PointStatus;
import com.heima.jifenquanyi.common.constants.RedisKeyConstant;
import com.heima.jifenquanyi.common.exception.BizException;
import com.heima.jifenquanyi.common.util.UserContext;
import com.heima.jifenquanyi.point.dto.SignInVO;
import com.heima.jifenquanyi.point.entity.SignInRecord;
import com.heima.jifenquanyi.point.mapper.SignInRecordMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SignInService {

    private static final int SIGN_POINT = 5;

    private final SignInRecordMapper signInRecordMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;
    private final PointService pointService;
    private final DefaultRedisScript<Long> signScript;

    public SignInService(SignInRecordMapper signInRecordMapper, StringRedisTemplate stringRedisTemplate,
                         RedissonClient redissonClient, PointService pointService,
                         DefaultRedisScript<Long> signScript) {
        this.signInRecordMapper = signInRecordMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redissonClient = redissonClient;
        this.pointService = pointService;
        this.signScript = signScript;
    }

    public SignInVO signIn() {
        Long userId = UserContext.getUserId();
        LocalDate today = LocalDate.now();
        Long first = stringRedisTemplate.execute(
                signScript, List.of(RedisKeyConstant.sign(userId, today)), "172800");
        if (first == null || first == 0) {
            throw new BizException("今日已签到");
        }
        RLock lock = redissonClient.getLock(RedisKeyConstant.signLock(userId, today));
        try {
            lock.lock();
            SignInRecord record = new SignInRecord();
            record.setUserId(userId);
            record.setSignDate(today);
            record.setPoint(SIGN_POINT);
            try {
                signInRecordMapper.insert(record);
            } catch (DuplicateKeyException e) {
                throw new BizException("今日已签到");
            }
            pointService.addPoint(userId, SIGN_POINT, PointStatus.SIGN, record.getId(),
                    LocalDateTime.now().plusYears(1));
            return todayVO();
        } finally {
            lock.unlock();
        }
    }

    public SignInVO today() {
        return todayVO();
    }

    private SignInVO todayVO() {
        Long userId = UserContext.getUserId();
        Boolean signed = stringRedisTemplate.hasKey(RedisKeyConstant.sign(userId, LocalDate.now()));
        Long count = signInRecordMapper.selectCount(
                new LambdaQueryWrapper<SignInRecord>().eq(SignInRecord::getUserId, userId));
        SignInVO vo = new SignInVO();
        vo.setTodaySigned(Boolean.TRUE.equals(signed));
        vo.setContinueDays(count == null ? 0 : count.intValue());
        vo.setPoint(pointService.balance(userId));
        return vo;
    }
}
