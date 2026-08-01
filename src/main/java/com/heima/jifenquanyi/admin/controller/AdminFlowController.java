package com.heima.jifenquanyi.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.jifenquanyi.bill.mapper.PointFlowQueryMapper;
import com.heima.jifenquanyi.common.result.PageResult;
import com.heima.jifenquanyi.common.result.R;
import com.heima.jifenquanyi.point.entity.PointFlow;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/flows")
public class AdminFlowController {

    private final PointFlowQueryMapper pointFlowQueryMapper;

    public AdminFlowController(PointFlowQueryMapper pointFlowQueryMapper) {
        this.pointFlowQueryMapper = pointFlowQueryMapper;
    }

    @GetMapping
    public R<PageResult<PointFlow>> page(@RequestParam(defaultValue = "1") long current,
                                         @RequestParam(defaultValue = "10") long size,
                                         @RequestParam(required = false) Long userId,
                                         @RequestParam(required = false) Integer bizType) {
        LambdaQueryWrapper<PointFlow> wrapper = new LambdaQueryWrapper<PointFlow>()
                .eq(userId != null, PointFlow::getUserId, userId)
                .eq(bizType != null, PointFlow::getBizType, bizType)
                .orderByDesc(PointFlow::getCreateTime);
        Page<PointFlow> page = pointFlowQueryMapper.selectPage(new Page<>(current, size), wrapper);
        PageResult<PointFlow> result = new PageResult<>();
        result.setRecords(page.getRecords());
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        return R.ok(result);
    }
}
