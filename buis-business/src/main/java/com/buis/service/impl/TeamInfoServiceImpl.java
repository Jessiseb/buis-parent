package com.buis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.DateUtils;
import com.buis.domain.RaceDetail;
import com.buis.domain.TeamInfo;
import com.buis.domain.TeamMemberStudent;
import com.buis.domain.UserInfo;
import com.buis.dto.TeamMemberDto;
import com.buis.mapper.TeamInfoMapper;
import com.buis.service.*;
import com.buis.vo.RaceInfoVo;
import com.buis.vo.TeamInfoVo;
import com.buis.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.buis.common.constant.CacheConstants.JOIN_TEAM_LOCK;

/**
 * 队伍信息Service业务层处理
 *
 * @author buis
 * @date 2024-10-11
 */
@Service
@Slf4j
public class TeamInfoServiceImpl implements ITeamInfoService {
    @Resource
    private TeamInfoMapper teamInfoMapper;
    @Resource
    private IUserInfoService userInfoService;
    @Resource
    private ITeamMemberStudentService teamStudentService;
    @Resource
    private ITeamMemberTeacherService teamTeacherService;
    @Resource
    private IRaceInfoService raceInfoService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RaceDetailServiceImpl raceDetailService;


    /**
     * 新增队伍信息
     *
     * @param teamInfo 队伍信息
     * @return 结果
     */
    @Override
    public int insertTeamInfo(TeamInfo teamInfo) {
        // 判断报名条件
        raceInfoService.checkJoinCondition(teamInfo.getLeader(), teamInfo.getRace());
        teamInfo.setCreateTime(DateUtils.getNowDate());
        //根据比赛信息设置队伍最大人数
        RaceDetail raceDetail = raceDetailService.selectRaceDetailByRaceId(teamInfo.getRace());
        int teamMax =raceDetail.getTeamMax();//最大人数
        teamInfo.setNumber(teamMax);

        int ok = teamInfoMapper.insertTeamInfo(teamInfo);
        //创建项目成功后,把信息插入队伍用户表
        int sucess = 0;
        if (ok > 0) {
            TeamMemberStudent teamMemberStudent = new TeamMemberStudent();
            teamMemberStudent.setTeam(teamInfo.getId()); //队伍id
            teamMemberStudent.setRace(teamInfo.getRace()); //比赛id
            teamMemberStudent.setStudent(teamInfo.getLeader()); //队长
            teamMemberStudent.setDecider(teamInfo.getLeader()); //决定者 = 创建该队伍的人
            teamMemberStudent.setIsChoosed(1L); //选定状态
            sucess = teamStudentService.insertTeamMemberStudent(teamMemberStudent);
        }
        return sucess;
    }

    /**
     * 修改队伍信息
     *
     * @param teamInfo 队伍信息
     * @return 结果
     */
    @Override
    public int updateTeamInfo(TeamInfo teamInfo) {
        // 判断报名条件
        raceInfoService.checkJoinCondition(teamInfo.getLeader(), teamInfo.getRace());
        teamInfo.setUpdateTime(DateUtils.getNowDate());
        return teamInfoMapper.updateTeamInfo(teamInfo);
    }

    /**
     * 批量删除队伍信息
     *
     * @param ids 需要删除的队伍信息主键
     * @return 结果
     */
    @Override
    public int deleteTeamInfoByIds(Long[] ids) {
        return teamInfoMapper.deleteTeamInfoByIds(ids);
    }

    /**
     * 删除队伍信息信息
     *
     * @param id 队伍信息主键
     * @return 结果
     */
    @Override
    public int deleteTeamInfoById(Long id) {
        return teamInfoMapper.deleteTeamInfoById(id);
    }

    @Override
    public void deleteTeamInfoByRaceIdAndUserId(Long race, Long userId) {
        teamInfoMapper.deleteTeamInfoByRaceIdAndUserId(race, userId);
    }

    @Override
    public int getCountJoin(Long userId, Long raceId, Long team) {
        return teamInfoMapper.getCountJoin(userId,raceId,team);
    }

    /**
     * 根据用户id查询用户创建的队伍
     * @param userId
     * @return
     */
    @Override
    public List<TeamInfoVo> selectTeamInfoByUserId(Long userId) {
        List<TeamInfoVo> results = new ArrayList<>();
        List<TeamInfo> teamInfoList = teamInfoMapper.selectTeamInfoByUserId(userId);
        if (teamInfoList == null || teamInfoList.isEmpty()) {
            return results; // 返回空列表
        }
        for (TeamInfo team : teamInfoList) {
            results.add(createTeamInfoVo(team));
        }
        return results;
    }

