package com.heima.jifenquanyi.exchange.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.heima.jifenquanyi.common.constants.PointStatus;
import com.heima.jifenquanyi.common.constants.RedisKeyConstant;
import com.heima.jifenquanyi.common.exception.BizException;
import com.heima.jifenquanyi.common.util.OrderNoGenerator;
import com.heima.jifenquanyi.common.util.UserContext;
import com.heima.jifenquanyi.exchange.dto.ExchangeDTO;
import com.heima.jifenquanyi.exchange.entity.ExchangeRecord;
import com.heima.jifenquanyi.exchange.entity.Product;
import com.heima.jifenquanyi.exchange.mapper.ExchangeRecordMapper;
import com.heima.jifenquanyi.exchange.mapper.ProductMapper;
import com.heima.jifenquanyi.point.service.PointService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class ExchangeService {

    private final ProductMapper productMapper;
    private final ExchangeRecordMapper exchangeRecordMapper;
    private final RedissonClient redissonClient;
    private final PointService pointService;

    public ExchangeService(ProductMapper productMapper, ExchangeRecordMapper exchangeRecordMapper,
                           RedissonClient redissonClient, PointService pointService) {
        this.productMapper = productMapper;
        this.exchangeRecordMapper = exchangeRecordMapper;
        this.redissonClient = redissonClient;
        this.pointService = pointService;
    }

    public void exchange(ExchangeDTO dto) {
        Long userId = UserContext.getUserId();
        RLock lock = redissonClient.getLock(RedisKeyConstant.pointAccountLock(userId));
        try {
            lock.lock();
            Product product = productMapper.selectById(dto.getProductId());
            if (product == null || product.getStatus() == null || product.getStatus() != 1) {
                throw new BizException("商品不可兑换");
            }
            int count = dto.getQuantity() == null ? 1 : dto.getQuantity();
            if (product.getStock() < count) {
                throw new BizException("库存不足");
            }
            pointService.subtractPoint(userId, product.getPointPrice() * count, PointStatus.EXCHANGE, product.getId());
            int updated = productMapper.update(null, new LambdaUpdateWrapper<Product>()
                    .eq(Product::getId, product.getId())
                    .ge(Product::getStock, count)
                    .setSql("stock = stock - " + count));
            if (updated == 0) {
                throw new BizException("库存不足");
            }
            ExchangeRecord record = new ExchangeRecord();
            record.setExchangeNo(OrderNoGenerator.next("E"));
            record.setUserId(userId);
            record.setProductId(product.getId());
            record.setProductName(product.getName());
            record.setPointCost(product.getPointPrice() * count);
            record.setStatus(1);
            exchangeRecordMapper.insert(record);
        } finally {
            lock.unlock();
        }
    }

    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<ExchangeRecord> records(int current, int size) {
        Long userId = UserContext.getUserId();
        return exchangeRecordMapper.selectPage(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, size),
                new LambdaQueryWrapper<ExchangeRecord>()
                        .eq(ExchangeRecord::getUserId, userId)
                        .orderByDesc(ExchangeRecord::getCreateTime));
    }
}
