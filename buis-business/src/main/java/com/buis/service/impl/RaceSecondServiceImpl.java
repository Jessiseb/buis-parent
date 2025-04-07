package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import com.buis.vo.RaceSecondVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.RaceSecondMapper;
import com.buis.domain.RaceSecond;
import com.buis.service.IRaceSecondService;

import javax.annotation.Resource;

/**
 * 关联第二课堂表Service业务层处理
 * 
 * @author buis
 * @date 2025-04-04
 */
@Service
public class RaceSecondServiceImpl implements IRaceSecondService 
{
    @Resource
    private RaceSecondMapper raceSecondMapper;

    /**
     * 查询关联第二课堂表
     * 
     * @param id 关联第二课堂表主键
     * @return 关联第二课堂表
     */
    @Override
    public RaceSecond selectRaceSecondById(Long id)
    {
        return raceSecondMapper.selectRaceSecondById(id);
    }

    /**
     * 查询关联第二课堂表列表
     * 
     * @param raceSecondvo 关联第二课堂表
     * @return 关联第二课堂表
     */
    @Override
    public List<RaceSecondVo> selectRaceSecondList(RaceSecondVo raceSecondvo)
    {
        return raceSecondMapper.selectRaceSecondList(raceSecondvo);
    }

    /**
     * 新增关联第二课堂表
     * 
     * @param raceSecond 关联第二课堂表
     * @return 结果
     */
    @Override
    public int insertRaceSecond(RaceSecond raceSecond)
    {
        raceSecond.setCreateTime(DateUtils.getNowDate());
        return raceSecondMapper.insertRaceSecond(raceSecond);
    }

    /**
     * 修改关联第二课堂表
     * 
     * @param raceSecond 关联第二课堂表
     * @return 结果
     */
    @Override
    public int updateRaceSecond(RaceSecond raceSecond)
    {
        raceSecond.setUpdateTime(DateUtils.getNowDate());
        return raceSecondMapper.updateRaceSecond(raceSecond);
    }

    /**
     * 批量删除关联第二课堂表
     * 
     * @param ids 需要删除的关联第二课堂表主键
     * @return 结果
     */
    @Override
    public int deleteRaceSecondByIds(Long[] ids)
    {
        return raceSecondMapper.deleteRaceSecondByIds(ids);
    }

    /**
     * 删除关联第二课堂表信息
     * 
     * @param id 关联第二课堂表主键
     * @return 结果
     */
    @Override
    public int deleteRaceSecondById(Long id)
    {
        return raceSecondMapper.deleteRaceSecondById(id);
    }
}
