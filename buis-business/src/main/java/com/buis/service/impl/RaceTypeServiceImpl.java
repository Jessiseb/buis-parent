package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.RaceTypeMapper;
import com.buis.model.domain.RaceType;
import com.buis.service.IRaceTypeService;

/**
 * 比赛类型Service业务层处理
 * 
 * @author buis
 * @date 2024-10-23
 */
@Service
public class RaceTypeServiceImpl implements IRaceTypeService 
{
    @Autowired
    private RaceTypeMapper raceTypeMapper;

    /**
     * 查询比赛类型
     * 
     * @param id 比赛类型主键
     * @return 比赛类型
     */
    @Override
    public RaceType selectRaceTypeById(Long id)
    {
        return raceTypeMapper.selectRaceTypeById(id);
    }

    /**
     * 查询比赛类型列表
     * 
     * @param raceType 比赛类型
     * @return 比赛类型
     */
    @Override
    public List<RaceType> selectRaceTypeList(RaceType raceType)
    {
        return raceTypeMapper.selectRaceTypeList(raceType);
    }

    /**
     * 新增比赛类型
     * 
     * @param raceType 比赛类型
     * @return 结果
     */
    @Override
    public int insertRaceType(RaceType raceType)
    {
        raceType.setCreateTime(DateUtils.getNowDate());
        return raceTypeMapper.insertRaceType(raceType);
    }

    /**
     * 修改比赛类型
     * 
     * @param raceType 比赛类型
     * @return 结果
     */
    @Override
    public int updateRaceType(RaceType raceType)
    {
        raceType.setUpdateTime(DateUtils.getNowDate());
        return raceTypeMapper.updateRaceType(raceType);
    }

    /**
     * 批量删除比赛类型
     * 
     * @param ids 需要删除的比赛类型主键
     * @return 结果
     */
    @Override
    public int deleteRaceTypeByIds(Long[] ids)
    {
        return raceTypeMapper.deleteRaceTypeByIds(ids);
    }

    /**
     * 删除比赛类型信息
     * 
     * @param id 比赛类型主键
     * @return 结果
     */
    @Override
    public int deleteRaceTypeById(Long id)
    {
        return raceTypeMapper.deleteRaceTypeById(id);
    }
}
