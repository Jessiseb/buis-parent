package com.buis.service;

import java.util.List;
import com.buis.domain.Judge;

/**
 * 评委管理Service接口
 * 
 * @author buis
 * @date 2024-10-10
 */
public interface IJudgeService 
{
    /**
     * 查询评委管理
     * 
     * @param id 评委管理主键
     * @return 评委管理
     */
    public Judge selectJudgeById(Long id);

    /**
     * 查询评委管理列表
     * 
     * @param judge 评委管理
     * @return 评委管理集合
     */
    public List<Judge> selectJudgeList(Judge judge);

    /**
     * 新增评委管理
     * 
     * @param judge 评委管理
     * @return 结果
     */
    public int insertJudge(Judge judge);

    /**
     * 修改评委管理
     * 
     * @param judge 评委管理
     * @return 结果
     */
    public int updateJudge(Judge judge);

    /**
     * 批量删除评委管理
     * 
     * @param ids 需要删除的评委管理主键集合
     * @return 结果
     */
    public int deleteJudgeByIds(Long[] ids);

    /**
     * 删除评委管理信息
     * 
     * @param id 评委管理主键
     * @return 结果
     */
    public int deleteJudgeById(Long id);
}
