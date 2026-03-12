package com.buis.service;

import java.util.List;
import com.buis.model.domain.RaceLanqiaoType;

/**
 * 蓝桥杯赛道类型Service接口
 * 
 * @author buis
 * @date 2024-10-23
 */
public interface IRaceLanqiaoTypeService 
{
    /**
     * 查询蓝桥杯赛道类型
     * 
     * @param id 蓝桥杯赛道类型主键
     * @return 蓝桥杯赛道类型
     */
    public RaceLanqiaoType selectRaceLanqiaoTypeById(Long id);

    /**
     * 查询蓝桥杯赛道类型列表
     * 
     * @param raceLanqiaoType 蓝桥杯赛道类型
     * @return 蓝桥杯赛道类型集合
     */
    public List<RaceLanqiaoType> selectRaceLanqiaoTypeList(RaceLanqiaoType raceLanqiaoType);

    /**
     * 新增蓝桥杯赛道类型
     * 
     * @param raceLanqiaoType 蓝桥杯赛道类型
     * @return 结果
     */
    public int insertRaceLanqiaoType(RaceLanqiaoType raceLanqiaoType);

    /**
     * 修改蓝桥杯赛道类型
     * 
     * @param raceLanqiaoType 蓝桥杯赛道类型
     * @return 结果
     */
    public int updateRaceLanqiaoType(RaceLanqiaoType raceLanqiaoType);

    /**
     * 批量删除蓝桥杯赛道类型
     * 
     * @param ids 需要删除的蓝桥杯赛道类型主键集合
     * @return 结果
     */
    public int deleteRaceLanqiaoTypeByIds(Long[] ids);

    /**
     * 删除蓝桥杯赛道类型信息
     * 
     * @param id 蓝桥杯赛道类型主键
     * @return 结果
     */
    public int deleteRaceLanqiaoTypeById(Long id);
}
