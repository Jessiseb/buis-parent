package com.buis.mapper;

import java.util.List;
import com.buis.model.domain.RacePublish;

/**
 * 第二课堂活动发布表Mapper接口
 *
 * @author buis
 * @date 2025-04-04
 */
public interface RacePublishMapper
{
    /**
     * 查询第二课堂活动发布表
     *
     * @param id 第二课堂活动发布表主键
     * @return 第二课堂活动发布表
     */
    public RacePublish selectRacePublishById(Long id);

    /**
     * 查询第二课堂活动发布表列表
     *
     * @param racePublish 第二课堂活动发布表
     * @return 第二课堂活动发布表集合
     */
    public List<RacePublish> selectRacePublishList(RacePublish racePublish);

    /**
     * 新增第二课堂活动发布表
     *
     * @param racePublish 第二课堂活动发布表
     * @return 结果
     */
    public int insertRacePublish(RacePublish racePublish);

    /**
     * 修改第二课堂活动发布表
     *
     * @param racePublish 第二课堂活动发布表
     * @return 结果
     */
    public int updateRacePublish(RacePublish racePublish);

    /**
     * 删除第二课堂活动发布表
     *
     * @param id 第二课堂活动发布表主键
     * @return 结果
     */
    public int deleteRacePublishById(Long id);

    /**
     * 批量删除第二课堂活动发布表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRacePublishByIds(Long[] ids);
}

