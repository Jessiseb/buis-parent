package com.buis.service;

import com.buis.model.domain.SkillTag;
import com.buis.model.vo.SkillTagVo;

import java.util.List;

/**
 * 技能标签服务接口
 *
 *
 */
public interface ISkillTagService {

    /**
     * 查询技能标签
     *
     * @param id 技能标签主键
     * @return 技能标签
     */
    SkillTag selectSkillTagById(Long id);

    /**
     * 查询技能标签列表
     *
     * @param skillTag 技能标签
     * @return 技能标签集合
     */
    List<SkillTagVo> selectSkillTagList(SkillTag skillTag);

    /**
     * 查询所有启用的技能标签
     *
     * @return 技能标签列表
     */
    List<SkillTag> selectAllActiveTags();

    /**
     * 根据分类查询技能标签
     *
     * @param category 分类
     * @return 技能标签列表
     */
    List<SkillTag> selectSkillTagsByCategory(String category);

    /**
     * 搜索技能标签
     *
     * @param keyword 关键词
     * @return 技能标签列表
     */
    List<SkillTag> searchSkillTags(String keyword);

    /**
     * 新增技能标签
     *
     * @param skillTag 技能标签
     * @return 结果
     */
    int insertSkillTag(SkillTag skillTag);

    /**
     * 修改技能标签
     *
     * @param skillTag 技能标签
     * @return 结果
     */
    int updateSkillTag(SkillTag skillTag);

    /**
     * 批量删除技能标签
     *
     * @param ids 需要删除的技能标签主键集合
     * @return 结果
     */
    int deleteSkillTagByIds(Long[] ids);

    /**
     * 删除技能标签信息
     *
     * @param id 技能标签主键
     * @return 结果
     */
    int deleteSkillTagById(Long id);
}