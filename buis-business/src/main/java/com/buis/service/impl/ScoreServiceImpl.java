package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import com.buis.vo.ScoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.ScoreMapper;
import com.buis.domain.Score;
import com.buis.service.IScoreService;

/**
 * 评分管理Service业务层处理
 * 
 * @author buis
 * @date 2024-10-11
 */
@Service
public class ScoreServiceImpl implements IScoreService 
{
    @Autowired
    private ScoreMapper scoreMapper;

    /**
     * 查询评分管理
     * 
     * @param id 评分管理主键
     * @return 评分管理
     */
    @Override
    public Score selectScoreById(Long id)
    {
        return scoreMapper.selectScoreById(id);
    }

    /**
     * 查询评分管理列表
     * 
     * @param score 评分管理
     * @return 评分管理
     */
    @Override
    public List<ScoreVo> selectScoreList(Score score)
    {
        return scoreMapper.selectScoreList(score);
    }

    /**
     * 新增评分管理
     * 
     * @param score 评分管理
     * @return 结果
     */
    @Override
    public int insertScore(Score score)
    {
        score.setCreateTime(DateUtils.getNowDate());
        return scoreMapper.insertScore(score);
    }

    /**
     * 修改评分管理
     * 
     * @param score 评分管理
     * @return 结果
     */
    @Override
    public int updateScore(Score score)
    {
        score.setUpdateTime(DateUtils.getNowDate());
        return scoreMapper.updateScore(score);
    }

    /**
     * 批量删除评分管理
     * 
     * @param ids 需要删除的评分管理主键
     * @return 结果
     */
    @Override
    public int deleteScoreByIds(Long[] ids)
    {
        return scoreMapper.deleteScoreByIds(ids);
    }

    /**
     * 删除评分管理信息
     * 
     * @param id 评分管理主键
     * @return 结果
     */
    @Override
    public int deleteScoreById(Long id)
    {
        return scoreMapper.deleteScoreById(id);
    }
}
