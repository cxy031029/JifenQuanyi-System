package com.heima.jifenquanyi.seckill.service;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.heima.jifenquanyi.common.constants.PointStatus;
import com.heima.jifenquanyi.point.service.PointService;
import com.heima.jifenquanyi.seckill.entity.SeckillActivity;
import com.heima.jifenquanyi.seckill.entity.SeckillOrder;
import com.heima.jifenquanyi.seckill.entity.Stock;
import com.heima.jifenquanyi.seckill.mapper.SeckillActivityMapper;
import com.heima.jifenquanyi.seckill.mapper.SeckillOrderMapper;
import com.heima.jifenquanyi.seckill.mapper.StockMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SeckillOrderService {

    private final SeckillOrderMapper seckillOrderMapper;
    private final SeckillActivityMapper seckillActivityMapper;
    private final StockMapper stockMapper;
    private final PointService pointService;

    public SeckillOrderService(SeckillOrderMapper seckillOrderMapper, SeckillActivityMapper seckillActivityMapper,
                               StockMapper stockMapper, PointService pointService) {
        this.seckillOrderMapper = seckillOrderMapper;
        this.seckillActivityMapper = seckillActivityMapper;
        this.stockMapper = stockMapper;
        this.pointService = pointService;
    }

    public void createOrder(String json) {
        JSONObject msg = JSONUtil.parseObj(json);
        Long userId = msg.getLong("userId");
        Long activityId = msg.getLong("activityId");
        String orderNo = msg.getStr("orderNo");
        try {
            SeckillOrder order = new SeckillOrder();
            order.setOrderNo(orderNo);
            order.setUserId(userId);
            order.setActivityId(activityId);
            order.setStatus(1);
            seckillOrderMapper.insert(order);
        } catch (DuplicateKeyException e) {
            log.warn("秒杀订单已存在，忽略重复消费 orderNo={}", orderNo);
            return;
        }
        SeckillActivity activity = seckillActivityMapper.selectById(activityId);
        if (activity == null) {
            return;
        }
        int updated = stockMapper.update(null, new LambdaUpdateWrapper<Stock>()
                .eq(Stock::getBizType, 1)
                .eq(Stock::getBizId, activityId)
                .gt(Stock::getAvailable, 0)
                .setSql("available = available - 1"));
        if (updated == 0) {
            log.warn("DB库存扣减失败 activityId={}", activityId);
            return;
        }
        pointService.subtractPoint(userId, activity.getPointCost(), PointStatus.SECKILL, activityId);
    }
}
