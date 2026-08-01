package com.heima.jifenquanyi.bill.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.jifenquanyi.bill.dto.ReconcileResultVO;
import com.heima.jifenquanyi.bill.entity.ReconcileRecord;
import com.heima.jifenquanyi.bill.mapper.ReconcileRecordMapper;
import com.heima.jifenquanyi.common.constants.RedisKeyConstant;
import com.heima.jifenquanyi.common.exception.BizException;
import com.heima.jifenquanyi.common.util.OrderNoGenerator;
import com.heima.jifenquanyi.point.entity.PointAccount;
import com.heima.jifenquanyi.point.mapper.PointAccountMapper;
import com.heima.jifenquanyi.point.mapper.PointFlowMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ReconcileService {

    private final PointAccountMapper pointAccountMapper;
    private final PointFlowMapper pointFlowMapper;
    private final ReconcileRecordMapper reconcileRecordMapper;
    private final RedissonClient redissonClient;

    public ReconcileService(PointAccountMapper pointAccountMapper, PointFlowMapper pointFlowMapper,
                            ReconcileRecordMapper reconcileRecordMapper, RedissonClient redissonClient) {
        this.pointAccountMapper = pointAccountMapper;
        this.pointFlowMapper = pointFlowMapper;
        this.reconcileRecordMapper = reconcileRecordMapper;
        this.redissonClient = redissonClient;
    }

    public ReconcileResultVO reconcile() {
        RLock lock = redissonClient.getLock(RedisKeyConstant.POINT_EXPIRE_RUN_LOCK);
        boolean locked;
        try {
            locked = lock.tryLock(0, 10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BizException("获取对账锁失败");
        }
        if (!locked) {
            throw new BizException("对账任务正在执行中");
        }
        try {
            String batchNo = OrderNoGenerator.next("RC");
            int diffCount = 0;
            List<PointAccount> accounts = pointAccountMapper.selectList(null);
            for (PointAccount account : accounts) {
                Integer in = pointFlowMapper.sumIn(account.getUserId());
                Integer out = pointFlowMapper.sumOut(account.getUserId());
                Integer expired = pointFlowMapper.sumExpired(account.getUserId());
                int expected = in - out - expired;
                if (account.getTotalPoint() != expected) {
                    ReconcileRecord record = new ReconcileRecord();
                    record.setBatchNo(batchNo);
                    record.setUserId(account.getUserId());
                    record.setDiff(account.getTotalPoint() - expected);
                    record.setStatus(0);
                    reconcileRecordMapper.insert(record);
                    diffCount++;
                }
            }
            ReconcileResultVO vo = new ReconcileResultVO();
            vo.setBatchNo(batchNo);
            vo.setDiffCount(diffCount);
            return vo;
        } finally {
            lock.unlock();
        }
    }
}
