package com.buis.service.impl;

import com.buis.common.utils.DateUtils;
import com.buis.domain.TeamMemberTeacher;
import com.buis.mapper.TeamMemberTeacherMapper;
import com.buis.service.IRaceInfoService;
import com.buis.service.ITeamInfoService;
import com.buis.service.ITeamMemberTeacherService;
import com.buis.service.IUserInfoService;
import com.buis.vo.TeamInfoVo;
import com.buis.vo.TeamMemberTeacherVo;
import com.buis.vo.UserInfoVo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 队伍老师管理Service业务层处理
 *
 * @author buis
 * @date 2024-10-11
 */
@Service
public class TeamMemberTeacherServiceImpl implements ITeamMemberTeacherService {
    @Resource
    private TeamMemberTeacherMapper teamMemberTeacherMapper;
    @Resource
    private IUserInfoService userInfoService;
    @Resource
    private ITeamInfoService teamInfoService;
    @Resource
     private IRaceInfoService iRaceInfoService;

    /**
     * 查询队伍老师管理
     *
     * @param id 队伍老师管理主键
     * @return 队伍老师管理
     */
    @Override
    public TeamMemberTeacherVo selectTeamMemberTeacherById(Long id) {
        TeamMemberTeacherVo teamMemberTeacherVo = teamMemberTeacherMapper.selectTeamMemberTeacherById(id);
        setTeacherInfo(teamMemberTeacherVo);
        setTeamInfo(teamMemberTeacherVo);
        return teamMemberTeacherVo;
    }

    /**
     * 查询队伍老师管理列表
     *
     * @param teamMemberTeacher 队伍老师管理
     * @return 队伍老师管理列表
     */
    @Override
    public List<TeamMemberTeacherVo> selectTeamMemberTeacherList(TeamMemberTeacher teamMemberTeacher) {
        List<TeamMemberTeacherVo> teamMemberTeacherVos = teamMemberTeacherMapper.selectTeamMemberTeacherList(teamMemberTeacher);
        for (TeamMemberTeacherVo teamMemberTeacherVo : teamMemberTeacherVos) {
            setTeacherInfo(teamMemberTeacherVo);
            setTeamInfo(teamMemberTeacherVo);
        }
        return teamMemberTeacherVos;
    }

    /**
     * 设置老师信息
     *
     * @param teamMemberTeacherVo 队伍老师管理对象
     */
    private void setTeacherInfo(TeamMemberTeacherVo teamMemberTeacherVo) {
        if (teamMemberTeacherVo != null) {
            teamMemberTeacherVo.setTeacherInfo(userInfoService.selectUserInfoById(teamMemberTeacherVo.getTeacher()));
        }
    }
/**
 * 设置队伍信息
 */
private void setTeamInfo(TeamMemberTeacherVo teamMemberTeacherVo) {
    if (teamMemberTeacherVo != null) {
        teamMemberTeacherVo.setTeamInfo(teamInfoService.selectTeamInfoById(teamMemberTeacherVo.getTeam()));
    }
}

    /**
     * 新增队伍老师管理
     *
     * @param teamMemberTeacher 队伍老师管理
     * @return 结果
     */
    @Override
    public int insertTeamMemberTeacher(TeamMemberTeacher teamMemberTeacher) {
        iRaceInfoService.checkGuideLimit(teamMemberTeacher.getRace(),teamMemberTeacher.getTeacher());
        //判断是否重复指导
        int joinCount = getJoinCount(teamMemberTeacher.getTeacher(), teamMemberTeacher.getRace(), teamMemberTeacher.getTeam());
        if (joinCount >= 1) {
            throw new RuntimeException("该老师已指导该队伍");
        }
        teamMemberTeacher.setCreateTime(DateUtils.getNowDate());
        return teamMemberTeacherMapper.insertTeamMemberTeacher(teamMemberTeacher);
    }

