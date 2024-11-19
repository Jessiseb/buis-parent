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
     */
    void deleteTeamMemberStudentByTeamAndStudent(Long team, Long user);

}
