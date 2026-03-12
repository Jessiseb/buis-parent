package com.buis.service;

import java.util.List;
import com.buis.model.domain.RaceSecond;
import com.buis.model.vo.RaceSecondVo;

/**
 * 第二课堂和race_info中间表Service接口
 * 
 * @author buis
 * @date 2025-04-07
 */
public interface IRaceSecondService 
{
    /**
     * 查询第二课堂和race_info中间表
     * 
     * @param id 第二课堂和race_info中间表主键
     * @return 第二课堂和race_info中间表
     */
    public RaceSecond selectRaceSecondById(Long id);

    /**
     * 查询第二课堂和race_info中间表列表
     * 
     * @param raceSecondVo 第二课堂和race_info中间表
     * @return 第二课堂和race_info中间表集合
     */
    public List<RaceSecondVo> selectRaceSecondList(RaceSecondVo raceSecondVo);

    /**
     * 新增第二课堂和race_info中间表
     * 
     * @param raceSecond 第二课堂和race_info中间表
     * @return 结果
     */
    public int insertRaceSecond(RaceSecond raceSecond);

    /**
     * 修改第二课堂和race_info中间表
     * 
     * @param raceSecond 第二课堂和race_info中间表
     * @return 结果
     */
    public int updateRaceSecond(RaceSecond raceSecond);

    /**
     * 批量删除第二课堂和race_info中间表
     * 
     * @param ids 需要删除的第二课堂和race_info中间表主键集合
     * @return 结果
     */
    public int deleteRaceSecondByIds(Long[] ids);

    /**
     * 删除第二课堂和race_info中间表信息
     * 
     * @param id 第二课堂和race_info中间表主键
     * @return 结果
     */
    public int deleteRaceSecondById(Long id);

    RaceSecond selectRaceSecondByRace(Long raceId);

}
