package com.buis.service;
import com.buis.common.core.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存服务
 */
@Service
public class RedisCacheService {
    @Autowired
    private RedisCache redisCache;

    // 缓存键前缀
    private static final String USER_SKILLS_PREFIX = "user:skills:";
    private static final String TEAM_SKILLS_PREFIX = "team:skills:";
    private static final String TEAM_INFO_PREFIX = "team:info:";
    private static final String USER_INFO_PREFIX = "user:info:";

    // 缓存过期时间（24小时）
    private static final long CACHE_EXPIRE_TIME = 24;

    /**
     * 缓存用户技能
     *
     * @param userId 用户ID
     * @param skills 用户技能列表
     */
    public void cacheUserSkills(Long userId, List<?> skills) {
        String key = USER_SKILLS_PREFIX + userId;
        redisCache.setCacheObject(key, skills, (int) CACHE_EXPIRE_TIME, TimeUnit.HOURS);
    }

    /**
     * 缓存队伍技能需求
     *
     * @param teamId 队伍ID
     * @param skills 队伍技能需求列表
     */
    public void cacheTeamSkills(Long teamId, List<?> skills) {
        String key = TEAM_SKILLS_PREFIX + teamId;
        redisCache.setCacheObject(key, skills, (int) CACHE_EXPIRE_TIME, TimeUnit.HOURS);
    }

    /**
     * 缓存队伍信息
     *
     * @param teamId 队伍ID
     * @param teamInfo 队伍信息
     */
    public void cacheTeamInfo(Long teamId, Object teamInfo) {
        String key = TEAM_INFO_PREFIX + teamId;
        redisCache.setCacheObject(key, teamInfo, (int) CACHE_EXPIRE_TIME, TimeUnit.HOURS);
    }

    /**
     * 缓存用户信息
     *
     * @param userId 用户ID
     * @param userInfo 用户信息
     */
    public void cacheUserInfo(Long userId, Object userInfo) {
        String key = USER_INFO_PREFIX + userId;
        redisCache.setCacheObject(key, userInfo, (int) CACHE_EXPIRE_TIME, TimeUnit.HOURS);
    }

    /**
     * 获取缓存的用户技能
     *
     * @param userId 用户ID
     * @return 用户技能列表
     */
    @SuppressWarnings("unchecked")
    public List<?> getUserSkills(Long userId) {
        String key = USER_SKILLS_PREFIX + userId;
        return redisCache.getCacheObject(key);
    }

    /**
     * 获取缓存的队伍技能需求
     *
     * @param teamId 队伍ID
     * @return 队伍技能需求列表
     */
    @SuppressWarnings("unchecked")
    public List<?> getTeamSkills(Long teamId) {
        String key = TEAM_SKILLS_PREFIX + teamId;
        return redisCache.getCacheObject(key);
    }

    /**
     * 获取缓存的队伍信息
     *
     * @param teamId 队伍ID
     * @return 队伍信息
     */
    public Object getTeamInfo(Long teamId) {
        String key = TEAM_INFO_PREFIX + teamId;
        return redisCache.getCacheObject(key);
    }

    /**
     * 获取缓存的用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    public Object getUserInfo(Long userId) {
        String key = USER_INFO_PREFIX + userId;
        return redisCache.getCacheObject(key);
    }

    /**
     * 删除用户技能缓存
     *
     * @param userId 用户ID
     */
    public void deleteUserSkillsCache(Long userId) {
        String key = USER_SKILLS_PREFIX + userId;
        redisCache.deleteObject(key);
    }

    /**
     * 删除队伍技能需求缓存
     *
     * @param teamId 队伍ID
     */
    public void deleteTeamSkillsCache(Long teamId) {
        String key = TEAM_SKILLS_PREFIX + teamId;
        redisCache.deleteObject(key);
    }

    /**
     * 删除队伍信息缓存
     *
     * @param teamId 队伍ID
     */
    public void deleteTeamInfoCache(Long teamId) {
        String key = TEAM_INFO_PREFIX + teamId;
        redisCache.deleteObject(key);
    }

    /**
     * 删除用户信息缓存
     *
     * @param userId 用户ID
     */
    public void deleteUserInfoCache(Long userId) {
        String key = USER_INFO_PREFIX + userId;
        redisCache.deleteObject(key);
    }
}
