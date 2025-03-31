package com.buis.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.DateUtils;
import com.buis.domain.TeamMemberStudent;
import com.buis.mapper.TeamMemberStudentMapper;
import com.buis.service.ITeamInfoService;
import com.buis.service.ITeamMemberStudentService;
import com.buis.service.IUserInfoService;
import com.buis.vo.TeamInfoVo;
import com.buis.vo.TeamMemberStudentVo;
import com.buis.vo.UserInfoVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 队伍成员管理Service业务层处理
 *
 * @author buis
 * @date 2024-10-11
 */
@Service
public class TeamMemberStudentServiceImpl implements ITeamMemberStudentService {
    @Resource
    private TeamMemberStudentMapper teamMemberStudentMapper;
    @Resource
    private IUserInfoService userInfoService;
    @Resource
    private ITeamInfoService teamInfoService;

    /**
     * 查询队伍成员管理
     *
     * @param id 队伍成员管理主键
     * @return 队伍成员管理
     */
    @Override
    public TeamMemberStudentVo selectTeamMemberStudentById(Long id) {
        TeamMemberStudentVo teamMemberStudentVo = teamMemberStudentMapper.selectTeamMemberStudentById(id);
        setStudentInfo(teamMemberStudentVo);
        return teamMemberStudentVo;
    }

    /**
     * 查询队伍成员管理列表
     *
     * @param teamMemberStudent 队伍成员管理
     * @return 队伍成员管理
     */
    @Override
    public List<TeamMemberStudentVo> selectTeamMemberStudentList(TeamMemberStudent teamMemberStudent) {
        List<TeamMemberStudentVo> teamMemberStudentVos = teamMemberStudentMapper.selectTeamMemberStudentList(teamMemberStudent);
        for (TeamMemberStudentVo teamMemberStudentVo : teamMemberStudentVos) {
            setStudentInfo(teamMemberStudentVo);
        }
        return teamMemberStudentVos;
    }

    /**
     * 设置学生信息
     *
     * @param teamMemberStudentVo 队伍学生管理对象
     */
    private void setStudentInfo(TeamMemberStudentVo teamMemberStudentVo) {
        if (teamMemberStudentVo != null) {
            teamMemberStudentVo.setStudentInfo(userInfoService.selectUserInfoById(teamMemberStudentVo.getStudent()));
        }
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

    /**
     * 根据teamId获取结对的个数，查成员个数
     *
     * @param id teamId
     * @return
     */
    @Override
    public Long getJoinCountByTeamId(Long id) {
        return teamMemberStudentMapper.getJoinCountByTeamId(id);
    }

    /**
     * 查询队伍内学生列表
     *
     * @param team
     * @return
     */
    @Override
    public List<UserInfoVo> getMemberStudentByTeamId(Long team) {
        //根据队伍id获取到学生ids
        List<Long> ids = teamMemberStudentMapper.getMemberStudentByTeamId(team);
        List<UserInfoVo> userInfoVos = null;
        if (ids != null && ids.size() > 0) {
            //根据ids查询学生信息
            userInfoVos = userInfoService.selectUserInfoByIds(ids);
        }
        return userInfoVos;
    }

    /**
     * 根据team和user删除用户与队伍的关联
     *
     * @param team
     * @param user
     * @return
     */
    @Override
    public int deleteTeamMemberStudentByTeamAndStudent(Long team, Long user) {
        //判断是否为负责任人
        if (!isLeader(team)) {
            throw new ServiceException("您不是队长，无法进行此操作");
        }
        //是队长-->能进行删除
        return teamMemberStudentMapper.deleteTeamMemberStudentByTeamAndStudent(team, user);
    }

    @Override
    public TeamMemberStudent selectTeamMemberStudentByTeamAndStudent(Long user, Long team) {
        return teamMemberStudentMapper.selectTeamMemberStudentByTeamAndStudent(user, team);
    }

    @Override
    public int insertTeamMemberStudentByList(List<TeamMemberStudent> teamMemberStudentList) {
        if (teamMemberStudentList == null || teamMemberStudentList.size() == 0) {
            return 0;
        }
        //循环插入
        try {
            for (TeamMemberStudent teamMemberStudent : teamMemberStudentList) {
                teamMemberStudentMapper.insertTeamMemberStudent(teamMemberStudent);
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 判断用户是否已加入
     *
     * @param userId
     * @param raceId
     * @return
     */
    @Override
    public int getJoinCount(Long userId, Long raceId, Long teamId) {
        return teamMemberStudentMapper.getCountJoin(userId, raceId, teamId);
    }

    /**
     * 查询用户(学生)已经加入多少个队伍
     */
    @Override
    public int getStudentJoinTeamByUserId(Long userId) {
        return teamMemberStudentMapper.getJoinTeamByUserId(userId);
    }

    @Override
    public List<TeamMemberStudent> selectTeamMemberStudentByTeamId(Long team) {
        return teamMemberStudentMapper.selectTeamMemberStudentByTeamId(team);
    }

    @Override
    public List<TeamMemberStudent> selectTeamMemberStudentByUserId(Long userId) {
        return teamMemberStudentMapper.selectTeamMemberStudentByUserId(userId);
    }

    @Override
    public int getRequestCount(Long userId, Long raceId, Long team) {
        return teamMemberStudentMapper.getRequestCount(userId,raceId,team);
    }

    @Override
    public List<TeamMemberStudentVo> selectTeamMemberStudentListVo(TeamMemberStudentVo teamMemberStudentVo) {
        List<TeamMemberStudentVo> teamMemberStudentVos = teamMemberStudentMapper.selectTeamMemberStudentListVo(teamMemberStudentVo);
        for (TeamMemberStudentVo teamMemberStudentVo2 : teamMemberStudentVos) {
            setStudentInfo(teamMemberStudentVo2);
        }
        return teamMemberStudentVos;
    }

    @Override
    public int quitTeam(Long team, Long userId) {
        if (team == null || userId == null) {
            throw new ServiceException("参数错误");
        }
        return teamMemberStudentMapper.quitTeam(team, userId);
    }

    /**
     * 抽出判断是否为负责人的方法
     */
    private boolean isLeader(Long team) {
        //获取登录用户
        String loginIdStr = StpUtil.getLoginId().toString();
        Long loginId;
        try {
            loginId = Long.parseLong(loginIdStr);
        } catch (NumberFormatException e) {
            throw new ServiceException("无效的用户ID格式");
        }
        //根据team查出队长和loginId是否一致
        TeamInfoVo teamInfoVo = teamInfoService.selectTeamInfoById(team);
        Long leader = teamInfoVo.getLeader();
        if (leader.equals(loginId)) {
            return true;
        } else {
            return false;
        }
    }
}
