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

    /**
     * 队伍学生互选
     * @param dto
     * @return
     */
    @Override
    public String TeamsStudentsSelectedEach(TeamMemberStudentInvitedDto dto) {
        //查询队伍信息
        TeamInfo teamInfo = teamInfoService.selectTeamInfoById(dto.getTeam());
        if (teamInfo == null) {
            return "队伍不存在";
        }

        //查询队伍人数
        TeamMemberStudent teamMemberStudent = new TeamMemberStudent();
        teamMemberStudent.setTeam(dto.getTeam());
        teamMemberStudent.setIsChoosed((long)1);
        teamMemberStudent.setIsDeleted(0);
        int teamMember = selectTeamMemberStudentList(teamMemberStudent).size();
        if (teamMember >= teamInfo.getCapacity()) {
            return "队伍人数已满";
        }

        //查询是否已申请
        teamMemberStudent = new TeamMemberStudent();
        teamMemberStudent.setTeam(dto.getTeam());
        teamMemberStudent.setStudent(dto.getStudentId());
        teamMemberStudent.setIsDeleted(0);
        List<TeamMemberStudent> teamMemberStudents = selectTeamMemberStudentList(teamMemberStudent);
        if (teamMemberStudents.size() > 0) {
            return "已申请";
        }

        //新增
        teamMemberStudent = new TeamMemberStudent();
        teamMemberStudent.setTeam(dto.getTeam());
        teamMemberStudent.setStudent(dto.getStudentId());
        teamMemberStudent.setDecider(dto.getDecider());
        teamMemberStudent.setRace(teamInfo.getRace());
        teamMemberStudent.setIsChoosed((long)0);
        teamMemberStudent.setReason(dto.getReason());
        teamMemberStudent.setIsDeleted(0);
        teamMemberStudent.setCreateTime(DateUtils.getNowDate());
        teamMemberStudent.setUpdateTime(DateUtils.getNowDate());

        int i = teamMemberStudentMapper.insertTeamMemberStudent(teamMemberStudent);
        if (i == 1) {
            return "申请成功";
        } else {
            return "申请失败";
        }
    }

    /**
     * 取消邀请
     *
     * @param id
     * @return
     */
    @Override
    public String CancelInvited(long id) {
        TeamMemberStudent teamMemberStudent = selectTeamMemberStudentById(id);
        if (teamMemberStudent == null) {
            return "数据不存在";
        }

        teamMemberStudent.setIsDeleted(1);
        teamMemberStudent.setUpdateTime(DateUtils.getNowDate());

        int i = teamMemberStudentMapper.updateTeamMemberStudent(teamMemberStudent);
        if (i == 1) {
            return "取消成功";
        } else {
            return "取消失败";
        }
    }

    /**
     * 接受or拒绝邀请
     *
     * @param id
     * @param isAgree
     * @return
     */
    @Override
    public String agreeOrreject(long id, int isAgree) {
        TeamMemberStudent teamMemberStudent = selectTeamMemberStudentById(id);
        if (teamMemberStudent == null) {
            return "数据不存在";
        }

        if (isAgree == 1) {
            //同意
            teamMemberStudent.setIsChoosed((long)1);
        } else {
            //拒绝
            teamMemberStudent.setIsChoosed((long)2);
        }
        teamMemberStudent.setUpdateTime(DateUtils.getNowDate());

        int i = teamMemberStudentMapper.updateTeamMemberStudent(teamMemberStudent);
        if (i == 1) {
            return "操作成功";
        } else {
            return "操作失败";
        }
    }

    /**
     * 查询我的邀请列表
     *
     * @param dto
     * @return
     */
    @Override
    public List<TeamMemberStudent> InvitedList(TeamMemberStudentInvitedListDto dto) {
        Long id = dto.getId();

        TeamMemberStudent teamMemberStudent = new TeamMemberStudent();
        List<TeamMemberStudent> list = new ArrayList<>();

        //添加条件
        if(dto.getInitiated() != null && dto.getInitiated() == 0) {
            teamMemberStudent.setDecider((long)1);
            teamMemberStudent.setStudent(id);
        } else if (dto.getInitiated() != null && dto.getInitiated() == 1) {
            teamMemberStudent.setDecider((long)0);
            teamMemberStudent.setStudent(id);
        } else {
            teamMemberStudent.setStudent(id);
        }

        teamMemberStudent.setIsChoosed(dto.getIsChoosed());

        //查询
        List<TeamMemberStudent> list1 = selectTeamMemberStudentList(teamMemberStudent);

        //判断是否为队长
        teamMemberStudent = new TeamMemberStudent();
        TeamInfo teamInfo = new TeamInfo();
        teamInfo.setLeader(id);
        List<TeamInfo> teamInfos = teamInfoService.selectTeamInfoList(teamInfo);

        if (teamInfos.size() == 0) {
            //不是队长直接返回
            list.addAll(list1);
            return list;
        } else {
            list.addAll(list1);
            for (TeamInfo teamInfo1 : teamInfos) {
                if(dto.getInitiated() != null && dto.getInitiated() == 0) {
                    teamMemberStudent.setDecider((long)0);
                    teamMemberStudent.setTeam(teamInfo1.getId());
                } else if (dto.getInitiated() != null && dto.getInitiated() == 1) {
                    teamMemberStudent.setDecider((long)1);
                    teamMemberStudent.setTeam(teamInfo1.getId());
                } else {
                    teamMemberStudent.setTeam(teamInfo1.getId());
                }

                teamMemberStudent.setIsChoosed(dto.getIsChoosed());

                List<TeamMemberStudent> list2 = selectTeamMemberStudentList(teamMemberStudent);
                list.addAll(list2);
            }
            return list;
        }

    }
}
