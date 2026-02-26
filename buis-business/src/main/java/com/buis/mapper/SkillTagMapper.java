package com.buis.mapper;

import com.buis.model.domain.SkillTag;
import com.buis.model.vo.SkillTagVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 技能标签Mapper接口
 *
 * @author buis
 */
@Repository
public interface SkillTagMapper {

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
     * 根据标签名称查询技能标签
     *
     * @param tagName 标签名称
     * @return 技能标签
     */
    SkillTag selectSkillTagByName(String tagName);

    /**
     * 根据标签名称列表查询技能标签
     *
     * @param tagNames 标签名称列表
     * @return 技能标签列表
     */
    List<SkillTag> selectSkillTagsByNames(@Param("tagNames") List<String> tagNames);

    /**
     * 根据分类查询技能标签
     *
     * @param category 分类
     * @return 技能标签列表
     */
    List<SkillTag> selectSkillTagsByCategory(String category);

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
     * 删除技能标签
     *
     * @param id 技能标签主键
     * @return 结果
     */
    int deleteSkillTagById(Long id);

    /**
     * 批量删除技能标签
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteSkillTagByIds(Long[] ids);

    /**
     * 根据ID列表查询技能标签
     *
     * @param ids 技能标签ID列表
     * @return 技能标签列表
     */
    List<SkillTag> selectSkillTagsByIds(@Param("ids") List<Long> ids);

    /**
     * 统计技能标签数量
     *
     * @return 数量
     */
    int countSkillTags();

    /**
     * 搜索技能标签（支持模糊搜索）
     *
     * @param keyword 关键词
     * @return 技能标签列表
     */
    List<SkillTag> searchSkillTags(String keyword);
}