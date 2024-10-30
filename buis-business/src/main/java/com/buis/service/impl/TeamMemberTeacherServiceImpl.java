package com.buis.service.impl;

import com.buis.common.utils.DateUtils;
import com.buis.domain.TeamMemberTeacher;
import com.buis.domain.UserInfo;
import com.buis.mapper.TeamMemberTeacherMapper;
import com.buis.mapper.UserInfoMapper;
import com.buis.service.ITeamMemberTeacherService;
import com.buis.service.IUserInfoService;
import com.buis.vo.UserInfoVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 队伍老师管理Service业务层处理
 * 
 * @author buis
 * @date 2024-10-11
 */
@Service
public class TeamMemberTeacherServiceImpl implements ITeamMemberTeacherService {
    @Resource
    private TeamMemberTeacherMapper teamMemberTeacherMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private IUserInfoService userInfoService;

    /**
     * 查询队伍老师管理
     *
     * @param id 队伍老师管理主键
     * @return 队伍老师管理
     */
    @Override
    public TeamMemberTeacher selectTeamMemberTeacherById(Long id) {
        return teamMemberTeacherMapper.selectTeamMemberTeacherById(id);
    }

    /**
     * 查询队伍老师管理列表
     *
     * @param teamMemberTeacher 队伍老师管理
     * @return 队伍老师管理
     */
    @Override
    public List<TeamMemberTeacher> selectTeamMemberTeacherList(TeamMemberTeacher teamMemberTeacher) {
        return teamMemberTeacherMapper.selectTeamMemberTeacherList(teamMemberTeacher);
    }

    /**
     * 新增队伍老师管理
     *
     * @param teamMemberTeacher 队伍老师管理
     * @return 结果
     */
    @Override
    public int insertTeamMemberTeacher(TeamMemberTeacher teamMemberTeacher) {
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
    public int updateTeamMemberTeacher(TeamMemberTeacher teamMemberTeacher) {
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
    public int deleteTeamMemberTeacherByIds(Long[] ids) {
        return teamMemberTeacherMapper.deleteTeamMemberTeacherByIds(ids);
    }

    /**
     * 删除队伍老师管理信息
     *
     * @param id 队伍老师管理主键
     * @return 结果
     */
    @Override
    public int deleteTeamMemberTeacherById(Long id) {
        return teamMemberTeacherMapper.deleteTeamMemberTeacherById(id);
    }

    @Override
    public List<TeamMemberTeacher> selectTeamMyInstruct(Long id) {
        return teamMemberTeacherMapper.selectTeamMyInstruct(id);
    }

    /**
     * 根据比赛id查出老师id
     *
     * @param id
     * @return
     */
    @Override
    public List<Integer> listTeacherIdByRaceId(Long id) {
        return teamMemberTeacherMapper.listTeacherIdByRaceId(id);
    }

    @Override
    public List<UserInfoVo> listTeachersByIds(List<Integer> ids) {
        List<UserInfo> userInfos = userInfoMapper.listTeachersByIds(ids);
        return userInfos.stream()
                .flatMap(userInfo -> userInfoService.selectUserInfoList(userInfo).stream())
                .collect(Collectors.toList());
    }
}