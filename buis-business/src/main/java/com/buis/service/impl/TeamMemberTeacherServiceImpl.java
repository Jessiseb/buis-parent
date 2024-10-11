package com.buis.service.impl;

import java.util.List;
import com.buis.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.TeamMemberTeacherMapper;
import com.buis.domain.TeamMemberTeacher;
import com.buis.service.ITeamMemberTeacherService;

/**
 * 队伍老师管理Service业务层处理
 * 
 * @author buis
 * @date 2024-10-11
 */
@Service
public class TeamMemberTeacherServiceImpl implements ITeamMemberTeacherService 
{
    @Autowired
    private TeamMemberTeacherMapper teamMemberTeacherMapper;

    /**
     * 查询队伍老师管理
     * 
     * @param id 队伍老师管理主键
     * @return 队伍老师管理
     */
    @Override
    public TeamMemberTeacher selectTeamMemberTeacherById(Long id)
    {
        return teamMemberTeacherMapper.selectTeamMemberTeacherById(id);
    }

    /**
     * 查询队伍老师管理列表
     * 
     * @param teamMemberTeacher 队伍老师管理
     * @return 队伍老师管理
     */
    @Override
    public List<TeamMemberTeacher> selectTeamMemberTeacherList(TeamMemberTeacher teamMemberTeacher)
    {
        return teamMemberTeacherMapper.selectTeamMemberTeacherList(teamMemberTeacher);
    }

    /**
     * 新增队伍老师管理
     * 
     * @param teamMemberTeacher 队伍老师管理
     * @return 结果
     */
    @Override
    public int insertTeamMemberTeacher(TeamMemberTeacher teamMemberTeacher)
    {
        teamMemberTeacher.setCreateTime(DateUtils.getNowDate());
        return teamMemberTeacherMapper.insertTeamMemberTeacher(teamMemberTeacher);
    }

    /**
     * 修改队伍老师管理
     * 
     * @param teamMemberTeacher 队伍老师管理
     * @return 结果
     */
    @Override
    public int updateTeamMemberTeacher(TeamMemberTeacher teamMemberTeacher)
    {
        teamMemberTeacher.setUpdateTime(DateUtils.getNowDate());
        return teamMemberTeacherMapper.updateTeamMemberTeacher(teamMemberTeacher);
    }

    /**
     * 批量删除队伍老师管理
     * 
     * @param ids 需要删除的队伍老师管理主键
     * @return 结果
     */
    @Override
    public int deleteTeamMemberTeacherByIds(Long[] ids)
    {
        return teamMemberTeacherMapper.deleteTeamMemberTeacherByIds(ids);
    }

    /**
     * 删除队伍老师管理信息
     * 
     * @param id 队伍老师管理主键
     * @return 结果
     */
    @Override
    public int deleteTeamMemberTeacherById(Long id)
    {
        return teamMemberTeacherMapper.deleteTeamMemberTeacherById(id);
    }
}
