package com.buis.mapper;

import java.util.List;
import com.buis.domain.TeamMemberTeacher;
import com.buis.vo.TeamMemberTeacherVo;
import org.apache.ibatis.annotations.Param;

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
    public TeamMemberTeacherVo selectTeamMemberTeacherById(Long id);

    /**
     * 查询队伍老师管理列表
     * 
     * @param teamMemberTeacher 队伍老师管理
     * @return 队伍老师管理集合
     */
    public List<TeamMemberTeacherVo> selectTeamMemberTeacherList(TeamMemberTeacher teamMemberTeacher);

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

    Long getCount(Long id);


    List<TeamMemberTeacher> selectTeamMyInstruct(Long id);

    /**
     * 查询老师指导数量
     * @param id 老师id
     * @return 数量
     */
    Long getGuideCount(Long id);
    /**
     * 根据teamId获取结对的个数，查老师个数
     * @param id teamId
     * @return
     */
    Long getJoinCountByTeamId(Long id);
    /**
     * 根据比赛id获取指导老师列表
     *
     * @param raceId
     * @return
     */
    List<Long> getTeachersByRaceId(Long raceId);

    Long[] selectJoinTeamByTeacherId(Long userId);

    /**
     * 判断用户是否已加入
     * @param userId
     * @param raceId
     * @return
     */
    int getCountJoin(@Param("userId") Long userId, @Param("raceId") Long raceId,@Param("team") Long team);

    /**
     * 根据team和user删除邀请
     * @param team
     * @param user
     */
    void deleteTeamMemberTeacherByTeamAndTeacher(Long team, Long user);
}
