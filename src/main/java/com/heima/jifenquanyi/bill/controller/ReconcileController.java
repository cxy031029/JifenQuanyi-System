package com.heima.jifenquanyi.bill.controller;

import com.heima.jifenquanyi.bill.dto.ReconcileResultVO;
import com.heima.jifenquanyi.bill.service.ReconcileService;
import com.heima.jifenquanyi.common.result.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reconcile")
public class ReconcileController {

    private final ReconcileService reconcileService;

    public ReconcileController(ReconcileService reconcileService) {
        this.reconcileService = reconcileService;
    }

    @PostMapping("/run")
    public R<ReconcileResultVO> run() {
        return R.ok(reconcileService.reconcile());
    }
}
