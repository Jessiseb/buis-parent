package com.buis.mapper;

import java.util.List;


import com.buis.domain.TeamInfo;
import com.buis.domain.TeamMemberStudent;
import com.buis.domain.UserInfo;
import com.buis.vo.TeamMemberStudentQueryVo;
import org.apache.ibatis.annotations.Param;

/**
 * 队伍成员管理Mapper接口
 * 
 * @author buis
 * @date 2024-10-11
 */
public interface TeamMemberStudentMapper
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


    public TeamMemberStudentQueryVo selectTeamMemberStudentQueryById(Long id);
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
     * 删除队伍成员管理
     * 
     * @param id 队伍成员管理主键
     * @return 结果
     */
    public int deleteTeamMemberStudentById(Long id);

    /**
     * 批量删除队伍成员管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTeamMemberStudentByIds(Long[] ids);

    List<UserInfo> selectTeamMemberStudentQueryList(@Param("race") Long race,@Param("team") Long team);
}
