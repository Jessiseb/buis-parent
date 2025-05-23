package com.buis.common.manager;


import com.buis.common.exception.ServiceException;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 专门提供 RedisLimiter 限流基础服务的（提供了通用的能力）
 */
@Service
public class RedisLimiterManager {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 限流操作
     *
     * @param key 区分不同的限流器，比如不同的用户 id 应该分别统计
     */
    public void doRateLimit(String key) {
        if (redissonClient == null) {
            throw new ServiceException("Redisson 客户端未初始化");
        }
        if (key == null || key.isEmpty()) {
            throw new ServiceException("限流 key 不可为空");
        }
        try {
            RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
            rateLimiter.trySetRate(RateType.OVERALL, 2, 1, RateIntervalUnit.SECONDS);
            boolean canOp = rateLimiter.tryAcquire(1);
            if (!canOp) {
                throw new ServiceException("操作过于频繁，请稍后再试");
            }
        } catch (Exception e) {
            throw new ServiceException("限流操作失败: " + e.getMessage());
        }
    }
}
