package com.ruoyi.businessmanager.score.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.businessmanager.score.mapper.TScoreMapper;
import com.ruoyi.businessmanager.score.domain.entities.TScore;
import com.ruoyi.businessmanager.score.service.ITScoreService;

/**
 * 评委评分功能Service业务层处理
 * 
 * @author zjx
 * @date 2024-10-01
 */
@Service
public class TScoreServiceImpl implements ITScoreService 
{
    @Autowired
    private TScoreMapper tScoreMapper;

    /**
     * 查询评委评分功能
     * 
     * @param id 评委评分功能主键
     * @return 评委评分功能
     */
    @Override
    public TScore selectTScoreById(Long id)
    {
        return tScoreMapper.selectTScoreById(id);
    }

    /**
     * 查询评委评分功能列表
     * 
     * @param tScore 评委评分功能
     * @return 评委评分功能
     */
    @Override
    public List<TScore> selectTScoreList(TScore tScore)
    {
        return tScoreMapper.selectTScoreList(tScore);
    }

    /**
     * 新增评委评分功能
     * 
     * @param tScore 评委评分功能
     * @return 结果
     */
    @Override
    public int insertTScore(TScore tScore)
    {
        return tScoreMapper.insertTScore(tScore);
    }

    /**
     * 修改评委评分功能
     * 
     * @param tScore 评委评分功能
     * @return 结果
     */
    @Override
    public int updateTScore(TScore tScore)
    {
        return tScoreMapper.updateTScore(tScore);
    }

    /**
     * 批量删除评委评分功能
     * 
     * @param ids 需要删除的评委评分功能主键
     * @return 结果
     */
    @Override
    public int deleteTScoreByIds(Long[] ids)
    {
        return tScoreMapper.deleteTScoreByIds(ids);
    }

    /**
     * 删除评委评分功能信息
     * 
     * @param id 评委评分功能主键
     * @return 结果
     */
    @Override
    public int deleteTScoreById(Long id)
    {
        return tScoreMapper.deleteTScoreById(id);
    }
}
