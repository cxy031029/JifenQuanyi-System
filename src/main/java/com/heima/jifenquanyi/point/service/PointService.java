package com.heima.jifenquanyi.point.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.heima.jifenquanyi.common.constants.MQTopicConstant;
import com.heima.jifenquanyi.common.constants.RedisKeyConstant;
import com.heima.jifenquanyi.common.exception.BizException;
import com.heima.jifenquanyi.common.util.OrderNoGenerator;
import com.heima.jifenquanyi.point.entity.PointAccount;
import com.heima.jifenquanyi.point.entity.PointFlow;
import com.heima.jifenquanyi.point.mapper.PointAccountMapper;
import com.heima.jifenquanyi.point.mapper.PointFlowMapper;
// import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
// import org.springframework.messaging.Message;
// import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class PointService {

    private final PointAccountMapper pointAccountMapper;
    private final PointFlowMapper pointFlowMapper;
    private final StringRedisTemplate stringRedisTemplate;
    // private final RocketMQTemplate rocketMQTemplate;

    public PointService(PointAccountMapper pointAccountMapper, PointFlowMapper pointFlowMapper,
                        StringRedisTemplate stringRedisTemplate) {
        this.pointAccountMapper = pointAccountMapper;
        this.pointFlowMapper = pointFlowMapper;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addPoint(Long userId, int point, int bizType, Long sourceId, LocalDateTime expireTime) {
        changePoint(userId, point, bizType, sourceId, expireTime);
    }

    @Transactional(rollbackFor = Exception.class)
    public void subtractPoint(Long userId, int point, int bizType, Long sourceId) {
        changePoint(userId, -point, bizType, sourceId, null);
    }

    public Integer balance(Long userId) {
        String key = RedisKeyConstant.pointBalance(userId);
        String cached = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(cached)) {
            return Integer.valueOf(cached);
        }
        PointAccount account = pointAccountMapper.selectOne(
                new LambdaQueryWrapper<PointAccount>().eq(PointAccount::getUserId, userId));
        if (account == null) {
            return 0;
        }
        stringRedisTemplate.opsForValue().set(key, String.valueOf(account.getTotalPoint()), Duration.ofMinutes(10));
        return account.getTotalPoint();
    }

    private void changePoint(Long userId, int delta, int bizType, Long sourceId, LocalDateTime expireTime) {
        PointAccount account = pointAccountMapper.selectOne(
                new LambdaQueryWrapper<PointAccount>().eq(PointAccount::getUserId, userId));
        if (account == null) {
            throw new BizException("积分账户不存在");
        }
        int newBalance = account.getTotalPoint() + delta;
        if (newBalance < 0) {
            throw new BizException("积分不足");
        }
        String sql = delta >= 0
                ? "total_point = total_point + " + delta + ", version = version + 1"
                : "total_point = total_point - " + (-delta) + ", version = version + 1";
        int updated = pointAccountMapper.update(null, new LambdaUpdateWrapper<PointAccount>()
                .eq(PointAccount::getId, account.getId())
                .eq(PointAccount::getVersion, account.getVersion())
                .setSql(sql));
        if (updated == 0) {
            throw new BizException("操作冲突，请重试");
        }

        PointFlow flow = new PointFlow();
        flow.setFlowNo(OrderNoGenerator.next("F"));
        flow.setUserId(userId);
        flow.setBizType(bizType);
        flow.setChangePoint(delta);
        flow.setBalanceAfter(newBalance);
        flow.setSourceType(1);
        flow.setSourceId(sourceId);
        flow.setExpireTime(expireTime);
        flow.setStatus(1);
        pointFlowMapper.insert(flow);

        stringRedisTemplate.delete(RedisKeyConstant.pointBalance(userId));

        // if (expireTime != null && delta > 0) {
        //     sendExpireMsg(flow);
        // }
    }

    // private void sendExpireMsg(PointFlow flow) {
    //     long delaySeconds = Duration.between(LocalDateTime.now(), flow.getExpireTime()).getSeconds();
    //     if (delaySeconds <= 0) {
    //         return;
    //     }
    //     Map<String, Object> payload = new HashMap<>();
    //     payload.put("flowNo", flow.getFlowNo());
    //     payload.put("userId", flow.getUserId());
    //     payload.put("point", flow.getChangePoint());
    //     Message<String> message = MessageBuilder.withPayload(JSONUtil.toJsonStr(payload)).build();
    //     int delay = delaySeconds > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) delaySeconds;
    //     rocketMQTemplate.syncSendDelayTimeSeconds(
    //             MQTopicConstant.POINT_EXPIRE_TOPIC + ":" + MQTopicConstant.TAG_EXPIRE, message, delay);
    // }
}
