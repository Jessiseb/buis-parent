package com.buis.service.impl;

import com.buis.common.exception.ServiceException;
import com.buis.mapper.SkillTagMapper;
import com.buis.mapper.TeamSkillMapper;
import com.buis.model.domain.SkillTag;
import com.buis.model.domain.TeamSkill;
import com.buis.model.dto.SkillExtractRequest;
import com.buis.model.dto.SkillExtractResult;
import com.buis.model.vo.TeamSkillVo;
import com.buis.service.IAiService;
import com.buis.service.ITeamSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 团队技能需求服务实现
 *
 *
 */
@Service
public class TeamSkillServiceImpl implements ITeamSkillService {

    @Autowired
    private TeamSkillMapper teamSkillMapper;

    @Autowired
    private SkillTagMapper skillTagMapper;

    @Autowired
    private IAiService aiService;

    /**
     * 查询团队技能需求关联
     *
     * @param id 团队技能需求关联主键
     * @return 团队技能需求关联
     */
    @Override
    public TeamSkill selectTeamSkillById(Long id) {
        return teamSkillMapper.selectTeamSkillById(id);
    }

    /**
     * 查询团队技能需求关联列表
     *
     * @param teamSkill 团队技能需求关联
     * @return 团队技能需求关联集合
     */
    @Override
    public List<TeamSkillVo> selectTeamSkillList(TeamSkill teamSkill) {
        return teamSkillMapper.selectTeamSkillList(teamSkill);
    }

    /**
     * 根据团队ID查询团队技能需求列表
     *
     * @param teamId 团队ID
     * @return 团队技能需求列表
     */
    @Override
    public List<TeamSkillVo> selectTeamSkillsByTeamId(Long teamId) {
        return teamSkillMapper.selectTeamSkillsByTeamId(teamId);
    }

    /**
     * 新增团队技能需求关联
     *
     * @param teamSkill 团队技能需求关联
     * @return 结果
     */
    @Override
    public int insertTeamSkill(TeamSkill teamSkill) {
        // 检查技能标签是否存在
        SkillTag skillTag = skillTagMapper.selectSkillTagById(teamSkill.getSkillTagId());
        if (skillTag == null || skillTag.getIsActive() == 0) {
            throw new ServiceException("技能标签不存在或已禁用");
        }

        // 检查是否已关联
        int exists = teamSkillMapper.existsByTeamIdAndSkillTagId(
                teamSkill.getTeamId(), teamSkill.getSkillTagId());
        if (exists > 0) {
            throw new ServiceException("该技能需求已关联");
        }

        // 设置默认值
        if (teamSkill.getImportanceLevel() == null) {
            teamSkill.setImportanceLevel(2); // 默认重要等级为2（重要）
        }
        if (teamSkill.getCreateTime() == null) {
            teamSkill.setCreateTime(new Date());
        }

        return teamSkillMapper.insertTeamSkill(teamSkill);
    }

    /**
     * 批量新增团队技能需求关联
     *
     * @param teamSkillList 团队技能需求关联列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertTeamSkill(List<TeamSkill> teamSkillList) {
        if (CollectionUtils.isEmpty(teamSkillList)) {
            return 0;
        }

        // 过滤掉已存在的关联
        List<TeamSkill> toInsert = new ArrayList<>();
        Date now = new Date();

        for (TeamSkill teamSkill : teamSkillList) {
            int exists = teamSkillMapper.existsByTeamIdAndSkillTagId(
                    teamSkill.getTeamId(), teamSkill.getSkillTagId());
            if (exists == 0) {
                // 设置默认值
                if (teamSkill.getImportanceLevel() == null) {
                    teamSkill.setImportanceLevel(2);
                }
                teamSkill.setCreateTime(now);
                toInsert.add(teamSkill);
            }
        }

        if (CollectionUtils.isEmpty(toInsert)) {
            return 0;
        }

        return teamSkillMapper.batchInsertTeamSkill(toInsert);
    }

    /**
     * 修改团队技能需求关联
     *
     * @param teamSkill 团队技能需求关联
     * @return 结果
     */
    @Override
    public int updateTeamSkill(TeamSkill teamSkill) {
        // 检查记录是否存在
        TeamSkill existing = teamSkillMapper.selectTeamSkillById(teamSkill.getId());
        if (existing == null) {
            throw new ServiceException("团队技能需求关联不存在");
        }

        return teamSkillMapper.updateTeamSkill(teamSkill);
    }

