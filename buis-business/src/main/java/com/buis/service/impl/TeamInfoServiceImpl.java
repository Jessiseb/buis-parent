package com.buis.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.buis.common.constant.TeamQueryConstants;
import com.buis.common.core.page.MyPageVo;
import com.buis.common.core.page.PageDomain;
import com.buis.common.core.page.TableSupport;
import com.buis.common.core.redis.RedisCache;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.DateUtils;
import com.buis.common.utils.PageUtils;
import com.buis.common.utils.StringUtils;
import com.buis.domain.*;
import com.buis.dto.TeamMemberDto;
import com.buis.enums.ChooseStatus;
import com.buis.mapper.TeamInfoMapper;
import com.buis.service.*;
import com.buis.system.service.ISysDictDataService;
import com.buis.system.service.ISysDictTypeService;
import com.buis.vo.TeamInfoVo;
import com.buis.vo.TeamMemberStudentVo;
import com.buis.vo.UserInfoVo;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.buis.common.constant.CacheConstants.JOIN_TEAM_LOCK;
import static com.buis.common.constant.CacheConstants.QUERY_TEAM_LOCK;

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
    private ITeamInfoService teamInfoService;
    @Resource
    private IRaceInfoService raceInfoService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RaceDetailServiceImpl raceDetailService;
    @Resource
    private RedisCache redisCache;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private ITeamMemberStudentService teamMemberStudentService;
    @Resource
    private ITeamMemberTeacherService teamMemberTeacherService;
    @Resource
    private ICollegeMajorService classroomMajorService;

    /**
     * 新增队伍信息
     *
     * @param teamInfo 队伍信息
     * @return 结果
     */
    @Override
    @Transactional
    public TeamInfo insertTeamInfo(TeamInfo teamInfo) {
        //获取登录用户信息
        UserInfo loginUser = userInfoService.getLoginUser();
        //获取用户vo
        UserInfoVo userInfoVo = userInfoService.selectUserInfoById(loginUser.getId());
        //TODO 要有第三个指导老师
        Long teacher1 = teamInfo.getTeacher1();
        Long teacher2 = teamInfo.getTeacher2();
        if (teacher1 != null && teacher2 != null) {
            if (teacher1.equals(teacher2)) {
                throw new ServiceException("同一指导老师不能重复选择");
            }
        }
        // 判断报名条件
        raceInfoService.checkJoinCondition(loginUser.getId(), teamInfo.getRace());
        teamInfo.setCreateTime(DateUtils.getNowDate());
        //根据现在参与的比赛id判断创建的队伍个数是否超过限制
        //TODO 默認兩,后面根据数据库获取
        int limit = 2;
        TeamMemberStudent teamMember = new TeamMemberStudent();
        teamMember.setDecider(loginUser.getId());
        List<TeamMemberStudentVo> teamMemberStudentVos = teamStudentService.selectTeamMemberStudentList(teamMember);
        if (teamMemberStudentVos.size() > limit) {
            throw new ServiceException("当前比赛您已经加入过" + limit + "个队伍,无法再加入!");
        }

        //根据比赛信息设置队伍最大人数
        RaceDetail raceDetail = raceDetailService.selectRaceDetailByRaceId(teamInfo.getRace());
        int teamMax = raceDetail.getTeamMax();//最大人数
        teamInfo.setNumber(teamMax);
        teamInfo.setLeader(loginUser.getId()); //leader
        teamInfo.setLeaderName(loginUser.getName()); //leader_name
        teamInfo.setPhone(loginUser.getPhone()); //phone

        //班级
//        String stuGrade = userInfoVo.getStuGrade();
//        String majorName = userInfoVo.getMajorName();
//        String stuClass = userInfoVo.getStuClass();
        //拼接返回
        teamInfo.setSClass(userInfoVo.getSClass());
        int ok = teamInfoMapper.insertTeamInfo(teamInfo);
        //更新队伍信息缓存
        if (ok > 0) {
            updateTeamInfoCache(teamInfo);
            raceInfoService.deleteRacesInfoCache();
        }
        //创建项目成功后,把信息插入队伍用户表
        int sucess = 0;
        if (ok > 0) {
            TeamMemberStudent teamMemberStudent = new TeamMemberStudent();
            teamMemberStudent.setTeam(teamInfo.getId()); //队伍id
            teamMemberStudent.setRace(teamInfo.getRace()); //比赛id
            teamMemberStudent.setStudent(teamInfo.getLeader()); //队长
            teamMemberStudent.setDecider(teamInfo.getLeader()); //决定者 = 创建该队伍的人
            teamMemberStudent.setIsChoosed(ChooseStatus.CHOOSE.getCode()); //选定状态
            if (teamInfo.getTeacher1() != null) {
                TeamMemberTeacher teamMemberTeacher = new TeamMemberTeacher();
                teamMemberTeacher.setRace(teamInfo.getRace());
                teamMemberTeacher.setTeacher(teamInfo.getTeacher1());
                teamMemberTeacher.setDecider(teamInfo.getLeader());
                teamMemberTeacher.setTeam(teamInfo.getId());
                teamMemberStudent.setIsChoosed(ChooseStatus.CHOOSE.getCode()); //选定状态
                teamTeacherService.insertTeamMemberTeacher(teamMemberTeacher);
            }
            if (teamInfo.getTeacher2() != null) {
                TeamMemberTeacher teamMemberTeacher = new TeamMemberTeacher();
                teamMemberTeacher.setRace(teamInfo.getRace());
                teamMemberTeacher.setTeacher(teamInfo.getTeacher1());
                teamMemberTeacher.setDecider(teamInfo.getLeader());
                teamMemberTeacher.setTeam(teamInfo.getId());
                teamMemberStudent.setIsChoosed(ChooseStatus.CHOOSE.getCode()); //选定状态
                teamTeacherService.insertTeamMemberTeacher(teamMemberTeacher);
            }
            sucess = teamStudentService.insertTeamMemberStudent(teamMemberStudent);
        }

        return sucess > 0 ? teamInfo : null;
    }

    /**
     * 修改队伍信息
     *
     * @param teamInfo 队伍信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTeamInfo(TeamInfo teamInfo) {
        // 判断报名条件
        raceInfoService.checkJoinCondition(teamInfo.getLeader(), teamInfo.getRace());
        teamInfo.setUpdateTime(DateUtils.getNowDate());
        //只有指导老师和负责人才能修改
        if (!isTeamLeaderOrTeacher(teamInfo.getId())) {
            throw new ServiceException("您不是老师或负责人，不能修改队伍信息!");
        }
        int i = teamInfoMapper.updateTeamInfo(teamInfo);
        TeamInfo teamInfoNewest = teamInfoMapper.selectTeamInfoById(teamInfo.getId());
        //更新队伍信息缓存
        updateTeamInfoCache(teamInfoNewest);
        return i;
    }

    /**
     * 批量删除队伍信息
     *
     * @param ids 需要删除的队伍信息主键
     * @return 结果
     */
    @Override
    public int deleteTeamInfoByIds(Long[] ids) {
        int i = teamInfoMapper.deleteTeamInfoByIds(ids);

        //删除队伍信息缓存
        for (Long id : ids) {
            deleteTeamInfoCache(id);
        }
        return i;
    }


    /**
     * 删除队伍信息信息
     *
     * @param id 队伍信息主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteTeamInfoById(Long id) {
        //获取登录用户id
        String loginIdStr = StpUtil.getLoginId().toString();
        Long loginId;
        try {
            loginId = Long.parseLong(loginIdStr);
        } catch (NumberFormatException e) {
            throw new ServiceException("无效的用户ID格式");
        }
        //根据用户id查出该用户是不是队伍的队长
        //根据id查出队伍信息
        TeamInfo teamInfo = teamInfoMapper.selectTeamInfoById(id);

        //根据队伍信息查出队长id
        if (teamInfo.getLeader().equals(loginId)) {
            //是队长-->能进行删除
            int i = teamInfoMapper.deleteTeamInfoById(id);
            //删除队伍中间表
            teamMemberStudentService.deleteTeamMemberStudentByTeam(id);
            teamMemberTeacherService.deleteTeamMemberTeacherByTeam(id);

            //删除队伍信息缓存
            deleteTeamInfoCache(id);
            raceInfoService.deleteRacesInfoCache();
            return i;
        }

        //不是队长不能删除队伍
        throw new ServiceException("您不是负责人，不能删除队伍");

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTeamInfoByRaceIdAndUserId(Long race, Long userId) {
        TeamInfo teamInfo = teamInfoMapper.selectTeamInfoByRaceIdAndUserId(race, userId);
        teamInfoMapper.deleteTeamInfoByRaceIdAndUserId(race, userId);
        deleteTeamInfoCache(teamInfo.getId());
    }

    @Override
    public int getCountJoin(Long userId, Long raceId, Long team) {
        return teamInfoMapper.getCountJoin(userId, raceId, team);
    }

    /**
     * 根据用户id查询用户创建的队伍
     *
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
    @Transactional(rollbackFor = Exception.class)
    public int joinTeamById(TeamMemberDto teamMemberDto, UserInfo loginUser) {
        //判空
        if (teamMemberDto == null || loginUser == null) {
            throw new ServiceException("参数错误");
        }

        //用户最多加入指定个队伍
        //根据比赛id查询到比赛详情的用户可参与的队伍个数
        Long raceId = teamMemberDto.getRace();
        Long teamId = teamMemberDto.getTeam();
        if (raceId == null) {
            throw new ServiceException("参数错误,id为空");
        }
        RaceDetail raceDetail = raceDetailService.selectRaceDetailByRaceId(raceId);
        if (raceDetail == null) {
            throw new ServiceException("无详细比赛规则");
        }
        Long count = raceDetail.getCount();
        //小于0或为空
        if (count <= 0 || count == null) {
            throw new ServiceException("比赛配置错误,最大加入队伍数无效");
        }
        //根据用户id查出用户已经加入多少个队伍
        Long userId = loginUser.getId();
        //判断用户是老师还是学生 (0-学生 1-老师)
        Long isTeacher = teamMemberDto.getIsTeacher();
        TeamMemberStudent teamMemberStudent = new TeamMemberStudent();
        TeamInfo teamInfo = null;
        if (isTeacher == 0) {
            //学生
            //根据用户id和比赛id查询这个用户参与的这个比赛项目数量是否超过限制
            TeamMemberStudent memberStudent = new TeamMemberStudent();
            memberStudent.setRace(raceId);
            memberStudent.setStudent(userId);
            //个数
            int hasJoin = teamStudentService.selectTeamMemberStudentList(memberStudent).size();
//            int hasJoin = teamStudentService.getStudentJoinTeamByUserId(userId);
            if (hasJoin >= count) {
                throw new ServiceException("在该比赛中您已加入其他团队,最多只能加入一个项目团队!!!");
            }

            //分布式锁---->只有抢到锁的人才能加入队伍
            //尝试获得锁
            String key = JOIN_TEAM_LOCK + teamId;
            RLock rLock = redissonClient.getLock(key);
            //设置锁过期时间
            try {
                boolean locked = rLock.tryLock(2, 30, TimeUnit.SECONDS);
                if (!locked) {
                    throw new ServiceException("系统繁忙,请稍后重试");
                }
                //                while (true) {
                //根据id查询项目队伍信息
                teamInfo = teamInfoMapper.selectTeamInfoById(teamId);

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
                teamMemberStudent.setDecider(teamInfo.getLeader());//负责人
                teamMemberStudent.setStudent(userId); //成员
                teamMemberStudent.setIsChoosed(ChooseStatus.TOBESELECTED.getCode()); //是否选择
                teamMemberStudent.setReason(teamMemberDto.getReason()); //加入理由
//                }
            } catch (Exception e) {
                log.error("加入队伍失败!", e);
                throw new ServiceException("加入队伍失败: " + e.getMessage());
            } finally {
                //释放锁
                //只能释放自己的锁
                if (rLock.isHeldByCurrentThread()) {
                    rLock.unlock();
                }
            }
        } else if (isTeacher == 1) {
            //老师
            throw new ServiceException("您不是学生,不能加入队伍");
        }
        return teamStudentService.insertTeamMemberStudent(teamMemberStudent);
    }

    /**
     * 退出队伍
     *
     * @param team   队伍id
     * @param userId 登录用户id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int quitTeam(Long team, Long userId) {
        //对id进行校验 -------------> 可以避免缓存穿透(不会直接打到数据库)
        if (team == null || userId == null) {
            throw new ServiceException("参数错误");
        }

        //查询队伍是否存在
        TeamInfo teamInfo = teamInfoMapper.selectTeamInfoById(team);
        if (teamInfo == null) {
            throw new ServiceException("退出失败: 该队伍不存在");
        }
        //校验登录用户是否已经加入队伍
        if (teamStudentService.getJoinCount(userId, teamInfo.getRace(), teamInfo.getId()) == 0) {
            throw new ServiceException("退出失败: 您未加入该队伍");
        }
        //如果队伍只有一个人就直接解散
        if (teamStudentService.getJoinCountByTeamId(team) == 1) {
            //删除队伍
            //并且吧队伍用户中间表设置为已删除
            teamStudentService.deleteTeamMemberStudentByTeamAndStudent(team, userId);
            //删除队伍信息缓存
            deleteTeamInfoCache(userId);
            raceInfoService.deleteRacesInfoCache();
            return teamInfoMapper.deleteTeamInfoById(team);
        } else {
            //队伍不止一个人
            //如果是负责人退出队伍,负责人位置移交给第二个进项目团队的人
            if (teamInfo.getLeader().equals(userId)) {
                //查询队伍成员
                List<TeamMemberStudent> teamMemberStudents = teamStudentService.selectTeamMemberStudentByTeamId(team);
                //查出的队伍成员按照id排序
                teamMemberStudents.sort(Comparator.comparingLong(TeamMemberStudent::getId));
                //将第二个成员设置为负责人
                TeamMemberStudent teamMemberStudent = teamMemberStudents.get(1);
                Long nextLeader = teamMemberStudent.getStudent(); //id
                //根据nextLeader获取下一个队长的信息
                UserInfo nextLeaderInfo = userInfoService.selectUserInfoById(nextLeader);
                String phone = nextLeaderInfo.getPhone(); //手机号
                int college = nextLeaderInfo.getCollege(); //学院
                String name = nextLeaderInfo.getName(); //姓名
                teamInfo.setName(name);
                teamInfo.setPhone(phone);
                teamInfo.setCollege(String.valueOf(college));
                teamInfo.setLeader(teamMemberStudent.getStudent());
                //修改队伍负责人信息
                int ok = teamInfoMapper.updateTeamInfo(teamInfo);
                if (ok > 0) {
                    //删除该成员
                    //删除队伍信息缓存
                    deleteTeamInfoCache(userId);
                    raceInfoService.deleteRacesInfoCache();
                    return teamStudentService.deleteTeamMemberStudentByTeamAndStudent(team, userId);
                } else {
                    throw new ServiceException("退出队伍失败");
                }
            } else {
                //不是负责人退出队伍
                //删除队伍信息缓存
                deleteTeamInfoCache(userId);
                raceInfoService.deleteRacesInfoCache();
                return teamStudentService.quitTeam(team, userId);
            }
        }

    }

    @Override
    @Transactional
    public int agreeJoinTeam(Long userId, Long teamId) {

        TeamMemberStudent teamMemberStudent = teamStudentService.selectTeamMemberStudentByTeamAndStudent(userId, teamId);

        // 获取申请记录
        if (teamMemberStudent.getIsChoosed() != ChooseStatus.TOBESELECTED.getCode()) { // 0表示待审核
            throw new ServiceException("申请不存在或已处理");
        }

        // 4. 检查队伍容量
        TeamInfo teamInfo = teamInfoMapper.selectTeamInfoById(teamId);
        Long currentCount = teamStudentService.getJoinCountByTeamId(teamId);
        if (currentCount >= teamInfo.getNumber()) {
            throw new ServiceException("队伍已满");
        }


        // 5. 更新状态为已同意
        teamMemberStudent.setIsChoosed(ChooseStatus.CHOOSE.getCode()); // 1表示已同意
        int ok = teamStudentService.updateTeamMemberStudent(teamMemberStudent);
        if (ok == 0) {
            throw new ServiceException("同意失败");
        }

        return ok;
    }

    @Override
    @Transactional
    public int refuseJoinTeam(Long userId, Long teamId) {
        TeamMemberStudent teamMemberStudent = teamStudentService.selectTeamMemberStudentByTeamAndStudent(userId, teamId);

        // 获取申请记录
        if (teamMemberStudent.getIsChoosed() != ChooseStatus.TOBESELECTED.getCode()) { // 0表示待审核
            throw new ServiceException("申请不存在或已处理");
        }

        teamMemberStudent.setIsChoosed(ChooseStatus.REFUSE.getCode()); // 2表示已拒绝
        int ok = teamStudentService.updateTeamMemberStudent(teamMemberStudent);
        if (ok == 0) {
            throw new ServiceException("同意失败");
        }
        return ok;
    }

    /**
     * 根据队伍id查询队伍信息(缓存)
     *
     * @param teamInfo
     * @return
     */
    @Override
    public MyPageVo<List<TeamInfoVo>> selectTeamInfoListFromRedis(TeamInfo teamInfo) {
        //无查询条件
        //获取key
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String pageCacheKey = getPageCacheKey(pageNum, pageSize);

        String TeamsJson = redisCache.getCacheObject(pageCacheKey);
        if (StringUtils.isNotEmpty(TeamsJson)) {
            //存在直接返回
            return getMyPageVo(TeamsJson);
        }
        //判断空值
        if (TeamsJson != null) {
            return null;
        }

//        String lockKey = QUERY_TEAM_LOCK + getPageCacheKey(pageNum, pageSize);
//        RLock rLock = redissonClient.getLock(lockKey);
        MyPageVo myPageVo = null;
        try {
//            boolean isLock = rLock.tryLock(3, 60 * 3, TimeUnit.SECONDS);

            //再次检查缓存
            TeamsJson = redisCache.getCacheObject(pageCacheKey);
            //判断是否存在
            if (StringUtils.isNotEmpty(TeamsJson)) {
                //存在直接返回
                return getMyPageVo(TeamsJson);
            }
            //判断空值
            if (TeamsJson != null) {
                return null;
            }

//            if (!isLock) {
//                Thread.sleep(50);
//                return selectTeamInfoListFromRedis(teamInfo);
//            }

            //拿到锁，查询数据库
            Page<TeamInfoVo> page = PageUtils.startPage(pageNum, pageSize);
            List<TeamInfoVo> teamInfoVos = teamInfoService.selectTeamInfoList(teamInfo);

            //不存在，返回错误信息
            if (teamInfoVos.isEmpty()) {
                log.info("将空值写入redis");
                redisCache.setCacheObject(pageCacheKey, "", 5, TimeUnit.MINUTES);
                return null;
            }
            //存在，写入redis
            myPageVo = new MyPageVo(page, teamInfoVos);
            redisCache.setCacheObject(pageCacheKey, JSON.toJSONString(myPageVo), 10, TimeUnit.MINUTES);

        } catch (Exception e) {
            throw new ServiceException("查询失败: " + e.getMessage());
        } finally {
            //释放锁
//            if (rLock.isHeldByCurrentThread()) {
//                rLock.unlock();
//            }
        }
        return myPageVo;
    }

    /**
     * 封装成MyPageVo对象
     *
     * @param TeamsJson
     * @return
     */
    private MyPageVo getMyPageVo(String TeamsJson) {
        Map jsonMap = JSON.parseObject(TeamsJson, Map.class);
        String rowsJson = JSON.toJSONString(jsonMap.get("rows"));
        List<TeamInfoVo> teamInfoVoList = JSON.parseObject(rowsJson, new TypeReference<List<TeamInfoVo>>() {
        });
        MyPageVo myPageVo = JSONUtil.toBean(TeamsJson, MyPageVo.class);
        myPageVo.setRows(teamInfoVoList);
        return myPageVo;
    }

    /**
     * 获取分页缓存key
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    private String getPageCacheKey(int pageNum, int pageSize) {
        return String.format("teams:page:%d:size:%d", pageNum, pageSize);
    }


    /**
     * 检查是否有查询条件
     *
     * @param queryTeam
     * @return
     */
    public boolean hasSearchCondition(TeamInfo queryTeam) {
        try {
            for (String fieldName : TeamQueryConstants.CONDITION_FIELDS1) {
                Field field = TeamInfo.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                if (field.get(queryTeam) != null && !field.get(queryTeam).equals("")) {
                    return true;
                }
            }
            for (String fieldName : TeamQueryConstants.CONDITION_FIELDS2) {
                Field field = TeamInfo.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                if (!field.get(queryTeam).equals(0)) {
                    return true;
                }
            }
            for (String fieldName : TeamQueryConstants.CONDITION_FIELDS3) {
                Field field = TeamInfo.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                if (!field.get(queryTeam).equals("Y")) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("条件检测异常", e);
        }
    }

    /**
     * 负责人转让
     *
     * @param team 团队id
     * @param user 用户id
     * @return
     */
    @Override
    @Transactional
    public int transfer(Long team, Long user) {
        //只有队长或指导老师有权限
        boolean ok = isTeamLeaderOrTeacher(team);
        if (!ok) {
            throw new ServiceException("您没有操作权限!");
        }

        //队伍表的leader改成user, leader_name改成user对应的用户名字,
        // phone改成user对应的联系方式,s_class改成对应的班级,学院改成对应的学院
        TeamInfoVo teamInfoVo = teamInfoService.selectTeamInfoById(team);
        //根据user查询到对应用户信息
        UserInfoVo userInfoVo = userInfoService.selectUserInfoById(user);
        //把年级,专业,班级拼接返回
        String stuGrade = userInfoVo.getStuGrade();
        String majorName = userInfoVo.getMajorName();
        String stuClass = userInfoVo.getStuClass();
        String stuGradeMajorClass = stuGrade + majorName + stuClass;

        teamInfoVo.setLeader(user); //用户id
        teamInfoVo.setLeaderName(userInfoVo.getName()); //用户名字
        teamInfoVo.setPhone(userInfoVo.getPhone()); //用户联系方式
        teamInfoVo.setSClass(stuGradeMajorClass); //用户年级+专业+班级
        teamInfoVo.setCollege(String.valueOf(userInfoVo.getCollege())); //用户学院

        int i = teamInfoService.updateTeamInfoWithLeader(teamInfoVo);
        if (i <= 0) {
            throw new ServiceException("转让失败");
        }

        //队伍用户中间表的队长修改成user
        List<TeamMemberStudent> teamMemberStudents = teamStudentService.selectTeamMemberStudentByTeamId(team);
        int result = 0;
        for (TeamMemberStudent teamMemberStudent : teamMemberStudents) {
            teamMemberStudent.setDecider(user);
            result = teamStudentService.updateTeamMemberStudent(teamMemberStudent);
        }
        if (result <= 0) {
            throw new ServiceException("更新失败");
        }
        return result;
    }

    @Override
    public int updateTeamInfoWithLeader(TeamInfoVo teamInfoVo) {


        //只有指导老师和负责人才能修改
        if (!isTeamLeaderOrTeacher(teamInfoVo.getId())) {
            throw new ServiceException("您不是老师或负责人，不能修改队伍信息!");
        }
        int i = teamInfoMapper.updateTeamInfo(teamInfoVo);
        //更新队伍信息缓存
        updateTeamInfoCache(teamInfoVo);
        return i;
    }

    /**
     * 根据队伍负责人姓名查出比赛队伍信息
     *
     * @param name
     * @return
     */
    @Override
    public List<TeamInfo> selectTeamInfoByName(String name) {
        return teamInfoMapper.selectTeamInfoByName(name);
    }

    @Override
    public String getTeacherInstructCountFromContest(Long userId) {
        return teamInfoMapper.getTeacherInstructCountFromContest(userId);
    }

    @Override
    public String getTeacherInstructCountFromInnovation(Long userId) {
        return teamInfoMapper.getTeacherInstructCountFromInnovation(userId);
    }

    @Override
    public List<TeamInfoVo> selectMyPublishedTeams(Long userId) {
        return teamInfoMapper.selectMyPublishedTeams(userId);
    }

    @Override
    public List<TeamInfo> selectAllTeamInfoById(Long userId) {
        return teamInfoMapper.selectAllTeamInfoById(userId);
    }

    /**
     * 后台查询队伍信息列表 Vo
     *
     * @param teamInfo
     * @return
     */
    @Override
    public List<TeamInfoVo> selectTeamInfoList(TeamInfo teamInfo) {
        List<TeamInfo> teamInfoList = teamInfoMapper.selectTeamInfoList(teamInfo);
        if (teamInfoList == null || teamInfoList.isEmpty()) {
            return Collections.emptyList(); // 返回空列表
        }
        List<TeamInfoVo> results = new ArrayList<>();
        for (TeamInfo team : teamInfoList) {
            TeamInfoVo teamInfoVo = createTeamInfoVo(team);
            //带指导老师查询
            if (teamInfo.getTeacher1() != null || teamInfo.getTeacher2() != null) {
                //返回该老师指导的队伍id
                List<Long> teamIds1 = teamTeacherService.selectStudentIdsByTeacherId(teamInfo.getTeacher1());
                List<Long> teamIds2 = teamTeacherService.selectStudentIdsByTeacherId(teamInfo.getTeacher2());
                List<Long> teamIds = new ArrayList<>(teamIds1);
                teamIds1.addAll(teamIds2);
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
            String college = teamInfoVo.getCollege();
            CollegeMajor collegeMajor = classroomMajorService.selectCollegeMajorById(Long.valueOf(college));
            teamInfoVo.setCollege(collegeMajor.getName());
            teamInfoVos.add(teamInfoVo);
        }
        return teamInfoVos;
    }


    /**
     * 抽取出判断登录用户是否为指导老师或者团队负责人
     */
    public boolean isTeamLeaderOrTeacher(Long team) {
        //获取用户信息
        UserInfo loginUser = userInfoService.getLoginUser();
        Long loginId = loginUser.getId();
        //团队信息
        TeamInfoVo teamInfoVo = teamInfoService.selectTeamInfoById(team);
        //判断用户是否为老师
        if (loginUser.getIsTeacher() == 1) {
            //是老师
            //判断是否为指导老师
            return teamInfoVo.getTeacher1().equals(loginId) || teamInfoVo.getTeacher2().equals(loginId);
        }
        //判断用户是否是负责人
        Long leader = teamInfoVo.getLeader();
        return leader.equals(loginId);
    }

    //更新队伍信息缓存 + 删除队伍信息分页缓存
    private void updateTeamInfoCache(TeamInfo teamInfo) {
        String teamInfoCacheKey = "team:" + teamInfo.getId();
        TeamInfoVo teamInfoVo = createTeamInfoVo(teamInfo);
        redisCache.setCacheObject(teamInfoCacheKey, JSON.toJSONString(teamInfoVo), 10, TimeUnit.MINUTES);

        //删除所有队伍信息分页缓存
        String pattern = "teams:page:*:size:*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            // 删除匹配的键
            redisTemplate.delete(keys);
        }
    }

    //删除队伍信息缓存 + 删除队伍信息分页缓存
    private void deleteTeamInfoCache(Long id) {
        String teamInfoCacheKey = "team:" + id;
        redisCache.deleteObject(teamInfoCacheKey);

        //删除所有队伍信息分页缓存
        String pattern = "teams:page:*:size:*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            // 删除匹配的键
            redisTemplate.delete(keys);
        }
    }

    /**
     * 查询队伍信息
     *
     * @param id 队伍信息主键
     * @return 队伍信息
     */
    @Override
    public TeamInfoVo selectTeamInfoById(Long id) {
        //从redis中查询队伍缓存
        String key = "team:" + id;
        String TeamJson = redisCache.getCacheObject(key);

        //判断是否存在
        if (StringUtils.isNotEmpty(TeamJson)) {
            //存在直接返回
            return JSONUtil.toBean(TeamJson, TeamInfoVo.class);
        }

        //判断空值
        if (TeamJson != null) {
            return null;
        }

//        String lockKey = QUERY_TEAM_LOCK + id;
//        RLock rLock = redissonClient.getLock(lockKey);
        TeamInfoVo teamInfoVo = null;
        try {
//            boolean isLock = rLock.tryLock(3, 60 * 3, TimeUnit.SECONDS);

            //再次检查缓存
            TeamJson = redisCache.getCacheObject("team:" + id);
            //判断是否存在
            if (StringUtils.isNotEmpty(TeamJson)) {
                //存在直接返回
                return JSONUtil.toBean(TeamJson, TeamInfoVo.class);
            }
            //判断空值
            if (TeamJson != null) {
                return null;
            }

//            if (!isLock) {
//                Thread.sleep(50);
//                return selectTeamInfoById(id);
//            }

            //拿到锁，查询数据库
            TeamInfo teamInfo = teamInfoMapper.selectTeamInfoById(id);

            //不存在，返回错误信息
            if (teamInfo == null) {
                log.info("将空值写入redis");
                redisCache.setCacheObject("team:" + id, "", 5, TimeUnit.MINUTES);
                return null;
            }
            //存在，写入redis
            teamInfoVo = createTeamInfoVo(teamInfo);
            redisCache.setCacheObject("team:" + id, JSONUtil.toJsonStr(teamInfoVo), 10, TimeUnit.MINUTES);

        } catch (Exception e) {
            throw new ServiceException("查询失败: " + e.getMessage());
        } finally {
            //释放锁
//            if (rLock.isHeldByCurrentThread()) {
//                rLock.unlock();
//            }
        }
        return teamInfoVo;
    }
}
