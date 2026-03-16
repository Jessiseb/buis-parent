package com.buis.mapper;

import com.buis.model.domain.TeamSkill;
import com.buis.model.vo.TeamSkillVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 团队技能需求关联Mapper接口
 *
 *
 */
@Repository
public interface TeamSkillMapper {

    /**
     * 查询团队技能需求关联
     *
     * @param id 团队技能需求关联主键
     * @return 团队技能需求关联
     */
    TeamSkill selectTeamSkillById(Long id);

    /**
     * 查询团队技能需求关联列表
     *
     * @param teamSkill 团队技能需求关联
     * @return 团队技能需求关联集合
     */
    List<TeamSkillVo> selectTeamSkillList(TeamSkill teamSkill);

    /**
     * 根据团队ID查询团队技能需求列表
     *
     * @param teamId 团队ID
     * @return 团队技能需求列表
     */
    List<TeamSkillVo> selectTeamSkillsByTeamId(Long teamId);

    /**
     * 根据技能标签ID查询团队技能需求关联
     *
     * @param skillTagId 技能标签ID
     * @return 团队技能需求关联列表
     */
    List<TeamSkill> selectTeamSkillsBySkillTagId(Long skillTagId);

    /**
     * 根据团队ID列表查询团队技能需求
     *
     * @param teamIds 团队ID列表
     * @return 团队技能需求列表
     */
    List<TeamSkill> selectTeamSkillsByTeamIds(@Param("teamIds") List<Long> teamIds);

    /**
     * 新增团队技能需求关联
     *
     * @param teamSkill 团队技能需求关联
     * @return 结果
     */
    int insertTeamSkill(TeamSkill teamSkill);

    /**
     * 批量新增团队技能需求关联
     *
     * @param teamSkillList 团队技能需求关联列表
     * @return 结果
     */
    int batchInsertTeamSkill(@Param("list") List<TeamSkill> teamSkillList);

    /**
     * 修改团队技能需求关联
     *
     * @param teamSkill 团队技能需求关联
     * @return 结果
     */
    int updateTeamSkill(TeamSkill teamSkill);

    /**
     * 删除团队技能需求关联
     *
     * @param id 团队技能需求关联主键
     * @return 结果
     */
    int deleteTeamSkillById(Long id);

    /**
     * 根据团队ID和技能标签ID删除团队技能需求关联
     *
     * @param teamId 团队ID
     * @param skillTagId 技能标签ID
     * @return 结果
     */
    int deleteTeamSkillByTeamIdAndSkillTagId(@Param("teamId") Long teamId, @Param("skillTagId") Long skillTagId);

    /**
     * 批量删除团队技能需求关联
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTeamSkillByIds(Long[] ids);

    /**
     * 根据团队ID删除该团队的所有技能需求关联
     *
     * @param teamId 团队ID
     * @return 结果
     */
    int deleteTeamSkillsByTeamId(Long teamId);

    /**
     * 统计团队需求的技能数量
     *
     * @param teamId 团队ID
     * @return 数量
     */
    int countTeamSkills(Long teamId);

    /**
     * 检查团队是否已需求某个技能
     *
     * @param teamId 团队ID
     * @param skillTagId 技能标签ID
     * @return 存在返回1，否则返回0
     */
    int existsByTeamIdAndSkillTagId(@Param("teamId") Long teamId, @Param("skillTagId") Long skillTagId);

    /**
     * 根据团队ID和技能重要性等级查询团队技能需求
     *
     * @param teamId 团队ID
     * @param importanceLevel 重要性等级
     * @return 团队技能需求列表
     */
    List<TeamSkillVo> selectTeamSkillsByTeamIdAndLevel(@Param("teamId") Long teamId, @Param("importanceLevel") Integer importanceLevel);

    /**
     * 根据技能标签ID列表查询团队
     *
     * @param skillTagIds 技能标签ID列表
     * @return 团队技能需求列表
     */
    List<TeamSkill> selectTeamsBySkillTags(@Param("skillTagIds") List<Long> skillTagIds);

    /**
     * 查询团队技能需求及其重要性
     *
     * @param teamId 团队ID
     * @return 团队技能需求列表
     */
    List<TeamSkillVo> selectTeamSkillsWithImportance(Long teamId);
}