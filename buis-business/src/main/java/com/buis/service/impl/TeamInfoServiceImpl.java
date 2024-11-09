package com.buis.service.impl;

import java.util.*;

import cn.hutool.core.bean.BeanUtil;
import com.buis.common.utils.DateUtils;
import com.buis.service.ITeamMemberStudentService;
import com.buis.service.ITeamMemberTeacherService;
import com.buis.service.IUserInfoService;
import com.buis.vo.TeamInfoVo;
import com.buis.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.TeamInfoMapper;
import com.buis.domain.TeamInfo;
import com.buis.service.ITeamInfoService;

import javax.annotation.Resource;

/**
 * 队伍信息Service业务层处理
 * 
 * @author buis
 * @date 2024-10-11
 */
@Service
public class TeamInfoServiceImpl implements ITeamInfoService
{
    @Autowired
    private TeamInfoMapper teamInfoMapper;

    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private ITeamMemberStudentService teamStudentService;
    @Autowired
    private ITeamMemberTeacherService teamTeacherService;


    /**
     * 新增队伍信息
     * 
     * @param teamInfo 队伍信息
     * @return 结果
     */
    @Override
    public int insertTeamInfo(TeamInfo teamInfo)
    {
        teamInfo.setCreateTime(DateUtils.getNowDate());
        return teamInfoMapper.insertTeamInfo(teamInfo);
    }

    /**
     * 修改队伍信息
     * 
     * @param teamInfo 队伍信息
     * @return 结果
     */
    @Override
    public int updateTeamInfo(TeamInfo teamInfo)
    {
        teamInfo.setUpdateTime(DateUtils.getNowDate());
        return teamInfoMapper.updateTeamInfo(teamInfo);
    }

    /**
     * 批量删除队伍信息
     * 
     * @param ids 需要删除的队伍信息主键
     * @return 结果
     */
    @Override
    public int deleteTeamInfoByIds(Long[] ids)
    {
        return teamInfoMapper.deleteTeamInfoByIds(ids);
    }

    /**
     * 删除队伍信息信息
     * 
     * @param id 队伍信息主键
     * @return 结果
     */
    @Override
    public int deleteTeamInfoById(Long id)
    {
        return teamInfoMapper.deleteTeamInfoById(id);
    }

    @Override
    public void deleteTeamInfoByUserIdAndRaceId(Long race, Long userId) {
        teamInfoMapper.deleteTeamInfoByUserIdAndRaceId(race, userId);
    }


    /**
     * 查询队伍信息列表 Vo
     * @param teamInfo
     * @return
     */
    @Override
    public List<TeamInfoVo> selectTeamInfoList(TeamInfo teamInfo) {
        List<TeamInfoVo> results = new ArrayList<>();
        List<TeamInfo> teamInfoList = teamInfoMapper.selectTeamInfoList(teamInfo);
        if (teamInfoList == null || teamInfoList.isEmpty()) {
            return results; // 返回空列表
        }
        for (TeamInfo team : teamInfoList) {
            results.add(createTeamInfoVo(team));
        }
        return results;
    }

    /**
     * 查询队伍信息
     *
     * @param id 队伍信息主键
     * @return 队伍信息
     */
    @Override
    public TeamInfoVo selectTeamInfoById(Long id) {
        TeamInfo team = teamInfoMapper.selectTeamInfoById(id);
        return team != null ? createTeamInfoVo(team) : new TeamInfoVo();
    }

    /**
     * 创建 TeamInfoVo 对象
     *
     * @param team 队伍信息
     * @return TeamInfoVo
     */
    private TeamInfoVo createTeamInfoVo(TeamInfo team) {
        TeamInfoVo teamInfoVo = new TeamInfoVo();
        BeanUtil.copyProperties(team, teamInfoVo);
        UserInfoVo userInfoVo = userInfoService.selectUserInfoById(team.getLeader());
        Long studentCount = teamStudentService.getJoinCountByTeamId(team.getId());
        Long teacherCount = teamTeacherService.getJoinCountByTeamId(team.getId());
        if (userInfoVo!=null) { // 判断队长是学生还是老师，算上人数
            if (userInfoVo.getIsTeacher() == 1){
                teacherCount++;
            }else {
                studentCount++;
            }
        }
        // 获取队长信息
        teamInfoVo.setLeaderInfo(userInfoVo);
        // 获取学生人数
        teamInfoVo.setStudentCount(studentCount);
        // 获取老师人数
        teamInfoVo.setTeacherCount(teacherCount);
        return teamInfoVo;
    }

    @Override
    public List<TeamInfoVo> selectTeamInfoByIds(Long[] ids) {
        return teamInfoMapper.selectTeamInfoByIds(ids);
    }
}
