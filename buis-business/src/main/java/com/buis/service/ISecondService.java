package com.buis.service;

import java.util.List;
import com.buis.model.domain.Second;

/**
 * 关联第二课堂表Service接口
 * 
 * @author buis
 * @date 2025-04-04
 */
public interface ISecondService
{
    /**
     * 查询关联第二课堂表
     * 
     * @param id 关联第二课堂表主键
     * @return 关联第二课堂表
     */
    public Second selectSecondById(Long id);

    /**
     * 查询关联第二课堂表列表
     * 
     * @param second 关联第二课堂表
     * @return 关联第二课堂表集合
     */
    public List<Second> selectSecondList(Second second);

    /**
     * 新增关联第二课堂表
     * 
     * @param second 关联第二课堂表
     * @return 结果
     */
    public int insertSecond(Second second);

    /**
     * 修改关联第二课堂表
     * 
     * @param second 关联第二课堂表
     * @return 结果
     */
    public int updateSecond(Second second);

    /**
     * 批量删除关联第二课堂表
     * 
     * @param ids 需要删除的关联第二课堂表主键集合
     * @return 结果
     */
    public int deleteSecondByIds(Long[] ids);

    /**
     * 删除关联第二课堂表信息
     * 
     * @param id 关联第二课堂表主键
     * @return 结果
     */
    public int deleteSecondById(Long id);
}
