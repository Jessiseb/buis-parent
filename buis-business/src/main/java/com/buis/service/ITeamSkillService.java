package com.buis.service;

import com.buis.model.domain.TeamSkill;
import com.buis.model.dto.SkillExtractRequest;
import com.buis.model.dto.SkillExtractResult;
import com.buis.model.vo.TeamSkillVo;

import java.util.List;

/**
 * 团队技能需求服务接口
 *
 *
 */
public interface ITeamSkillService {

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
    int batchInsertTeamSkill(List<TeamSkill> teamSkillList);

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
    int deleteTeamSkillByTeamIdAndSkillTagId(Long teamId, Long skillTagId);

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
     * 更新团队的技能需求
     *
     * @param teamId 团队ID
     * @param teamSkills 团队技能需求列表（包含重要性等级）
     * @return 结果
     */
    int updateTeamSkills(Long teamId, List<TeamSkill> teamSkills);

    /**
     * 使用AI提取并关联团队技能需求
     *
     * @param teamId 团队ID
     * @param request 技能提取请求
     * @return 技能提取结果
     */
    SkillExtractResult extractAndAddTeamSkills(Long teamId, SkillExtractRequest request);

    /**
     * 检查团队是否需要某个技能
     *
     * @param teamId 团队ID
     * @param skillTagId 技能标签ID
     * @return 是否需要
     */
    boolean needsSkill(Long teamId, Long skillTagId);
}