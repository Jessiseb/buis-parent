package com.buis.service;

import java.util.List;
import com.buis.domain.TeamMemberStudent;
import com.buis.domain.UserInfo;
import com.buis.dto.TeamMemberStudentQueryDTO;
import com.buis.vo.TeamMemberStudentQueryVo;

/**
 * 队伍成员管理Service接口
 * 
 * @author buis
 * @date 2024-10-11
 */
public interface ITeamMemberStudentService 
{
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
    public TeamMemberStudentQueryVo selectTeamMemberStudentQueryById(Long id);
    //    查询队伍学生列表
    List<UserInfo> selectTeamMemberStudentQueryList(Long race, Long team);
}
