package com.buis.mapper;

import java.util.List;


<<<<<<< HEAD
import java.util.Map;

import com.buis.domain.TeamMemberStudent;
import com.buis.domain.UserInfo;
import com.buis.dto.TeamMemberStudentDto;
=======
import com.buis.domain.TeamMemberStudent;
import com.buis.vo.TeamMemberStudentQueryVo;
import com.buis.vo.UserInfoVo;
>>>>>>> 3b679592e9155bb2c435ee603a8acf26f8d42ff8
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


    public TeamMemberStudentDto selectTeamMemberStudentQueryById(Long id);
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

<<<<<<< HEAD
    int getCount(Long id);

    Map<String, Object> getCountAndStudents(Long id);

//    Map<String, Object> getCountAndStudents(Long id);


    List<UserInfo> selectTeamMemberStudentQueryList(@Param("race") Long race,@Param("team") Long team);

    List<TeamMemberStudent> selectTeamMyCreate(Long id);
=======
    List<UserInfoVo> selectTeamMemberStudentQueryList(@Param("race") Long race, @Param("team") Long team);
>>>>>>> 3b679592e9155bb2c435ee603a8acf26f8d42ff8
}
