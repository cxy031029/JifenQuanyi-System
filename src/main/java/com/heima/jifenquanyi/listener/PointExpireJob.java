package com.heima.jifenquanyi.listener;

import com.heima.jifenquanyi.point.service.PointExpireService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PointExpireJob {

    private final PointExpireService pointExpireService;

    public PointExpireJob(PointExpireService pointExpireService) {
        this.pointExpireService = pointExpireService;
    }

    @Scheduled(fixedDelay = 300000, initialDelay = 60000)
    public void run() {
        try {
            pointExpireService.recycleExpiredPoints();
        } catch (Exception e) {
            log.error("过期积分定时回收异常", e);
        }
    }
}
