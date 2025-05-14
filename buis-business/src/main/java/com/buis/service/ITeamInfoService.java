package com.buis.service;

import java.util.List;

import com.buis.common.core.page.MyPageVo;
import com.buis.domain.TeamInfo;
import com.buis.domain.UserInfo;
import com.buis.dto.TeamMemberDto;
import com.buis.vo.TeamInfoVo;

/**
 * 队伍信息Service接口
 *
 * @author buis
 * @date 2024-10-11
 */
public interface ITeamInfoService {
    /**
     * 查询队伍信息
     *
     * @param id 队伍信息主键
     * @return 队伍信息
     */
    public TeamInfoVo selectTeamInfoById(Long id);

    /**
     * 根据队伍ids查询队伍信息
     *
     * @param ids 队伍信息主键数组
     * @return 队伍信息
     */
    public List<TeamInfoVo> selectTeamInfoByIds(Long[] ids);

    /**
     * 后台查询队伍信息列表
     *
     * @param teamInfo 队伍信息
     * @return 队伍信息集合
     */
    public List<TeamInfoVo> selectTeamInfoList(TeamInfo teamInfo);

    /**
     * 新增队伍信息
     *
     * @param teamInfo 队伍信息
     * @return 结果
     */
    public TeamInfo insertTeamInfo(TeamInfo teamInfo);

    /**
     * 修改队伍信息(前台)
     *
     * @param teamInfo 队伍信息
     * @return 结果
     */
    public int updateTeamInfo(TeamInfo teamInfo);

    /**
     * 修改队伍信息(后台)
     *
     * @param teamInfo 队伍信息
     * @return 结果
     */
    public int updateTeamInfoBackend(TeamInfo teamInfo);



    /**
     * 批量删除队伍信息
     *
     * @param ids 需要删除的队伍信息主键集合
     * @return 结果
     */
    public int deleteTeamInfoByIds(Long[] ids);

    /**
     * 删除队伍信息
     *
     * @param id 队伍信息主键
     * @return 结果
     */
    public int deleteTeamInfoById(Long id);

    /**
     * 根据用户id和比赛id删除队伍信息
     *
     * @param race
     * @param userId
     */
    void deleteTeamInfoByRaceIdAndUserId(Long race, Long userId);

    /**
     * 判断用户是否已加入
     *
     * @param userId
     * @param raceId
     * @return
     */
    int getCountJoin(Long userId, Long raceId, Long team);

    /**
     * 根据用户id查询用户创建的队伍
     *
     * @param userId
     * @return
     */
    List<TeamInfoVo> selectTeamInfoByUserId(Long userId);

    int joinTeamById(TeamMemberDto teamMemberDto, UserInfo loginUser);

    int quitTeam(Long team, Long userId);

    /**
     *
     * @param userId
     * @param teamId
     */
    int agreeJoinTeam(Long userId,Long teamId);

    /**
     * 根据队伍id查询队伍信息(缓存)
     * @param teamInfo
     * @return
     */
    public MyPageVo<List<TeamInfoVo>> selectTeamInfoListFromRedis(TeamInfo teamInfo);

    /**
     * 检查是否带有查询条件
     * @param queryTeam
     * @return
     */
    public boolean hasSearchCondition(TeamInfo queryTeam);

    int transfer(Long team, Long user);


    int updateTeamInfoWithLeader(TeamInfoVo teamInfoVo);
    /**
     * 根据队伍负责人姓名查出比赛队伍信息
     * @param name
     * @return
     */
    List<TeamInfo> selectTeamInfoByName(String name);


    String getTeacherInstructCountFromContest(Long userId);

    String getTeacherInstructCountFromInnovation(Long userId);
    List<TeamInfoVo> selectMyPublishedTeams(Long userId);

    int refuseJoinTeam(Long userId, Long teamId);

    List<TeamInfo> selectAllTeamInfoById(Long userId);

    /**
     * 更新队伍信息缓存
     * @param teamInfo
     */
    void updateTeamInfoCache(TeamInfo teamInfo);
}
