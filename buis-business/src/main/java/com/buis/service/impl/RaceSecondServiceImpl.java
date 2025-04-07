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
 * 第二课堂和race_info中间表Service业务层处理
 * 
 * @author buis
 * @date 2025-04-07
 */
@Service
public class RaceSecondServiceImpl implements IRaceSecondService 
{
    @Resource
    private RaceSecondMapper raceSecondMapper;

    /**
     * 查询第二课堂和race_info中间表
     * 
     * @param id 第二课堂和race_info中间表主键
     * @return 第二课堂和race_info中间表
     */
    @Override
    public RaceSecond selectRaceSecondById(Long id)
    {
        return raceSecondMapper.selectRaceSecondById(id);
    }

    /**
     * 查询第二课堂和race_info中间表列表
     * 
     * @param raceSecondVo 第二课堂和race_info中间表
     * @return 第二课堂和race_info中间表
     */
    @Override
    public List<RaceSecondVo> selectRaceSecondList(RaceSecondVo raceSecondVo)
    {
        return raceSecondMapper.selectRaceSecondList(raceSecondVo);
    }

    /**
     * 新增第二课堂和race_info中间表
     * 
     * @param raceSecond 第二课堂和race_info中间表
     * @return 结果
     */
    @Override
    public int insertRaceSecond(RaceSecond raceSecond)
    {
        raceSecond.setCreateTime(DateUtils.getNowDate());
        return raceSecondMapper.insertRaceSecond(raceSecond);
    }

    /**
     * 修改第二课堂和race_info中间表
     * 
     * @param raceSecond 第二课堂和race_info中间表
     * @return 结果
     */
    @Override
    public int updateRaceSecond(RaceSecond raceSecond)
    {
        raceSecond.setUpdateTime(DateUtils.getNowDate());
        return raceSecondMapper.updateRaceSecond(raceSecond);
    }

    /**
     * 批量删除第二课堂和race_info中间表
     * 
     * @param ids 需要删除的第二课堂和race_info中间表主键
     * @return 结果
     */
    @Override
    public int deleteRaceSecondByIds(Long[] ids)
    {
        return raceSecondMapper.deleteRaceSecondByIds(ids);
    }

    /**
     * 删除第二课堂和race_info中间表信息
     * 
     * @param id 第二课堂和race_info中间表主键
     * @return 结果
     */
    @Override
    public int deleteRaceSecondById(Long id)
    {
        return raceSecondMapper.deleteRaceSecondById(id);
    }
}
