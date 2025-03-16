package com.buis.service;

import java.util.List;

import com.buis.common.core.domain.R;
import com.buis.domain.Score;
import com.buis.vo.ScoreVo;

/**
 * 评分管理Service接口
 * 
 * @author buis
 * @date 2024-10-11
 */
public interface IScoreService 
{
    /**
     * 查询评分管理
     * 
     * @param id 评分管理主键
     * @return 评分管理
     */
    public Score selectScoreById(Long id);

    /**
     * 查询评分管理列表
     * 
     * @param score 评分管理
     * @return 评分管理集合
     */
    public List<ScoreVo> selectScoreList(Score score);

    /**
     * 新增评分管理
     * 
     * @param score 评分管理
     * @return 结果
     */
    public int insertScore(Score score);

    /**
     * 修改评分管理
     * 
     * @param score 评分管理
     * @return 结果
     */
    public int updateScore(Score score);

    /**
     * 批量删除评分管理
     * 
     * @param ids 需要删除的评分管理主键集合
     * @return 结果
     */
    public int deleteScoreByIds(Long[] ids);

    /**
     * 删除评分管理信息
     * 
     * @param id 评分管理主键
     * @return 结果
     */
    public int deleteScoreById(Long id);

    /**
     * 新增评分（带业务逻辑）
     * @param score
     * @return
     */
    public R addScore(Score score);
}
