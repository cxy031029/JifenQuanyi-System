package com.heima.jifenquanyi.listener;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.heima.jifenquanyi.point.service.PointExpireService;
// import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
// import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

// @Component
// @RocketMQMessageListener(topic = MQTopicConstant.POINT_EXPIRE_TOPIC, consumerGroup = "point-expire-consumer")
// public class PointMqListener implements RocketMQListener<String> {
//
//     private final PointExpireService pointExpireService;
//
//     public PointMqListener(PointExpireService pointExpireService) {
//         this.pointExpireService = pointExpireService;
//     }
//
//     @Override
//     public void onMessage(String message) {
//         JSONObject msg = JSONUtil.parseObj(message);
//         pointExpireService.recycleByFlowNo(msg.getStr("flowNo"));
//     }
// }
