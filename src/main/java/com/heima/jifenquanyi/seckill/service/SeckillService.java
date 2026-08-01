package com.heima.jifenquanyi.seckill.service;

import cn.hutool.json.JSONUtil;
import com.heima.jifenquanyi.common.constants.MQTopicConstant;
import com.heima.jifenquanyi.common.constants.RedisKeyConstant;
import com.heima.jifenquanyi.common.exception.BizException;
import com.heima.jifenquanyi.common.util.OrderNoGenerator;
import com.heima.jifenquanyi.common.util.UserContext;
import com.heima.jifenquanyi.seckill.dto.SeckillResultVO;
// import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SeckillService {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;
    private final DefaultRedisScript<Long> stockDecScript;
    // private final RocketMQTemplate rocketMQTemplate;

    public SeckillService(StringRedisTemplate stringRedisTemplate, RedissonClient redissonClient,
                          DefaultRedisScript<Long> stockDecScript) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redissonClient = redissonClient;
        this.stockDecScript = stockDecScript;
    }

    public SeckillResultVO seckill(Long activityId) {
        Long userId = UserContext.getUserId();
        String soldOutFlag = stringRedisTemplate.opsForValue().get(RedisKeyConstant.soldOut(activityId));
        if (soldOutFlag != null) {
            throw new BizException("手慢了，已售罄");
        }
        RLock lock = redissonClient.getLock(RedisKeyConstant.seckillUser(activityId, userId));
        boolean locked;
        try {
            locked = lock.tryLock(0, 30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BizException("系统繁忙，请稍后再试");
        }
        if (!locked) {
            throw new BizException("请勿重复提交");
        }
        try {
            Long stock = stringRedisTemplate.execute(
                    stockDecScript, List.of(RedisKeyConstant.stock(activityId)));
            if (stock == null || stock < 0) {
                stringRedisTemplate.opsForValue()
                        .set(RedisKeyConstant.soldOut(activityId), "1", Duration.ofHours(2));
                throw new BizException("手慢了，已售罄");
            }
            Map<String, Object> msg = new HashMap<>();
            msg.put("userId", userId);
            msg.put("activityId", activityId);
            msg.put("orderNo", OrderNoGenerator.next("SO"));
            // rocketMQTemplate.syncSend(MQTopicConstant.SECKILL_ORDER_TOPIC + ":" + MQTopicConstant.TAG_SECKILL,
            //         JSONUtil.toJsonStr(msg));
            SeckillResultVO vo = new SeckillResultVO();
            vo.setOrderNo((String) msg.get("orderNo"));
            vo.setStatus(1);
            vo.setMsg("抢购成功，等待出单");
            return vo;
        } finally {
            lock.unlock();
        }
    }
}
