package com.buis.service;

import java.util.List;
import com.buis.model.domain.RaceType;

/**
 * 比赛类型Service接口
 * 
 * @author buis
 * @date 2024-10-23
 */
public interface IRaceTypeService 
{
    /**
     * 查询比赛类型
     * 
     * @param id 比赛类型主键
     * @return 比赛类型
     */
    public RaceType selectRaceTypeById(Long id);

    /**
     * 查询比赛类型列表
     * 
     * @param raceType 比赛类型
     * @return 比赛类型集合
     */
    public List<RaceType> selectRaceTypeList(RaceType raceType);

    /**
     * 新增比赛类型
     * 
     * @param raceType 比赛类型
     * @return 结果
     */
    public int insertRaceType(RaceType raceType);

    /**
     * 修改比赛类型
     * 
     * @param raceType 比赛类型
     * @return 结果
     */
    public int updateRaceType(RaceType raceType);

    /**
     * 批量删除比赛类型
     * 
     * @param ids 需要删除的比赛类型主键集合
     * @return 结果
     */
    public int deleteRaceTypeByIds(Long[] ids);

    /**
     * 删除比赛类型信息
     * 
     * @param id 比赛类型主键
     * @return 结果
     */
    public int deleteRaceTypeById(Long id);
}
