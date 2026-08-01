package com.heima.jifenquanyi.point.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.heima.jifenquanyi.common.constants.RedisKeyConstant;
import com.heima.jifenquanyi.common.exception.BizException;
import com.heima.jifenquanyi.point.entity.PointAccount;
import com.heima.jifenquanyi.point.entity.PointExpireRecord;
import com.heima.jifenquanyi.point.entity.PointFlow;
import com.heima.jifenquanyi.point.mapper.PointAccountMapper;
import com.heima.jifenquanyi.point.mapper.PointExpireRecordMapper;
import com.heima.jifenquanyi.point.mapper.PointFlowMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class PointExpireService {

    private final PointFlowMapper pointFlowMapper;
    private final PointExpireRecordMapper pointExpireRecordMapper;
    private final PointAccountMapper pointAccountMapper;
    private final RedissonClient redissonClient;

    public PointExpireService(PointFlowMapper pointFlowMapper, PointExpireRecordMapper pointExpireRecordMapper,
                              PointAccountMapper pointAccountMapper, RedissonClient redissonClient) {
        this.pointFlowMapper = pointFlowMapper;
        this.pointExpireRecordMapper = pointExpireRecordMapper;
        this.pointAccountMapper = pointAccountMapper;
        this.redissonClient = redissonClient;
    }

    public void recycleExpiredPoints() {
        RLock lock = redissonClient.getLock(RedisKeyConstant.POINT_EXPIRE_RUN_LOCK);
        boolean locked;
        try {
            locked = lock.tryLock(0, 5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        if (!locked) {
            return;
        }
        try {
            List<PointFlow> expired = pointFlowMapper.selectList(
                    new LambdaQueryWrapper<PointFlow>()
                            .eq(PointFlow::getStatus, 1)
                            .isNotNull(PointFlow::getExpireTime)
                            .lt(PointFlow::getExpireTime, LocalDateTime.now())
                            .last("LIMIT 500"));
            for (PointFlow flow : expired) {
                try {
                    recycleOne(flow);
                } catch (Exception e) {
                    log.warn("积分过期回收失败 flowNo={}", flow.getFlowNo(), e);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void recycleByFlowNo(String flowNo) {
        PointFlow flow = pointFlowMapper.selectOne(
                new LambdaQueryWrapper<PointFlow>().eq(PointFlow::getFlowNo, flowNo));
        if (flow == null) {
            return;
        }
        recycleOne(flow);
    }

    private void recycleOne(PointFlow flow) {
        if (flow.getStatus() == null || flow.getStatus() != 1 || flow.getChangePoint() == null || flow.getChangePoint() <= 0) {
            return;
        }
        RLock lock = redissonClient.getLock(RedisKeyConstant.pointAccountLock(flow.getUserId()));
        try {
            lock.lock();
            PointFlow latest = pointFlowMapper.selectById(flow.getId());
            if (latest.getStatus() == null || latest.getStatus() != 1) {
                return;
            }
            PointAccount account = pointAccountMapper.selectOne(
                    new LambdaQueryWrapper<PointAccount>().eq(PointAccount::getUserId, flow.getUserId()));
            if (account == null) {
                return;
            }
            int updated = pointAccountMapper.update(null, new LambdaUpdateWrapper<PointAccount>()
                    .eq(PointAccount::getId, account.getId())
                    .eq(PointAccount::getVersion, account.getVersion())
                    .setSql("total_point = total_point - " + flow.getChangePoint()
                            + ", expired_point = expired_point + " + flow.getChangePoint()
                            + ", version = version + 1"));
            if (updated == 0) {
                throw new BizException("过期回收更新失败");
            }
            PointExpireRecord record = new PointExpireRecord();
            record.setUserId(flow.getUserId());
            record.setFlowNo(flow.getFlowNo());
            record.setPoint(flow.getChangePoint());
            record.setExpireTime(flow.getExpireTime());
            record.setStatus(1);
            pointExpireRecordMapper.insert(record);
            pointFlowMapper.update(null, new LambdaUpdateWrapper<PointFlow>()
                    .eq(PointFlow::getId, flow.getId())
                    .set(PointFlow::getStatus, 2));
        } finally {
            lock.unlock();
        }
    }
}
