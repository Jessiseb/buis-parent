package com.buis.service;

import java.util.List;
import com.buis.domain.RaceSecond;
import com.buis.vo.RaceSecondVo;

/**
 * 关联第二课堂表Service接口
 * 
 * @author buis
 * @date 2025-04-04
 */
public interface IRaceSecondService 
{
    /**
     * 查询关联第二课堂表
     * 
     * @param id 关联第二课堂表主键
     * @return 关联第二课堂表
     */
    public RaceSecond selectRaceSecondById(Long id);

    /**
     * 查询关联第二课堂表列表
     * 
     * @param raceSecond 关联第二课堂表
     * @return 关联第二课堂表集合
     */
    public List<RaceSecondVo> selectRaceSecondList(RaceSecond raceSecond);

    /**
     * 新增关联第二课堂表
     * 
     * @param raceSecond 关联第二课堂表
     * @return 结果
     */
    public int insertRaceSecond(RaceSecond raceSecond);

    /**
     * 修改关联第二课堂表
     * 
     * @param raceSecond 关联第二课堂表
     * @return 结果
     */
    public int updateRaceSecond(RaceSecond raceSecond);

    /**
     * 批量删除关联第二课堂表
     * 
     * @param ids 需要删除的关联第二课堂表主键集合
     * @return 结果
     */
    public int deleteRaceSecondByIds(Long[] ids);

    /**
     * 删除关联第二课堂表信息
     * 
     * @param id 关联第二课堂表主键
     * @return 结果
     */
    public int deleteRaceSecondById(Long id);
}
