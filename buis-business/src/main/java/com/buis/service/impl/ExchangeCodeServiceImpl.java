package com.buis.service.impl;

import com.buis.common.utils.coding.CodeUtil;
import com.buis.service.IExchangeCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ExchangeCodeServiceImpl implements IExchangeCodeService {
    private static final String CODE_SERIAL_KEY = "team:code:serial";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 生成编码
     *
     * @param teamId
     * @return
     */
    @Override
    public String generateCode(long teamId) {
        // 1. 获取全局唯一序列号（Redis原子操作）
        Long serialNum = redisTemplate.opsForValue().increment(CODE_SERIAL_KEY);
        if (serialNum == null) {
            throw new RuntimeException("获取序列号失败");
        }

        // 2. 生成兑换码（自动截取序列号低28位）
        String code = CodeUtil.generateCode(serialNum, teamId & 0xF);

        return code;
    }
}
