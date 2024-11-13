package com.buis.service.impl;

import com.buis.common.utils.DateUtils;
import com.buis.domain.TeamInfo;
import com.buis.domain.TeamMemberStudent;
import com.buis.dto.TeamMemberStudentDto;
import com.buis.dto.TeamMemberStudentInvitedDto;
import com.buis.dto.TeamMemberStudentInvitedListDto;
import com.buis.mapper.TeamMemberStudentMapper;
import com.buis.service.ITeamInfoService;
import com.buis.service.ITeamMemberStudentService;
import com.buis.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 队伍成员管理Service业务层处理
 *
 * @author buis
 * @date 2024-10-11
 */
@Service
@Slf4j
public class TeamMemberStudentServiceImpl implements ITeamMemberStudentService {
    @Autowired
    private TeamMemberStudentMapper teamMemberStudentMapper;
    @Autowired
    private ITeamInfoService teamInfoService;

    /**
     * 查询队伍成员管理
     *
     * @param id 队伍成员管理主键
     * @return 队伍成员管理
     */
    @Override
    public TeamMemberStudent selectTeamMemberStudentById(Long id) {
        return teamMemberStudentMapper.selectTeamMemberStudentById(id);
    }

    /**
     * 查询队伍成员管理列表
     *
     * @param teamMemberStudent 队伍成员管理
     * @return 队伍成员管理
     */
    @Override
    public List<TeamMemberStudent> selectTeamMemberStudentList(TeamMemberStudent teamMemberStudent) {
        return teamMemberStudentMapper.selectTeamMemberStudentList(teamMemberStudent);
    }

    /**
     * 新增队伍成员管理
     *
     * @param teamMemberStudent 队伍成员管理
     * @return 结果
     */
    @Override
    public int insertTeamMemberStudent(TeamMemberStudent teamMemberStudent) {
        teamMemberStudent.setCreateTime(DateUtils.getNowDate());
        return teamMemberStudentMapper.insertTeamMemberStudent(teamMemberStudent);
    }

    /**
     * 修改队伍成员管理
     *
     * @param teamMemberStudent 队伍成员管理
     * @return 结果
     */
    @Override
    public int updateTeamMemberStudent(TeamMemberStudent teamMemberStudent) {
        teamMemberStudent.setUpdateTime(DateUtils.getNowDate());
        return teamMemberStudentMapper.updateTeamMemberStudent(teamMemberStudent);
    }

    /**
     * 批量删除队伍成员管理
     *
     * @param ids 需要删除的队伍成员管理主键
     * @return 结果
     */
    @Override
    public int deleteTeamMemberStudentByIds(Long[] ids) {
        return teamMemberStudentMapper.deleteTeamMemberStudentByIds(ids);
    }

    /**
     * 删除队伍成员管理信息
     *
     * @param id 队伍成员管理主键
     * @return 结果
     */
    @Override
    public int deleteTeamMemberStudentById(Long id) {
        return teamMemberStudentMapper.deleteTeamMemberStudentById(id);
    }

    @Override
    public TeamMemberStudentDto selectTeamMemberStudentQueryById(Long id) {
        return teamMemberStudentMapper.selectTeamMemberStudentQueryById(id);
    }

    /**
     * 查询队伍成员管理列表
     *
     * @return 队伍成员管理
     */
    @Override
    public List<UserInfoVo> selectTeamMemberStudentQueryList(Long race, Long team) {
        return teamMemberStudentMapper.selectTeamMemberStudentQueryList(race, team);
    }

    @Override
    public List<TeamMemberStudent> selectTeamMyCreate(Long id) {
        return teamMemberStudentMapper.selectTeamMyCreate(id);
    }

    
}
