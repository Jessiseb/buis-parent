package com.buis.mapper;

import java.util.List;


import com.buis.model.domain.Score;
import com.buis.model.vo.ScoreVo;

/**
 * 评分管理Mapper接口
 * 
 * @author buis
 * @date 2024-10-11
 */
public interface ScoreMapper
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
     * 删除评分管理
     * 
     * @param id 评分管理主键
     * @return 结果
     */
    public int deleteScoreById(Long id);

    /**
     * 批量删除评分管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteScoreByIds(Long[] ids);

    List<Score> selectTeamsRanking();

    List<ScoreVo> selectScoreListFuzzyQuery( ScoreVo scoreVo);
}
