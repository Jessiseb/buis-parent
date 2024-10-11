package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.JudgeMapper;
import com.buis.domain.Judge;
import com.buis.service.IJudgeService;

/**
 * 评委管理Service业务层处理
 * 
 * @author buis
 * @date 2024-10-10
 */
@Service
public class JudgeServiceImpl implements IJudgeService 
{
    @Autowired
    private JudgeMapper judgeMapper;

    /**
     * 查询评委管理
     * 
     * @param id 评委管理主键
     * @return 评委管理
     */
    @Override
    public Judge selectJudgeById(Long id)
    {
        return judgeMapper.selectJudgeById(id);
    }

    /**
     * 查询评委管理列表
     * 
     * @param judge 评委管理
     * @return 评委管理
     */
    @Override
    public List<Judge> selectJudgeList(Judge judge)
    {
        return judgeMapper.selectJudgeList(judge);
    }

    /**
     * 新增评委管理
     * 
     * @param judge 评委管理
     * @return 结果
     */
    @Override
    public int insertJudge(Judge judge)
    {
        judge.setCreateTime(DateUtils.getNowDate());
        return judgeMapper.insertJudge(judge);
    }

    /**
     * 修改评委管理
     * 
     * @param judge 评委管理
     * @return 结果
     */
    @Override
    public int updateJudge(Judge judge)
    {
        judge.setUpdateTime(DateUtils.getNowDate());
        return judgeMapper.updateJudge(judge);
    }

    /**
     * 批量删除评委管理
     * 
     * @param ids 需要删除的评委管理主键
     * @return 结果
     */
    @Override
    public int deleteJudgeByIds(Long[] ids)
    {
        return judgeMapper.deleteJudgeByIds(ids);
    }

    /**
     * 删除评委管理信息
     * 
     * @param id 评委管理主键
     * @return 结果
     */
    @Override
    public int deleteJudgeById(Long id)
    {
        return judgeMapper.deleteJudgeById(id);
    }
}
