package com.buis.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.buis.common.core.redis.RedisCache;
import com.buis.common.utils.DateUtils;
import com.buis.model.domain.SkillTag;
import com.buis.model.vo.SkillDemandStatVo;
import com.buis.model.vo.StudentSkillStatVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.SkillTagMapper;
import com.buis.service.ISkillTagService;

import javax.annotation.Resource;

/**
 * 技能标签主Service业务层处理
 * 
 * @author buis
 * @date 2026-03-11
 */
@Service
public class SkillTagServiceImpl implements ISkillTagService
{
    @Resource
    private SkillTagMapper skillTagMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询技能标签主
     * 
     * @param id 技能标签主主键
     * @return 技能标签主
     */
    @Override
    public SkillTag selectSkillTagById(Long id)
    {
        return skillTagMapper.selectSkillTagById(id);
    }

    /**
     * 查询技能标签主列表
     * 
     * @param skillTag 技能标签主
     * @return 技能标签主
     */
    @Override
    public List<SkillTag> selectSkillTagList(SkillTag skillTag)
    {
        return skillTagMapper.selectSkillTagList(skillTag);
    }

    /**
     * 新增技能标签主
     * 
     * @param skillTag 技能标签主
     * @return 结果
     */
    @Override
    public int insertSkillTag(SkillTag skillTag)
    {
        skillTag.setCreateTime(DateUtils.getNowDate());
        return skillTagMapper.insertSkillTag(skillTag);
    }

    /**
     * 修改技能标签主
     * 
     * @param skillTag 技能标签主
     * @return 结果
     */
    @Override
    public int updateSkillTag(SkillTag skillTag)
    {
        skillTag.setUpdateTime(DateUtils.getNowDate());
        return skillTagMapper.updateSkillTag(skillTag);
    }

    /**
     * 批量删除技能标签主
     * 
     * @param ids 需要删除的技能标签主主键
     * @return 结果
     */
    @Override
    public int deleteSkillTagByIds(Long[] ids)
    {
        return skillTagMapper.deleteSkillTagByIds(ids);
    }

    /**
     * 删除技能标签主信息
     * 
     * @param id 技能标签主主键
     * @return 结果
     */
    @Override
    public int deleteSkillTagById(Long id)
    {
        return skillTagMapper.deleteSkillTagById(id);
    }




    /**
     * 生成随机过期时间（6-8天）
     */
    private int generateRandomExpireSeconds() {
        Random random = new Random();
        int baseSeconds = 6 * 24 * 60 * 60;      // 6天
        int randomRange = 24 * 60 * 60;         // ±1天
        return baseSeconds + random.nextInt(2 * randomRange + 1);
    }

    /**
     * 获取学生掌握的技能统计（前7条）
     */
    @Override
    public List<StudentSkillStatVo> getSkillTagByAllUser() {
        final String CACHE_KEY = "skill:student:stats:top7";

        // 1. 先尝试从缓存获取
        List<StudentSkillStatVo> cachedData = redisCache.getCacheObject(CACHE_KEY);
        if (cachedData != null) {
            return cachedData;
        }

        // 2. 缓存不存在，查询数据库
        List<StudentSkillStatVo> data = skillTagMapper.selectTop8StudentSkillStats();

        // 3. 如果数据库中数据为空，返回空列表
        if (data == null) {
            data = new ArrayList<>();
        }

        // 4. 设置缓存，6-8天随机过期
        int expireSeconds = generateRandomExpireSeconds();
        redisCache.setCacheObject(CACHE_KEY, data, expireSeconds, TimeUnit.SECONDS);

        return data;
    }


    @Override
    public List<SkillDemandStatVo> getSkillDemandStatistics() {
        final String CACHE_KEY = "skill:demand:stats:all";
        // 1. 先尝试从缓存获取
        List<SkillDemandStatVo> cachedData = redisCache.getCacheObject(CACHE_KEY);
        if (cachedData != null) {
            return cachedData;
        }
        // 2. 缓存不存在，查询数据库
        List<SkillDemandStatVo> data = skillTagMapper.countSkillDemandByTeams();
        // 3. 如果数据库中数据为空，返回空列表
        if (data == null) {
            data = new ArrayList<>();
        }
        // 4. 设置缓存，不设置过期时间
        redisCache.setCacheObject(CACHE_KEY, data);
        return data;
    }


}

