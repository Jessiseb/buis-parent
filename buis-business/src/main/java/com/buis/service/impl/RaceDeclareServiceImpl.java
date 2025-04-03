package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.RaceDeclareMapper;
import com.buis.domain.RaceDeclare;
import com.buis.service.IRaceDeclareService;

import javax.annotation.Resource;

/**
 * 第二课堂活动申报表Service业务层处理
 * 
 * @author buis
 * @date 2025-04-03
 */
@Service
public class RaceDeclareServiceImpl implements IRaceDeclareService 
{
    @Resource
    private RaceDeclareMapper raceDeclareMapper;

    /**
     * 查询第二课堂活动申报表
     * 
     * @param id 第二课堂活动申报表主键
     * @return 第二课堂活动申报表
     */
    @Override
    public RaceDeclare selectRaceDeclareById(Long id)
    {
        return raceDeclareMapper.selectRaceDeclareById(id);
    }

    /**
     * 查询第二课堂活动申报表列表
     * 
     * @param raceDeclare 第二课堂活动申报表
     * @return 第二课堂活动申报表
     */
    @Override
    public List<RaceDeclare> selectRaceDeclareList(RaceDeclare raceDeclare)
    {
        return raceDeclareMapper.selectRaceDeclareList(raceDeclare);
    }

    /**
     * 新增第二课堂活动申报表
     * 
     * @param raceDeclare 第二课堂活动申报表
     * @return 结果
     */
    @Override
    public int insertRaceDeclare(RaceDeclare raceDeclare)
    {
        raceDeclare.setCreateTime(DateUtils.getNowDate());
        return raceDeclareMapper.insertRaceDeclare(raceDeclare);
    }

    /**
     * 修改第二课堂活动申报表
     * 
     * @param raceDeclare 第二课堂活动申报表
     * @return 结果
     */
    @Override
    public int updateRaceDeclare(RaceDeclare raceDeclare)
    {
        raceDeclare.setUpdateTime(DateUtils.getNowDate());
        return raceDeclareMapper.updateRaceDeclare(raceDeclare);
    }

    /**
     * 批量删除第二课堂活动申报表
     * 
     * @param ids 需要删除的第二课堂活动申报表主键
     * @return 结果
     */
    @Override
    public int deleteRaceDeclareByIds(Long[] ids)
    {
        return raceDeclareMapper.deleteRaceDeclareByIds(ids);
    }

    /**
     * 删除第二课堂活动申报表信息
     * 
     * @param id 第二课堂活动申报表主键
     * @return 结果
     */
    @Override
    public int deleteRaceDeclareById(Long id)
    {
        return raceDeclareMapper.deleteRaceDeclareById(id);
    }
}