    /**
     * 删除团队技能需求关联
     *
     * @param id 团队技能需求关联主键
     * @return 结果
     */
    @Override
    public int deleteTeamSkillById(Long id) {
        return teamSkillMapper.deleteTeamSkillById(id);
    }

    /**
     * 根据团队ID和技能标签ID删除团队技能需求关联
     *
     * @param teamId 团队ID
     * @param skillTagId 技能标签ID
     * @return 结果
     */
    @Override
    public int deleteTeamSkillByTeamIdAndSkillTagId(Long teamId, Long skillTagId) {
        return teamSkillMapper.deleteTeamSkillByTeamIdAndSkillTagId(teamId, skillTagId);
    }

    /**
     * 批量删除团队技能需求关联
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    @Override
    public int deleteTeamSkillByIds(Long[] ids) {
        return teamSkillMapper.deleteTeamSkillByIds(ids);
    }

    /**
     * 根据团队ID删除该团队的所有技能需求关联
     *
     * @param teamId 团队ID
     * @return 结果
     */
    @Override
    public int deleteTeamSkillsByTeamId(Long teamId) {
        return teamSkillMapper.deleteTeamSkillsByTeamId(teamId);
    }

    /**
     * 更新团队的技能需求
     *
     * @param teamId 团队ID
     * @param teamSkills 团队技能需求列表（包含重要性等级）
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTeamSkills(Long teamId, List<TeamSkill> teamSkills) {
        if (teamId == null) {
            throw new ServiceException("团队ID不能为空");
        }

        // 删除团队原有的技能关联
        teamSkillMapper.deleteTeamSkillsByTeamId(teamId);

        if (CollectionUtils.isEmpty(teamSkills)) {
            return 0;
        }

        // 设置团队ID和创建时间
        Date now = new Date();
        for (TeamSkill teamSkill : teamSkills) {
            teamSkill.setTeamId(teamId);
            teamSkill.setCreateTime(now);
        }

        // 批量插入
        return batchInsertTeamSkill(teamSkills);
    }

    /**
     * 使用AI提取并关联团队技能需求
     *
     * @param teamId 团队ID
     * @param request 技能提取请求
     * @return 技能提取结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SkillExtractResult extractAndAddTeamSkills(Long teamId, SkillExtractRequest request) {
        if (teamId == null) {
            throw new ServiceException("团队ID不能为空");
        }

        // 设置请求类型
        request.setType("team");

        // 调用AI提取技能
        SkillExtractResult extractResult = aiService.extractSkillsFromText(request);

        // 如果没有匹配到技能，直接返回
        if (CollectionUtils.isEmpty(extractResult.getMatchedSkills())) {
            return extractResult;
        }

        // 构建团队技能需求关联列表（默认重要性等级为2-重要）
        List<TeamSkill> teamSkills = new ArrayList<>();
        Date now = new Date();

        for (SkillTag skillTag : extractResult.getMatchedSkills()) {
            // 检查是否已存在该关联
            int exists = teamSkillMapper.existsByTeamIdAndSkillTagId(teamId, skillTag.getId());
            if (exists == 0) {
                // 使用传统方式创建对象
                TeamSkill teamSkill = new TeamSkill();
                teamSkill.setTeamId(teamId);
                teamSkill.setSkillTagId(skillTag.getId());
                teamSkill.setImportanceLevel(2); // AI提取的技能默认设置为"重要"
                teamSkill.setCreateTime(now);
                teamSkills.add(teamSkill);
            }
        }

        // 批量插入
        if (!CollectionUtils.isEmpty(teamSkills)) {
            batchInsertTeamSkill(teamSkills);
        }

        return extractResult;
    }

    /**
     * 检查团队是否需要某个技能
     *
     * @param teamId 团队ID
     * @param skillTagId 技能标签ID
     * @return 是否需要
     */
    @Override
    public boolean needsSkill(Long teamId, Long skillTagId) {
        int exists = teamSkillMapper.existsByTeamIdAndSkillTagId(teamId, skillTagId);
        return exists > 0;
    }
}
