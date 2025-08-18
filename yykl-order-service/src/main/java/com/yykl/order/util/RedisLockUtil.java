package com.yykl.order.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisLockUtil {
    
    private final StringRedisTemplate redisTemplate;
    
    public RedisLockUtil(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String tryLock(String lockKey, long waitMillis) {
        String value = String.valueOf(System.currentTimeMillis());
        try {
            long end = System.currentTimeMillis() + waitMillis;
            while (System.currentTimeMillis() < end) {
                if (redisTemplate.opsForValue().setIfAbsent(lockKey, value, 30, TimeUnit.SECONDS)) {
                    return value;
                }
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return null;
    }

    public void unlock(String lockKey, String lockValue) {
        try {
            String currentValue = redisTemplate.opsForValue().get(lockKey);
            if (lockValue.equals(currentValue)) {
                redisTemplate.delete(lockKey);
            }
        } catch (Exception e) {
            log.error("释放分布式锁异常", e);
        }
    }
}