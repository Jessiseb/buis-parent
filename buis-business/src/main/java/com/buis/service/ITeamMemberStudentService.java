package com.buis.service;

import com.buis.domain.TeamMemberStudent;
import com.buis.vo.TeamMemberStudentVo;
import com.buis.vo.UserInfoVo;

import java.util.List;


/**
 * 队伍成员管理Service接口
 *
 * @author buis
 * @date 2024-10-11
 */
public interface ITeamMemberStudentService {
    /**
     * 查询队伍成员管理
     *
     * @param id 队伍成员管理主键
     * @return 队伍成员管理
     */
    public TeamMemberStudentVo selectTeamMemberStudentById(Long id);

    /**
     * 查询队伍成员管理列表
     *
     * @param teamMemberStudent 队伍成员管理
     * @return 队伍成员管理集合
     */
    public List<TeamMemberStudentVo> selectTeamMemberStudentList(TeamMemberStudent teamMemberStudent);

    /**
     * 新增队伍成员管理
     *
     * @param teamMemberStudent 队伍成员管理
     * @return 结果
     */
    public int insertTeamMemberStudent(TeamMemberStudent teamMemberStudent);

    /**
     * 修改队伍成员管理
     *
     * @param teamMemberStudent 队伍成员管理
     * @return 结果
     */
    public int updateTeamMemberStudent(TeamMemberStudent teamMemberStudent);

    /**
     * 批量删除队伍成员管理
     *
     * @param ids 需要删除的队伍成员管理主键集合
     * @return 结果
     */
    public int deleteTeamMemberStudentByIds(Long[] ids);

    /**
     * 删除队伍成员管理信息
     *
     * @param id 队伍成员管理主键
     * @return 结果
     */
    public int deleteTeamMemberStudentById(Long id);


    /**
     * 根据teamId获取结对的个数，查成员个数
     * @param id teamId
     * @return
     */
    Long getJoinCountByTeamId(Long id);

    /**
     * 查询队伍内学生列表
     * @param team
     * @return
     */
    List<UserInfoVo> getMemberStudentByTeamId(Long team);


    /**
     * 判断用户是否已加入
     * @param userId
     * @param raceId
     * @return
     */
    int getJoinCount(Long userId, Long raceId, Long team);

    /**
     * 根据team和user删除用户与队伍的关联
     * @param team
     * @param user
     * @return
     */
    int deleteTeamMemberStudentByTeamAndStudent(Long team, Long user);

    /**
     * 根据user和team查出队伍成员信息
     * @param team
     * @param user
     * @return
     */
    TeamMemberStudent selectTeamMemberStudentByTeamAndStudent(Long user, Long team);


     int insertTeamMemberStudentByList(List<TeamMemberStudent> teamMemberStudentList);

    int getStudentJoinTeamByUserId(Long userId);

    List<TeamMemberStudent> selectTeamMemberStudentByTeamId(Long team);

    //根据用户id获取队伍用户信息
    List<TeamMemberStudent> selectTeamMemberStudentByUserId(Long userId);

    int getRequestCount(Long id, Long race, Long team);

    List<TeamMemberStudentVo> selectTeamMemberStudentListVo(TeamMemberStudentVo teamMemberStudentVo);

    /**
     * 成员退出队伍
     * @param team 队伍id
     * @param userId 用户id
     * @return
     */
    int quitTeam(Long team, Long userId);
    //根据队伍id删除中间表
    int deleteTeamMemberStudentByTeam(Long team);

    //查询学生参与的第二课堂活动数量
    int getStudentJoinSecondCountByUserId(Long userId);

    //查询学生参与的大创活动数量
    Integer getStudentJoinInnovationCountByUserId(Long userId);
}
