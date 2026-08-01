package com.heima.jifenquanyi.bill.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.jifenquanyi.bill.dto.FlowPageDTO;
import com.heima.jifenquanyi.bill.mapper.PointFlowQueryMapper;
import com.heima.jifenquanyi.common.result.PageResult;
import com.heima.jifenquanyi.common.util.UserContext;
import com.heima.jifenquanyi.point.entity.PointFlow;
import org.springframework.stereotype.Service;

@Service
public class BillService {

    private final PointFlowQueryMapper pointFlowQueryMapper;

    public BillService(PointFlowQueryMapper pointFlowQueryMapper) {
        this.pointFlowQueryMapper = pointFlowQueryMapper;
    }

    public PageResult<PointFlow> flows(FlowPageDTO dto) {
        Long userId = UserContext.getUserId();
        LambdaQueryWrapper<PointFlow> wrapper = new LambdaQueryWrapper<PointFlow>()
                .eq(PointFlow::getUserId, userId)
                .orderByDesc(PointFlow::getCreateTime);
        if (dto.getBizType() != null) {
            wrapper.eq(PointFlow::getBizType, dto.getBizType());
        }
        Page<PointFlow> page = pointFlowQueryMapper.selectPage(
                new Page<>(dto.getCurrent(), dto.getSize()), wrapper);
        PageResult<PointFlow> result = new PageResult<>();
        result.setRecords(page.getRecords());
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        return result;
    }
}