    /**
     * 加入指定项目队伍
     *
     * @param teamMemberDto 加入队伍信息
     * @param loginUser     登录用户
     * @return
     */
    @Override
    public int joinTeamById(TeamMemberDto teamMemberDto, UserInfo loginUser) {
        //判空
        if (teamMemberDto == null || loginUser == null) {
            throw new ServiceException("参数错误");
        }

        //用户最多加入指定个队伍
        //TODO 先设置为1 后续改为可选
        int count = 1;

        //根据用户id查出用户已经加入多少个队伍
        Long userId = loginUser.getId();
        //判断用户是老师还是学生 (0-学生 1-老师)
        Long isTeacher = teamMemberDto.getIsTeacher();
        TeamMemberStudent teamMemberStudent = new TeamMemberStudent();
        if (isTeacher == 0){
            //学生
            int hasJoin = teamStudentService.getStudentJoinTeamByUserId(userId);
            if (hasJoin >= count) {
                throw new ServiceException("您已加入其他团队,最多只能加入一个项目团队!!!");
            }


            //分布式锁---->只有抢到锁的人才能加入队伍
            //尝试获得锁
            String key = JOIN_TEAM_LOCK;
            RLock rLock = redissonClient.getLock(key);
            //设置锁过期时间
            try{
                rLock.tryLock(3,60*3, TimeUnit.SECONDS);
                while(true){
                    //队伍id
                    Long teamId = teamMemberDto.getTeam();
                    //根据id查询项目队伍信息
                    TeamInfo teamInfo = teamInfoMapper.selectTeamInfoById(teamId);

                    //不能加入不存在的队伍
                    if (teamInfo == null) {
                        throw new ServiceException("该队伍不存在");
                    }

                    //已经过期的队伍不能加入
                    if (teamInfo.getEnd().before(DateUtils.getNowDate())) {
                        throw new ServiceException("该队伍已过期");
                    }

                    //已经满人的队伍不能加入
                    if (teamStudentService.getJoinCountByTeamId(teamId) >= teamInfo.getNumber()) {
                        throw new ServiceException("该队伍已满");
                    }

                    //不能重复加入已经加入的队伍
                    if (teamStudentService.getJoinCount(userId, teamInfo.getRace(), teamInfo.getId()) > 0) {
                        throw new ServiceException("您已加入该队伍");
                    }

                    //修改用户队伍表信息
                    teamMemberStudent.setTeam(teamInfo.getId()); //队伍
                    teamMemberStudent.setRace(teamInfo.getRace()); //比赛
                    teamMemberStudent.setStudent(userId); //成员
                    teamMemberStudent.setIsChoosed(0L); //是否选择
                    teamMemberStudent.setReason(teamMemberDto.getReason()); //加入理由
                }
            }catch (Exception e){
                log.error("加入队伍失败!",e);
                throw new ServiceException("加入队伍失败: "+e.getMessage());
            }finally {
                //释放锁
                //只能释放自己的锁
                if (rLock.isHeldByCurrentThread()) {
                    rLock.unlock();
                }
            }
        }else if(isTeacher == 1){
            //老师
            throw new ServiceException("您不是学生,不能加入队伍");
        }
        return teamStudentService.insertTeamMemberStudent(teamMemberStudent);
    }

    /**
     * 查询队伍信息列表 Vo
     *
     * @param teamInfo
     * @return
     */
    @Override
    public List<TeamInfoVo> selectTeamInfoList(TeamInfo teamInfo) {
        List<TeamInfoVo> results = new ArrayList<>();

        List<TeamInfo> teamInfoList = teamInfoMapper.selectTeamInfoList(teamInfo);

        if (teamInfoList == null || teamInfoList.isEmpty()) {
            return results; // 返回空列表
        }
        for (TeamInfo team : teamInfoList) {
            TeamInfoVo teamInfoVo = createTeamInfoVo(team);
            //带指导老师查询
            if (teamInfo.getTeacher() != null) {
                //返回该老师指导的队伍id
                List<Long> teamIds = teamTeacherService.selectStudentIdsByTeacherId(teamInfo.getTeacher());
                for (Long teamId : teamIds) {
                    if (teamId.equals(team.getId())) {
                        teamInfoVo.setIsInstruct(1);
                    }
                }
            }
            results.add(teamInfoVo);
        }
        return results;
    }

    /**
     * 查询队伍信息
     *
     * @param id 队伍信息主键
     * @return 队伍信息
     */
    @Override
    public TeamInfoVo selectTeamInfoById(Long id) {
        TeamInfo team = teamInfoMapper.selectTeamInfoById(id);
        return team != null ? createTeamInfoVo(team) : new TeamInfoVo();
    }

    /**
     * 创建 TeamInfoVo 对象
     *
     * @param team 队伍信息
     * @return TeamInfoVo
     */
    private TeamInfoVo createTeamInfoVo(TeamInfo team) {
        TeamInfoVo teamInfoVo = new TeamInfoVo();
        BeanUtil.copyProperties(team, teamInfoVo);
        UserInfoVo userInfoVo = userInfoService.selectUserInfoById(team.getLeader());
        Long studentCount = teamStudentService.getJoinCountByTeamId(team.getId());
        Long teacherCount = teamTeacherService.getJoinCountByTeamId(team.getId());
        if (userInfoVo != null) { // 判断队长是学生还是老师，算上人数
            if (userInfoVo.getIsTeacher() != null && userInfoVo.getIsTeacher() == 1) {
                teacherCount++;
            } else {
                studentCount++;
            }
        }
        // 获取队长信息
        teamInfoVo.setLeaderInfo(userInfoVo);
        //获取比赛信息
        teamInfoVo.setRaceInfoVo(raceInfoService.selectRaceInfoById(team.getRace()));
        // 获取学生人数
        teamInfoVo.setStudentCount(studentCount);
        // 获取老师人数
        teamInfoVo.setTeacherCount(teacherCount);



        return teamInfoVo;
    }

    @Override
    public List<TeamInfoVo> selectTeamInfoByIds(Long[] ids) {
        List<TeamInfoVo> teamInfoVos = new ArrayList<>();
        List<TeamInfo> teamInfos = teamInfoMapper.selectTeamInfoByIds(ids);
        for (TeamInfo teamInfo : teamInfos) {
            TeamInfoVo teamInfoVo = createTeamInfoVo(teamInfo);
            teamInfoVos.add(teamInfoVo);
        }
        return teamInfoVos;
    }
}
