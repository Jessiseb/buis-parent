package com.ruoyi.businessmanager.score.service;

import java.util.List;
import com.ruoyi.businessmanager.score.domain.entities.TScore;

/**
 * 评委评分功能Service接口
 * 
 * @author zjx
 * @date 2024-10-01
 */
public interface ITScoreService 
{
    /**
     * 查询评委评分功能
     * 
     * @param id 评委评分功能主键
     * @return 评委评分功能
     */
    public TScore selectTScoreById(Long id);

    /**
     * 查询评委评分功能列表
     * 
     * @param tScore 评委评分功能
     * @return 评委评分功能集合
     */
    public List<TScore> selectTScoreList(TScore tScore);

    /**
     * 新增评委评分功能
     * 
     * @param tScore 评委评分功能
     * @return 结果
     */
    public int insertTScore(TScore tScore);

    /**
     * 修改评委评分功能
     * 
     * @param tScore 评委评分功能
     * @return 结果
     */
    public int updateTScore(TScore tScore);

    /**
     * 批量删除评委评分功能
     * 
     * @param ids 需要删除的评委评分功能主键集合
     * @return 结果
     */
    public int deleteTScoreByIds(Long[] ids);

    /**
     * 删除评委评分功能信息
     * 
     * @param id 评委评分功能主键
     * @return 结果
     */
    public int deleteTScoreById(Long id);
}
