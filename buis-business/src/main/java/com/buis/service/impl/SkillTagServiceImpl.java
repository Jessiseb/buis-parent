package com.buis.service.impl;

import com.buis.common.exception.ServiceException;
import com.buis.mapper.SkillTagMapper;
import com.buis.model.domain.SkillTag;
import com.buis.model.vo.SkillTagVo;
import com.buis.service.ISkillTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 技能标签服务实现
 *
 *
 */
@Service
public class SkillTagServiceImpl implements ISkillTagService {

    @Autowired
    private SkillTagMapper skillTagMapper;

    /**
     * 查询技能标签
     *
     * @param id 技能标签主键
     * @return 技能标签
     */
    @Override
    public SkillTag selectSkillTagById(Long id) {
        return skillTagMapper.selectSkillTagById(id);
    }

    /**
     * 查询技能标签列表
     *
     * @param skillTag 技能标签
     * @return 技能标签集合
     */
    @Override
    public List<SkillTagVo> selectSkillTagList(SkillTag skillTag) {
        return skillTagMapper.selectSkillTagList(skillTag);
    }

    /**
     * 查询所有启用的技能标签
     *
     * @return 技能标签列表
     */
    @Override
    public List<SkillTag> selectAllActiveTags() {
        return skillTagMapper.selectAllActiveTags();
    }

    /**
     * 根据分类查询技能标签
     *
     * @param category 分类
     * @return 技能标签列表
     */
    @Override
    public List<SkillTag> selectSkillTagsByCategory(String category) {
        if (!StringUtils.hasText(category)) {
            throw new ServiceException("分类不能为空");
        }
        return skillTagMapper.selectSkillTagsByCategory(category);
    }

    /**
     * 搜索技能标签
     *
     * @param keyword 关键词
     * @return 技能标签列表
     */
    @Override
    public List<SkillTag> searchSkillTags(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return skillTagMapper.selectAllActiveTags();
        }
        return skillTagMapper.searchSkillTags(keyword);
    }

    /**
     * 新增技能标签
     *
     * @param skillTag 技能标签
     * @return 结果
     */
    @Override
    public int insertSkillTag(SkillTag skillTag) {
        // 检查标签名称是否已存在
        SkillTag existingTag = skillTagMapper.selectSkillTagByName(skillTag.getTagName());
        if (existingTag != null) {
            throw new ServiceException("技能标签名称已存在");
        }

        // 设置默认值
        if (skillTag.getIsActive() == null) {
            skillTag.setIsActive(1); // 默认启用
        }

        return skillTagMapper.insertSkillTag(skillTag);
    }

    /**
     * 修改技能标签
     *
     * @param skillTag 技能标签
     * @return 结果
     */
    @Override
    public int updateSkillTag(SkillTag skillTag) {
        // 检查标签是否存在
        SkillTag existingTag = skillTagMapper.selectSkillTagById(skillTag.getId());
        if (existingTag == null) {
            throw new ServiceException("技能标签不存在");
        }

        // 如果修改了标签名称，检查名称是否重复
        if (StringUtils.hasText(skillTag.getTagName()) &&
                !skillTag.getTagName().equals(existingTag.getTagName())) {
            SkillTag duplicateTag = skillTagMapper.selectSkillTagByName(skillTag.getTagName());
            if (duplicateTag != null && !duplicateTag.getId().equals(skillTag.getId())) {
                throw new ServiceException("技能标签名称已存在");
            }
        }

        return skillTagMapper.updateSkillTag(skillTag);
    }

    /**
     * 批量删除技能标签
     *
     * @param ids 需要删除的技能标签主键集合
     * @return 结果
     */
    @Override
    public int deleteSkillTagByIds(Long[] ids) {
        // 可以添加业务逻辑，如检查是否有用户或项目关联了这些技能标签
        return skillTagMapper.deleteSkillTagByIds(ids);
    }

    /**
     * 删除技能标签信息
     *
     * @param id 技能标签主键
     * @return 结果
     */
    @Override
    public int deleteSkillTagById(Long id) {
        // 可以添加业务逻辑，如检查是否有用户或项目关联了该技能标签
        return skillTagMapper.deleteSkillTagById(id);
    }
}