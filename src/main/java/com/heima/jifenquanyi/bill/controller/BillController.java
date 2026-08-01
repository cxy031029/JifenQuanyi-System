package com.heima.jifenquanyi.bill.controller;

import com.heima.jifenquanyi.bill.dto.FlowPageDTO;
import com.heima.jifenquanyi.bill.service.BillService;
import com.heima.jifenquanyi.common.result.PageResult;
import com.heima.jifenquanyi.common.result.R;
import com.heima.jifenquanyi.point.entity.PointFlow;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bill")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping("/flows")
    public R<PageResult<PointFlow>> flows(FlowPageDTO dto) {
        return R.ok(billService.flows(dto));
    }
}
