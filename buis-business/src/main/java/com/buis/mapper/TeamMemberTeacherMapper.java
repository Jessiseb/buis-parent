package com.buis.mapper;

import java.util.List;
import com.buis.domain.TeamMemberTeacher;

/**
 * 队伍老师管理Mapper接口
 * 
 * @author buis
 * @date 2024-10-11
 */
public interface TeamMemberTeacherMapper 
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
     * 删除队伍老师管理
     * 
     * @param id 队伍老师管理主键
     * @return 结果
     */
    public int deleteTeamMemberTeacherById(Long id);

    /**
     * 批量删除队伍老师管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTeamMemberTeacherByIds(Long[] ids);
}
