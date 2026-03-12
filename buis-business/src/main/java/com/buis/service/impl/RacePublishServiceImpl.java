package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.RacePublishMapper;
import com.buis.model.domain.RacePublish;
import com.buis.service.IRacePublishService;

/**
 * 第二课堂活动发布表Service业务层处理
 *
 * @author buis
 * @date 2025-04-04
 */
@Service
public class RacePublishServiceImpl implements IRacePublishService
{
    @Autowired
    private RacePublishMapper racePublishMapper;

    /**
     * 查询第二课堂活动发布表
     *
     * @param id 第二课堂活动发布表主键
     * @return 第二课堂活动发布表
     */
    @Override
    public RacePublish selectRacePublishById(Long id)
    {
        return racePublishMapper.selectRacePublishById(id);
    }

    /**
     * 查询第二课堂活动发布表列表
     *
     * @param racePublish 第二课堂活动发布表
     * @return 第二课堂活动发布表
     */
    @Override
    public List<RacePublish> selectRacePublishList(RacePublish racePublish)
    {
        return racePublishMapper.selectRacePublishList(racePublish);
    }

    /**
     * 新增第二课堂活动发布表
     *
     * @param racePublish 第二课堂活动发布表
     * @return 结果
     */
    @Override
    public int insertRacePublish(RacePublish racePublish)
    {
        racePublish.setCreateTime(DateUtils.getNowDate());
        return racePublishMapper.insertRacePublish(racePublish);
    }

    /**
     * 修改第二课堂活动发布表
     *
     * @param racePublish 第二课堂活动发布表
     * @return 结果
     */
    @Override
    public int updateRacePublish(RacePublish racePublish)
    {
        racePublish.setUpdateTime(DateUtils.getNowDate());
        return racePublishMapper.updateRacePublish(racePublish);
    }

    /**
     * 批量删除第二课堂活动发布表
     *
     * @param ids 需要删除的第二课堂活动发布表主键
     * @return 结果
     */
    @Override
    public int deleteRacePublishByIds(Long[] ids)
    {
        return racePublishMapper.deleteRacePublishByIds(ids);
    }

    /**
     * 删除第二课堂活动发布表信息
     *
     * @param id 第二课堂活动发布表主键
     * @return 结果
     */
    @Override
    public int deleteRacePublishById(Long id)
    {
        return racePublishMapper.deleteRacePublishById(id);
    }
}

