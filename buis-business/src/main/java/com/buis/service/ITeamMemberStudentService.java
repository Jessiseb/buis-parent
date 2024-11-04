package com.buis.service;

import com.buis.domain.TeamMemberStudent;
import com.buis.dto.TeamMemberStudentDto;
import com.buis.dto.TeamMemberStudentInvitedDto;
import com.buis.dto.TeamMemberStudentInvitedListDto;
import com.buis.vo.UserInfoVo;
import org.apache.ibatis.annotations.Param;

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
    public TeamMemberStudent selectTeamMemberStudentById(Long id);

    /**
     * 查询队伍成员管理列表
     *
     * @param teamMemberStudent 队伍成员管理
     * @return 队伍成员管理集合
     */
    public List<TeamMemberStudent> selectTeamMemberStudentList(TeamMemberStudent teamMemberStudent);

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

    //    查询队伍学生id
    public TeamMemberStudentDto selectTeamMemberStudentQueryById(Long id);

    //    查询队伍学生列表
    List<UserInfoVo> selectTeamMemberStudentQueryList(@Param("race") Long race, @Param("team") Long team);

    /**
     * 查询我创建的队伍
     * @param id
     * @return
     */
    List<TeamMemberStudent> selectTeamMyCreate(Long id);

    /**
     * 队伍学生互选
     * @param teamMemberStudentInvitedDto
     * @return
     */
    String TeamsStudentsSelectedEach(TeamMemberStudentInvitedDto teamMemberStudentInvitedDto);

    /**
     * 取消邀请
     * @param id
     * @return
     */
    String CancelInvited(long id);

    /**
     * 接受or拒绝邀请
     * @param id
     * @param isAgree
     * @return
     */
    String agreeOrreject(long id, int isAgree);

    /**
     * 查询我的邀请列表
     * @param dto
     * @return
     */
    List<TeamMemberStudent> InvitedList(TeamMemberStudentInvitedListDto dto);
}
