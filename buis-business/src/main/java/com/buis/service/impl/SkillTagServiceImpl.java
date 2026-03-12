package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import com.buis.model.domain.SkillTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.SkillTagMapper;
import com.buis.service.ISkillTagService;

import javax.annotation.Resource;

/**
 * 技能标签主Service业务层处理
 * 
 * @author buis
 * @date 2026-03-11
 */
@Service
public class SkillTagServiceImpl implements ISkillTagService
{
    @Resource
    private SkillTagMapper skillTagMapper;

    /**
     * 查询技能标签主
     * 
     * @param id 技能标签主主键
     * @return 技能标签主
     */
    @Override
    public SkillTag selectSkillTagById(Long id)
    {
        return skillTagMapper.selectSkillTagById(id);
    }

    /**
     * 查询技能标签主列表
     * 
     * @param skillTag 技能标签主
     * @return 技能标签主
     */
    @Override
    public List<SkillTag> selectSkillTagList(SkillTag skillTag)
    {
        return skillTagMapper.selectSkillTagList(skillTag);
    }

    /**
     * 新增技能标签主
     * 
     * @param skillTag 技能标签主
     * @return 结果
     */
    @Override
    public int insertSkillTag(SkillTag skillTag)
    {
        skillTag.setCreateTime(DateUtils.getNowDate());
        return skillTagMapper.insertSkillTag(skillTag);
    }

    /**
     * 修改技能标签主
     * 
     * @param skillTag 技能标签主
     * @return 结果
     */
    @Override
    public int updateSkillTag(SkillTag skillTag)
    {
        skillTag.setUpdateTime(DateUtils.getNowDate());
        return skillTagMapper.updateSkillTag(skillTag);
    }

    /**
     * 批量删除技能标签主
     * 
     * @param ids 需要删除的技能标签主主键
     * @return 结果
     */
    @Override
    public int deleteSkillTagByIds(Long[] ids)
    {
        return skillTagMapper.deleteSkillTagByIds(ids);
    }

    /**
     * 删除技能标签主信息
     * 
     * @param id 技能标签主主键
     * @return 结果
     */
    @Override
    public int deleteSkillTagById(Long id)
    {
        return skillTagMapper.deleteSkillTagById(id);
    }
}
