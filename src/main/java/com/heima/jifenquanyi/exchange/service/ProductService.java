package com.heima.jifenquanyi.exchange.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.jifenquanyi.common.constants.RedisKeyConstant;
import com.heima.jifenquanyi.common.exception.BizException;
import com.heima.jifenquanyi.common.result.PageResult;
import com.heima.jifenquanyi.exchange.entity.Product;
import com.heima.jifenquanyi.exchange.mapper.ProductMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ProductService {

    private final ProductMapper productMapper;
    private final StringRedisTemplate stringRedisTemplate;

    public ProductService(ProductMapper productMapper, StringRedisTemplate stringRedisTemplate) {
        this.productMapper = productMapper;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public PageResult<Product> list(int current, int size) {
        Page<Product> page = productMapper.selectPage(new Page<>(current, size),
                new LambdaQueryWrapper<Product>().eq(Product::getStatus, 1));
        PageResult<Product> result = new PageResult<>();
        result.setRecords(page.getRecords());
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        return result;
    }

    public Product detail(Long id) {
        String key = RedisKeyConstant.product(id);
        String cached = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(cached)) {
            return JSONUtil.toBean(cached, Product.class);
        }
        Product product = productMapper.selectById(id);
        if (product == null || product.getStatus() == null || product.getStatus() != 1) {
            stringRedisTemplate.opsForValue().set(key, "null", Duration.ofMinutes(5));
            throw new BizException("商品不存在");
        }
        long ttl = 600 + ThreadLocalRandom.current().nextLong(300);
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(product), Duration.ofSeconds(ttl));
        return product;
    }
}
