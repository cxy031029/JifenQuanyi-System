package com.heima.jifenquanyi.point.controller;

import com.heima.jifenquanyi.common.result.R;
import com.heima.jifenquanyi.common.util.UserContext;
import com.heima.jifenquanyi.point.service.PointService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/point")
public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping("/balance")
    public R<Integer> balance() {
        return R.ok(pointService.balance(UserContext.getUserId()));
    }
}
