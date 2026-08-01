package com.heima.jifenquanyi.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.jifenquanyi.bill.entity.ReconcileRecord;
import com.heima.jifenquanyi.bill.mapper.ReconcileRecordMapper;
import com.heima.jifenquanyi.common.result.R;
import com.heima.jifenquanyi.exchange.mapper.ExchangeRecordMapper;
import com.heima.jifenquanyi.exchange.mapper.ProductMapper;
import com.heima.jifenquanyi.point.entity.PointFlow;
import com.heima.jifenquanyi.point.mapper.PointFlowMapper;
import com.heima.jifenquanyi.seckill.mapper.SeckillOrderMapper;
import com.heima.jifenquanyi.user.entity.User;
import com.heima.jifenquanyi.user.mapper.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/overview")
public class AdminOverviewController {

    private final UserMapper userMapper;
    private final PointFlowMapper pointFlowMapper;
    private final ProductMapper productMapper;
    private final ExchangeRecordMapper exchangeRecordMapper;
    private final SeckillOrderMapper seckillOrderMapper;
    private final ReconcileRecordMapper reconcileRecordMapper;

    public AdminOverviewController(UserMapper userMapper, PointFlowMapper pointFlowMapper,
                                   ProductMapper productMapper, ExchangeRecordMapper exchangeRecordMapper,
                                   SeckillOrderMapper seckillOrderMapper, ReconcileRecordMapper reconcileRecordMapper) {
        this.userMapper = userMapper;
        this.pointFlowMapper = pointFlowMapper;
        this.productMapper = productMapper;
        this.exchangeRecordMapper = exchangeRecordMapper;
        this.seckillOrderMapper = seckillOrderMapper;
        this.reconcileRecordMapper = reconcileRecordMapper;
    }

    @GetMapping
    public R<Map<String, Object>> overview() {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        Map<String, Object> data = new HashMap<>();
        data.put("userTotal", userMapper.selectCount(null));
        data.put("userToday", userMapper.selectCount(
                new LambdaQueryWrapper<User>().ge(User::getCreateTime, today)));
        data.put("flowTotal", pointFlowMapper.selectCount(null));
        data.put("flowToday", pointFlowMapper.selectCount(
                new LambdaQueryWrapper<PointFlow>().ge(PointFlow::getCreateTime, today)));
        data.put("productTotal", productMapper.selectCount(null));
        data.put("exchangeTotal", exchangeRecordMapper.selectCount(null));
        data.put("seckillOrderTotal", seckillOrderMapper.selectCount(null));
        data.put("reconcilePending", reconcileRecordMapper.selectCount(
                new LambdaQueryWrapper<ReconcileRecord>().eq(ReconcileRecord::getStatus, 0)));
        return R.ok(data);
    }
}
