package com.buis.service;

import com.buis.model.domain.SkillTag;
import com.buis.model.vo.SkillDemandStatVo;
import com.buis.model.vo.StudentSkillStatVo;

import java.util.List;


/**
 * 技能标签主Service接口
 * 
 * @author buis
 * @date 2026-03-11
 */
public interface ISkillTagService 
{
    /**
     * 查询技能标签主
     * 
     * @param id 技能标签主主键
     * @return 技能标签主
     */
    public SkillTag selectSkillTagById(Long id);

    /**
     * 查询技能标签主列表
     * 
     * @param skillTag 技能标签主
     * @return 技能标签主集合
     */
    public List<SkillTag> selectSkillTagList(SkillTag skillTag);

    /**
     * 新增技能标签主
     * 
     * @param skillTag 技能标签主
     * @return 结果
     */
    public int insertSkillTag(SkillTag skillTag);

    /**
     * 修改技能标签主
     * 
     * @param skillTag 技能标签主
     * @return 结果
     */
    public int updateSkillTag(SkillTag skillTag);

    /**
     * 批量删除技能标签主
     * 
     * @param ids 需要删除的技能标签主主键集合
     * @return 结果
     */
    public int deleteSkillTagByIds(Long[] ids);

    /**
     * 删除技能标签主信息
     * 
     * @param id 技能标签主主键
     * @return 结果
     */
    public int deleteSkillTagById(Long id);

    List<StudentSkillStatVo> getSkillTagByAllUser();


    /**
     * 获取技能需求统计（所有技能）
     */
    List<SkillDemandStatVo> getSkillDemandStatistics();
}
