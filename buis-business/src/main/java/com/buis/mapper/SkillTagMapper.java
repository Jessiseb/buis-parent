package com.buis.mapper;

import com.buis.model.domain.SkillTag;
import com.buis.model.vo.StudentSkillStatVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 技能标签主Mapper接口
 *
 * @author buis
 * @date 2026-03-11
 */
@Mapper
public interface SkillTagMapper {
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
     * 删除技能标签主
     *
     * @param id 技能标签主主键
     * @return 结果
     */
    public int deleteSkillTagById(Long id);

    /**
     * 批量删除技能标签主
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSkillTagByIds(Long[] ids);



    public List<SkillTag> selectAllActiveTags();

    /**
     * 获取热门技能统计（前7条）
     * 连接skill_tag和user_skill表统计学生掌握情况
     * @return 学生技能统计列表
     */
    List<StudentSkillStatVo> selectTop7StudentSkillStats();

}
