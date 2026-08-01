package com.heima.jifenquanyi.seckill.service;

import com.heima.jifenquanyi.common.constants.RedisKeyConstant;
import com.heima.jifenquanyi.common.exception.BizException;
import com.heima.jifenquanyi.seckill.entity.SeckillActivity;
import com.heima.jifenquanyi.seckill.mapper.SeckillActivityMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SeckillStockService {

    private final SeckillActivityMapper seckillActivityMapper;
    private final StringRedisTemplate stringRedisTemplate;

    public SeckillStockService(SeckillActivityMapper seckillActivityMapper, StringRedisTemplate stringRedisTemplate) {
        this.seckillActivityMapper = seckillActivityMapper;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void prepareStock(Long activityId) {
        SeckillActivity activity = seckillActivityMapper.selectById(activityId);
        if (activity == null) {
            throw new BizException("活动不存在");
        }
        stringRedisTemplate.opsForValue()
                .set(RedisKeyConstant.stock(activityId), String.valueOf(activity.getTotalStock()));
    }

    public void restoreStock(Long activityId, int count) {
        stringRedisTemplate.opsForValue().increment(RedisKeyConstant.stock(activityId), count);
    }
}
