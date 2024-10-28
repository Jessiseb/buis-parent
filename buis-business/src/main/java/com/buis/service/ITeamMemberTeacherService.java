package com.buis.service;

import java.util.List;
import com.buis.domain.TeamMemberTeacher;
import com.buis.domain.UserInfo;
import com.buis.vo.UserInfoVo;

/**
 * 队伍老师管理Service接口
 * 
 * @author buis
 * @date 2024-10-11
 */
public interface ITeamMemberTeacherService 
{
    /**
     * 查询队伍老师管理
     * 
     * @param id 队伍老师管理主键
     * @return 队伍老师管理
     */
    public TeamMemberTeacher selectTeamMemberTeacherById(Long id);

    /**
     * 查询队伍老师管理列表
     * 
     * @param teamMemberTeacher 队伍老师管理
     * @return 队伍老师管理集合
     */
    public List<TeamMemberTeacher> selectTeamMemberTeacherList(TeamMemberTeacher teamMemberTeacher);

    /**
     * 新增队伍老师管理
     * 
     * @param teamMemberTeacher 队伍老师管理
     * @return 结果
     */
    public int insertTeamMemberTeacher(TeamMemberTeacher teamMemberTeacher);

    /**
     * 修改队伍老师管理
     * 
     * @param teamMemberTeacher 队伍老师管理
     * @return 结果
     */
    public int updateTeamMemberTeacher(TeamMemberTeacher teamMemberTeacher);

    /**
     * 批量删除队伍老师管理
     * 
     * @param ids 需要删除的队伍老师管理主键集合
     * @return 结果
     */
    public int deleteTeamMemberTeacherByIds(Long[] ids);

    /**
     * 删除队伍老师管理信息
     * 
     * @param id 队伍老师管理主键
     * @return 结果
     */
    public int deleteTeamMemberTeacherById(Long id);

    List<TeamMemberTeacher> selectTeamMyInstruct(Long id);

    List<Integer> listTeacherIdByRaceId(Long id);


    List<UserInfoVo> listTeachersByIds(List<Integer> ids);
}
