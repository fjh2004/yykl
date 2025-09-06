package com.yykl.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yykl.product.mapper.ProductStockMapper;
import com.yykl.product.mapper.ProductStockLogMapper;
import com.yykl.product.model.dto.StockLockDTO;
import com.yykl.product.model.entity.ProductStock;
import com.yykl.product.model.entity.ProductStockLog;
import com.yykl.product.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StockServiceImpl implements StockService {
    private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ProductStockMapper productStockMapper;

    @Autowired
    private ProductStockLogMapper stockLogMapper;

    // Lua脚本：判断锁是否为当前线程持有，是则删除
    private static final String UNLOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "return redis.call('del', KEYS[1]) " +
            "else " +
            "return 0 " +
            "end";

    @Override
    @Transactional
    public boolean lockStock(StockLockDTO stockLockDTO) {
        if (stockLockDTO.getItems() == null || stockLockDTO.getItems().isEmpty()) {
            return false;
        }

        // 遍历所有商品项进行锁定
        for (StockLockDTO.StockItem item : stockLockDTO.getItems()) {
            String lockKey = "lock:stock:" + item.getProductId();
            String lockValue = Thread.currentThread().getId() + ""; // 使用线程ID作为锁值

            try {
                // 尝试获取分布式锁
                boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, 10, TimeUnit.SECONDS);
                if (!locked) {
                    logger.warn("获取锁失败，商品ID:{}", item.getProductId());
                    return false;
                }

                // 检查库存是否足够
                ProductStock stock = productStockMapper.selectById(item.getProductId());
                if (stock == null || (stock.getQuantity() - stock.getLockedStock()) < item.getQuantity()) {
                    logger.warn("库存不足，商品ID:{}，请求数量:{}", item.getProductId(), item.getQuantity());
                    return false;
                }

                // 锁定库存
                stock.setLockedStock(stock.getLockedStock() + item.getQuantity());
                stock.setUpdateTime(new Date());
                productStockMapper.updateById(stock);

                // 记录库存操作日志
                logStockOperation(item.getProductId(), item.getSkuId(), "LOCK", item.getQuantity(), stockLockDTO.getOrderId());

            } finally {
                // 释放锁
                unlock(lockKey, lockValue);
            }
        }

        // 保存锁定记录到Redis，用于后续解锁和扣减
        redisTemplate.opsForValue().set("stock:lock:" + stockLockDTO.getOrderId(), stockLockDTO, 24, TimeUnit.HOURS);

        return true;
    }

    @Override
    @Transactional
    public boolean deductStock(String orderId) {
        // 根据订单ID获取锁定记录
        List<StockLockDTO> lockRecords = getLockRecordsByOrderId(orderId);
        if (lockRecords.isEmpty()) {
            logger.warn("没有找到订单锁定记录，订单ID:{}", orderId);
            return false;
        }

        for (StockLockDTO lockRecord : lockRecords) {
            if (lockRecord.getItems() == null) continue;

            for (StockLockDTO.StockItem item : lockRecord.getItems()) {
                String lockKey = "lock:stock:" + item.getProductId();
                String lockValue = Thread.currentThread().getId() + "";

                try {
                    // 获取分布式锁
                    boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, 10, TimeUnit.SECONDS);
                    if (!locked) {
                        logger.warn("获取锁失败，商品ID:{}", item.getProductId());
                        return false;
                    }

                    // 扣减库存
                    ProductStock stock = productStockMapper.selectById(item.getProductId());
                    if (stock == null || stock.getLockedStock() < item.getQuantity()) {
                        logger.warn("锁定库存不足，商品ID:{}", item.getProductId());
                        return false;
                    }

                    stock.setQuantity(stock.getQuantity() - item.getQuantity());
                    stock.setLockedStock(stock.getLockedStock() - item.getQuantity());
                    stock.setUpdateTime(new Date());
                    productStockMapper.updateById(stock);

                    // 记录库存操作日志
                    logStockOperation(item.getProductId(), item.getSkuId(), "DEDUCT", item.getQuantity(), orderId);

                } finally {
                    // 释放锁
                    unlock(lockKey, lockValue);
                }
            }
        }

        // 删除Redis中的锁定记录
        redisTemplate.delete("stock:lock:" + orderId);
        return true;
    }

    @Override
    @Transactional
    public boolean unlockStock(String orderId) {
        // 根据订单ID获取锁定记录
        List<StockLockDTO> lockRecords = getLockRecordsByOrderId(orderId);
        if (lockRecords.isEmpty()) {
            logger.warn("没有找到订单锁定记录，订单ID:{}", orderId);
            return false;
        }

        for (StockLockDTO lockRecord : lockRecords) {
            if (lockRecord.getItems() == null) continue;

            for (StockLockDTO.StockItem item : lockRecord.getItems()) {
                String lockKey = "lock:stock:" + item.getProductId();
                String lockValue = Thread.currentThread().getId() + "";

                try {
                    // 获取分布式锁
                    boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, 10, TimeUnit.SECONDS);
                    if (!locked) {
                        logger.warn("获取锁失败，商品ID:{}", item.getProductId());
                        return false;
                    }

                    // 解锁库存
                    ProductStock stock = productStockMapper.selectById(item.getProductId());
                    if (stock == null) {
                        continue;
                    }

                    stock.setLockedStock(stock.getLockedStock() - item.getQuantity());
                    stock.setUpdateTime(new Date());
                    productStockMapper.updateById(stock);

                    // 记录库存操作日志
                    logStockOperation(item.getProductId(), item.getSkuId(), "UNLOCK", item.getQuantity(), orderId);

                } finally {
                    // 释放锁
                    unlock(lockKey, lockValue);
                }
            }
        }

        // 删除Redis中的锁定记录
        redisTemplate.delete("stock:lock:" + orderId);
        return true;
    }

    /**
     * 根据订单ID获取锁定记录
     */
    private List<StockLockDTO> getLockRecordsByOrderId(String orderId) {
        // 从Redis获取锁定记录
        Object obj = redisTemplate.opsForValue().get("stock:lock:" + orderId);
        if (obj != null && obj instanceof StockLockDTO) {
            return Arrays.asList((StockLockDTO) obj);
        }
        return Arrays.asList();
    }

    /**
     * 记录库存操作日志
     */
    private void logStockOperation(Long productId, Long skuId, String operationType, Integer quantity, String orderId) {
        ProductStockLog log = new ProductStockLog();
        log.setProductId(productId);
        log.setSkuId(skuId);
        log.setOperationType(operationType);
        log.setQuantity(quantity);
        log.setOrderId(orderId);
        log.setCreateTime(new Date());
        stockLogMapper.insert(log);
    }

    /**
     * 使用Lua脚本释放锁，确保原子性
     */
    private void unlock(String lockKey, String lockValue) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(UNLOCK_SCRIPT);
        redisScript.setResultType(Long.class);

        // 执行脚本
        redisTemplate.execute(redisScript, Arrays.asList(lockKey), lockValue);
    }
}