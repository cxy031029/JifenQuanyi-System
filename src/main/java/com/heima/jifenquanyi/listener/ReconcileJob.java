package com.heima.jifenquanyi.listener;

import com.heima.jifenquanyi.bill.service.ReconcileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReconcileJob {

    private final ReconcileService reconcileService;

    public ReconcileJob(ReconcileService reconcileService) {
        this.reconcileService = reconcileService;
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void run() {
        try {
            reconcileService.reconcile();
        } catch (Exception e) {
            log.error("日终对账异常", e);
        }
    }
}
