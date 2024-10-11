package com.buis.mapper;

import java.util.List;
import com.buis.domain.RaceType;

/**
 * 比赛类型Mapper接口
 * 
 * @author buis
 * @date 2024-10-11
 */
public interface RaceTypeMapper 
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
     * 删除比赛类型
     * 
     * @param id 比赛类型主键
     * @return 结果
     */
    public int deleteRaceTypeById(Long id);

    /**
     * 批量删除比赛类型
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRaceTypeByIds(Long[] ids);
}
