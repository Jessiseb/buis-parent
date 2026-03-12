package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.RaceLanqiaoTypeMapper;
import com.buis.model.domain.RaceLanqiaoType;
import com.buis.service.IRaceLanqiaoTypeService;

/**
 * 蓝桥杯赛道类型Service业务层处理
 * 
 * @author buis
 * @date 2024-10-23
 */
@Service
public class RaceLanqiaoTypeServiceImpl implements IRaceLanqiaoTypeService 
{
    @Autowired
    private RaceLanqiaoTypeMapper raceLanqiaoTypeMapper;

    /**
     * 查询蓝桥杯赛道类型
     * 
     * @param id 蓝桥杯赛道类型主键
     * @return 蓝桥杯赛道类型
     */
    @Override
    public RaceLanqiaoType selectRaceLanqiaoTypeById(Long id)
    {
        return raceLanqiaoTypeMapper.selectRaceLanqiaoTypeById(id);
    }

    /**
     * 查询蓝桥杯赛道类型列表
     * 
     * @param raceLanqiaoType 蓝桥杯赛道类型
     * @return 蓝桥杯赛道类型
     */
    @Override
    public List<RaceLanqiaoType> selectRaceLanqiaoTypeList(RaceLanqiaoType raceLanqiaoType)
    {
        return raceLanqiaoTypeMapper.selectRaceLanqiaoTypeList(raceLanqiaoType);
    }

    /**
     * 新增蓝桥杯赛道类型
     * 
     * @param raceLanqiaoType 蓝桥杯赛道类型
     * @return 结果
     */
    @Override
    public int insertRaceLanqiaoType(RaceLanqiaoType raceLanqiaoType)
    {
        raceLanqiaoType.setCreateTime(DateUtils.getNowDate());
        return raceLanqiaoTypeMapper.insertRaceLanqiaoType(raceLanqiaoType);
    }

    /**
     * 修改蓝桥杯赛道类型
     * 
     * @param raceLanqiaoType 蓝桥杯赛道类型
     * @return 结果
     */
    @Override
    public int updateRaceLanqiaoType(RaceLanqiaoType raceLanqiaoType)
    {
        raceLanqiaoType.setUpdateTime(DateUtils.getNowDate());
        return raceLanqiaoTypeMapper.updateRaceLanqiaoType(raceLanqiaoType);
    }

    /**
     * 批量删除蓝桥杯赛道类型
     * 
     * @param ids 需要删除的蓝桥杯赛道类型主键
     * @return 结果
     */
    @Override
    public int deleteRaceLanqiaoTypeByIds(Long[] ids)
    {
        return raceLanqiaoTypeMapper.deleteRaceLanqiaoTypeByIds(ids);
    }

    /**
     * 删除蓝桥杯赛道类型信息
     * 
     * @param id 蓝桥杯赛道类型主键
     * @return 结果
     */
    @Override
    public int deleteRaceLanqiaoTypeById(Long id)
    {
        return raceLanqiaoTypeMapper.deleteRaceLanqiaoTypeById(id);
    }

    // 判断用户是否为老师

}
