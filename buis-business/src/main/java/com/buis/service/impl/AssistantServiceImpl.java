package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.AssistantMapper;
import com.buis.domain.Assistant;
import com.buis.service.IAssistantService;

/**
 * 学生助理Service业务层处理
 * 
 * @author buis
 * @date 2024-10-11
 */
@Service
public class AssistantServiceImpl implements IAssistantService 
{
    @Autowired
    private AssistantMapper assistantMapper;

    /**
     * 查询学生助理
     * 
     * @param id 学生助理主键
     * @return 学生助理
     */
    @Override
    public Assistant selectAssistantById(Long id)
    {
        return assistantMapper.selectAssistantById(id);
    }

    /**
     * 查询学生助理列表
     * 
     * @param assistant 学生助理
     * @return 学生助理
     */
    @Override
    public List<Assistant> selectAssistantList(Assistant assistant)
    {
        return assistantMapper.selectAssistantList(assistant);
    }

    /**
     * 新增学生助理
     * 
     * @param assistant 学生助理
     * @return 结果
     */
    @Override
    public int insertAssistant(Assistant assistant)
    {
        assistant.setCreateTime(DateUtils.getNowDate());
        return assistantMapper.insertAssistant(assistant);
    }

    /**
     * 修改学生助理
     * 
     * @param assistant 学生助理
     * @return 结果
     */
    @Override
    public int updateAssistant(Assistant assistant)
    {
        assistant.setUpdateTime(DateUtils.getNowDate());
        return assistantMapper.updateAssistant(assistant);
    }

    /**
     * 批量删除学生助理
     * 
     * @param ids 需要删除的学生助理主键
     * @return 结果
     */
    @Override
    public int deleteAssistantByIds(Long[] ids)
    {
        return assistantMapper.deleteAssistantByIds(ids);
    }

    /**
     * 删除学生助理信息
     * 
     * @param id 学生助理主键
     * @return 结果
     */
    @Override
    public int deleteAssistantById(Long id)
    {
        return assistantMapper.deleteAssistantById(id);
    }
}
