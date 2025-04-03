package com.buis.mapper;

import java.util.List;
import com.buis.domain.RaceDeclare;

/**
 * 第二课堂活动申报表Mapper接口
 * 
 * @author buis
 * @date 2025-04-03
 */
public interface RaceDeclareMapper 
{
    /**
     * 查询第二课堂活动申报表
     * 
     * @param id 第二课堂活动申报表主键
     * @return 第二课堂活动申报表
     */
    public RaceDeclare selectRaceDeclareById(Long id);

    /**
     * 查询第二课堂活动申报表列表
     * 
     * @param raceDeclare 第二课堂活动申报表
     * @return 第二课堂活动申报表集合
     */
    public List<RaceDeclare> selectRaceDeclareList(RaceDeclare raceDeclare);

    /**
     * 新增第二课堂活动申报表
     * 
     * @param raceDeclare 第二课堂活动申报表
     * @return 结果
     */
    public int insertRaceDeclare(RaceDeclare raceDeclare);

    /**
     * 修改第二课堂活动申报表
     * 
     * @param raceDeclare 第二课堂活动申报表
     * @return 结果
     */
    public int updateRaceDeclare(RaceDeclare raceDeclare);

    /**
     * 删除第二课堂活动申报表
     * 
     * @param id 第二课堂活动申报表主键
     * @return 结果
     */
    public int deleteRaceDeclareById(Long id);

    /**
     * 批量删除第二课堂活动申报表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRaceDeclareByIds(Long[] ids);
}