    /**
     * 修改队伍老师管理
     *
     * @param teamMemberTeacher 队伍老师管理
     * @return 结果
     */
    @Override
    public int updateTeamMemberTeacher(TeamMemberTeacher teamMemberTeacher) {
        TeamMemberTeacherVo teacherById = teamMemberTeacherMapper.selectTeamMemberTeacherById(teamMemberTeacher.getId());
        if(teacherById!=null && teacherById.getTeacher() != teamMemberTeacher.getTeacher()) {
            Long isChoosed = teamMemberTeacher.getIsChoosed();
            if (isChoosed!=null&&isChoosed ==1){
                iRaceInfoService.checkGuideLimit(teamMemberTeacher.getRace(),teamMemberTeacher.getTeacher());
            }
        }
        //判断是否重复指导
        int joinCount = getJoinCount(teamMemberTeacher.getTeacher(), teamMemberTeacher.getRace(), teamMemberTeacher.getTeam());
        if (joinCount >= 1) {
            throw new RuntimeException("该老师已指导该队伍");
        }
        teamMemberTeacher.setUpdateTime(DateUtils.getNowDate());
        return teamMemberTeacherMapper.updateTeamMemberTeacher(teamMemberTeacher);
    }


    /**
     * 批量删除队伍老师管理
     *
     * @param ids 需要删除的队伍老师管理主键
     * @return 结果
     */
    @Override
    public int deleteTeamMemberTeacherByIds(Long[] ids) {
        return teamMemberTeacherMapper.deleteTeamMemberTeacherByIds(ids);
    }

    /**
     * 删除队伍老师管理信息
     *
     * @param id 队伍老师管理主键
     * @return 结果
     */
    @Override
    public int deleteTeamMemberTeacherById(Long id) {
        return teamMemberTeacherMapper.deleteTeamMemberTeacherById(id);
    }

    /**
     * 查询老师指导数量
     *
     * @param id 老师id
     * @return 数量
     */
    @Override
    public Long getGuideCount(Long id) {
        return teamMemberTeacherMapper.getGuideCount(id);
    }

    /**
     * 根据teamId获取结对的个数，查老师个数
     *
     * @param id teamId
     * @return
     */
    @Override
    public Long getJoinCountByTeamId(Long id) {
        return teamMemberTeacherMapper.getJoinCountByTeamId(id);
    }

    @Override
    public List<TeamInfoVo> selectJoinTeamByTeacherId(Long userId, Long race, Long name) {
        Long[] teamIds = teamMemberTeacherMapper.selectJoinTeamByTeacherId(userId,race,name);
        List<TeamInfoVo> teamInfoVos = new ArrayList<>();
        if (teamIds != null && teamIds.length > 0) {
            teamInfoVos = teamInfoService.selectTeamInfoByIds(teamIds);
        }
        return teamInfoVos;

    }
    /**
     * 根据team获取指导老师
     * @param team
     * @return
     */
    @Override
    public List<UserInfoVo> getMemberTeachersByTeamId(Long team) {
        List<Long> ids = teamMemberTeacherMapper.getMemberTeachersByTeamId(team);
        List<UserInfoVo> userInfoVos = userInfoService.selectUserInfoByIds(ids);
        return userInfoVos;
    }

    /**
     * 根据team和user删除邀请
     * @param team
     * @param user
     */
    @Override
    public void deleteTeamMemberTeacherByTeamAndTeacher(Long team, Long user) {
        teamMemberTeacherMapper.deleteTeamMemberTeacherByTeamAndTeacher(team, user);
    }

    /**
     * 判断用户是否已加入
     * @param userId
     * @param raceId
     * @return
     */
    @Override
    public int getJoinCount(Long userId, Long raceId, Long team) {
        return teamMemberTeacherMapper.getCountJoin(userId,raceId, team);
    }

    /**
     * 根据比赛id获取指导老师列表
     *
     * @param raceId
     * @return
     */
    @Override
    public List<UserInfoVo> getTeachersByRaceId(Long raceId) {
        List<Long> ids = teamMemberTeacherMapper.getTeachersByRaceId(raceId);// 老师ids
        List<UserInfoVo> userInfoVos = new ArrayList<>();
        if (ids != null && ids.size() > 0) {
            userInfoVos = userInfoService.selectUserInfoByIds(ids);
        }
        return userInfoVos;
    }


}