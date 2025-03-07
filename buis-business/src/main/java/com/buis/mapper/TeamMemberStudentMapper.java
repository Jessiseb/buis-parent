package com.buis.mapper;

import com.buis.domain.TeamMemberStudent;
import com.buis.vo.TeamMemberStudentVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 队伍成员管理Mapper接口
 *
 * @author buis
 * @date 2024-10-11
 */
public interface TeamMemberStudentMapper {
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

    Long getJoinCountByTeamId(Long id);
    /**
     * 查询队伍内学生列表
     * @param team
     * @return
     */
    List<Long> getMemberStudentByTeamId(Long team);


    /**
     * 判断用户是否已加入
     * @param userId
     * @param raceId
     * @return
     */
    int getCountJoin(@Param("userId") Long userId, @Param("raceId") Long raceId, @Param("team") Long team);

    void deleteTeamMemberStudentByTeamAndStudent(Long team, Long user);

    int insertTeamMemberStudentByList(List<TeamMemberStudent> teamMemberStudentList);

    int getJoinTeamByUserId(Long userId);
}
