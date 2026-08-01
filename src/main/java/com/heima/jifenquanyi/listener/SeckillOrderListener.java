package com.heima.jifenquanyi.listener;

import com.heima.jifenquanyi.seckill.service.SeckillOrderService;
import lombok.extern.slf4j.Slf4j;
// import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
// import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

// @Slf4j
// @Component
// @RocketMQMessageListener(topic = MQTopicConstant.SECKILL_ORDER_TOPIC, consumerGroup = "seckill-order-consumer")
// public class SeckillOrderListener implements RocketMQListener<String> {
//
//     private final SeckillOrderService seckillOrderService;
//
//     public SeckillOrderListener(SeckillOrderService seckillOrderService) {
//         this.seckillOrderService = seckillOrderService;
//     }
//
//     @Override
//     public void onMessage(String message) {
//         try {
//             seckillOrderService.createOrder(message);
//         } catch (Exception e) {
//             log.error("秒杀订单创建失败", e);
//             throw e;
//         }
//     }
// }
