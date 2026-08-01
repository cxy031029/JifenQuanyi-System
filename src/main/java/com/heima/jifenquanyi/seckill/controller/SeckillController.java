package com.heima.jifenquanyi.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.jifenquanyi.common.result.R;
import com.heima.jifenquanyi.seckill.dto.SeckillResultVO;
import com.heima.jifenquanyi.seckill.entity.SeckillActivity;
import com.heima.jifenquanyi.seckill.mapper.SeckillActivityMapper;
import com.heima.jifenquanyi.seckill.service.SeckillService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/seckill")
public class SeckillController {

    private final SeckillService seckillService;
    private final SeckillActivityMapper seckillActivityMapper;

    public SeckillController(SeckillService seckillService, SeckillActivityMapper seckillActivityMapper) {
        this.seckillService = seckillService;
        this.seckillActivityMapper = seckillActivityMapper;
    }

    @GetMapping("/activities")
    public R<List<SeckillActivity>> activities() {
        return R.ok(seckillActivityMapper.selectList(new LambdaQueryWrapper<SeckillActivity>()
                .le(SeckillActivity::getStartTime, LocalDateTime.now())
                .ge(SeckillActivity::getEndTime, LocalDateTime.now())));
    }

    @PostMapping("/{activityId}")
    public R<SeckillResultVO> seckill(@PathVariable Long activityId) {
        return R.ok(seckillService.seckill(activityId));
    }
}
