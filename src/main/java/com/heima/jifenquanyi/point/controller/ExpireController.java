package com.heima.jifenquanyi.point.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.jifenquanyi.common.result.R;
import com.heima.jifenquanyi.common.util.UserContext;
import com.heima.jifenquanyi.point.entity.PointExpireRecord;
import com.heima.jifenquanyi.point.mapper.PointExpireRecordMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/expire")
public class ExpireController {

    private final PointExpireRecordMapper pointExpireRecordMapper;

    public ExpireController(PointExpireRecordMapper pointExpireRecordMapper) {
        this.pointExpireRecordMapper = pointExpireRecordMapper;
    }

    @GetMapping
    public R<List<PointExpireRecord>> list() {
        Long userId = UserContext.getUserId();
        return R.ok(pointExpireRecordMapper.selectList(
                new LambdaQueryWrapper<PointExpireRecord>()
                        .eq(PointExpireRecord::getUserId, userId)
                        .orderByDesc(PointExpireRecord::getCreateTime)));
    }
}
