package com.buis.service.impl;

import com.buis.common.utils.DateUtils;
import com.buis.domain.TeamMemberStudent;
import com.buis.mapper.TeamMemberStudentMapper;
import com.buis.service.ITeamMemberStudentService;
import com.buis.service.IUserInfoService;
import com.buis.vo.TeamMemberStudentVo;
import com.buis.vo.UserInfoVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param id teamId
     * @return
     */
    @Override
    public Long getJoinCountByTeamId(Long id) {
        return teamMemberStudentMapper.getJoinCountByTeamId(id);
    }

    /**
     * 查询队伍内学生列表
     * @param team
     * @return
     */
    @Override
    public List<UserInfoVo> getMemberStudentByTeamId(Long team) {
        List<Long> ids = teamMemberStudentMapper.getMemberStudentByTeamId(team);
        List<UserInfoVo> userInfoVos = null;
        if (ids!=null&& ids.size()>0) {
            userInfoVos = userInfoService.selectUserInfoByIds(ids);
        }
        return userInfoVos;
    }

    /**
     * 根据team和user删除用户与队伍的关联
     * @param team
     * @param user
     */
    @Override
    public void deleteTeamMemberStudentByTeamAndStudent(Long team, Long user) {
           Map<String, Object> params = new HashMap<>();
        params.put("team", team);
        params.put("user", user);
//        teamMemberStudentMapper.deleteTeamMemberStudentByTeamAndStudent(team,user);
        teamMemberStudentMapper.deleteTeamMemberStudentByTeamAndStudent(params);
    }


    /**
     * 判断用户是否已加入
     * @param userId
     * @param raceId
     * @return
     */
    @Override
    public int checkJoin(Long userId, Long raceId, Long teamId) {
        return teamMemberStudentMapper.checkJoin(userId,raceId,teamId);
    }

}
