package com.buis.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.domain.model.LoginUser;
import com.buis.common.core.page.MyPageVo;
import com.buis.common.core.page.PageDomain;
import com.buis.common.core.page.TableSupport;
import com.buis.common.core.redis.RedisCache;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.DateUtils;
import com.buis.common.utils.PageUtils;
import com.buis.common.utils.SecurityUtils;
import com.buis.common.utils.StringUtils;
import com.buis.domain.*;
import com.buis.dto.GradeStatistic;
import com.buis.dto.StudentExportToDiDto;
import com.buis.dto.TeacherExportToDaDto;
import com.buis.dto.TeacherExportToDiDto;
import com.buis.mapper.RaceInfoMapper;
import com.buis.service.*;
import com.buis.system.mapper.SysRoleMapper;
import com.buis.vo.*;
import com.buis.vo.RaceDetailVo;
import com.buis.vo.RaceInfoVo;
import com.buis.vo.TeamInfoVo;
import com.buis.vo.UserInfoVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.time.LocalDate;

import static com.buis.common.constant.CacheConstants.QUERY_HOT_RACE_LOCK;
import static com.buis.common.constant.CacheConstants.QUERY_RACE_LOCK;

/**
 * 比赛信息Service业务层处理
 *
 * @author kaven
 * @date 2024-10-23
 */
@Service
@Slf4j
public class RaceInfoServiceImpl implements IRaceInfoService {
    @Resource
    private RaceInfoMapper raceInfoMapper;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IRaceDetailService raceDetailService;
    @Autowired
    private ITeamInfoService teamInfoService;
    @Autowired
    private ITeamMemberTeacherService teamMemberTeacherService;
    @Autowired
    private ITeamMemberStudentService teamMemberStudentService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private IPreSignStudentService preSignStudentService;
    @Resource
    private IRaceInfoService raceInfoService;
    @Resource
    private SysRoleMapper sysRoleMapper;

    /**
     * 查询比赛信息
     *
     * @param id 比赛信息主键
     * @return 比赛信息
     */
    @Override
    public RaceInfoVo selectRaceInfoById(Long id) {
        RaceInfoVo raceInfoVo = raceInfoMapper.selectRaceInfoById(id);
        //登录用户id
//        Object userId =  StpUtil.getLoginId();
        RaceDetail raceDetail = new RaceDetail();
        if (raceInfoVo != null) {
            // 设置是否已报名
            setIsJoin(raceInfoVo);
            // 设置是否预报名
            setIsPreJoin(raceInfoVo);
            // 设置比赛详情
            raceDetail.setRace(raceInfoVo.getId());
            List<RaceDetailVo> raceDetailVos = raceDetailService.selectRaceDetailList(raceDetail);
            if (raceDetailVos != null && raceDetailVos.size() > 0) {
                raceInfoVo.setRaceDetail(raceDetailVos.get(0));
            }
        }
        return raceInfoVo;
    }

    /**
     * 为isJoin赋值
     */
    public void setIsJoin(RaceInfoVo raceInfoVo) {
        Long userId = null;
        try {
            userId = SecurityUtils.getFrontLoginUserId();
            raceInfoVo.setIsJoin(checkJoin(userId, raceInfoVo.getId(), null));
        } catch (ServiceException e) {
            return;
        }
    }

    /**
     * 为isPreJoin赋值
     */
    public void setIsPreJoin(RaceInfoVo raceInfoVo) {
        Long userId = null;
        try {
            userId = SecurityUtils.getFrontLoginUserId();
            raceInfoVo.setIsPreJoin(checkJoin(userId, raceInfoVo.getId(), null));
        } catch (ServiceException e) {
            return;
        }
    }

    /**
     * 查询比赛信息列表
     *
     * @param raceInfo 比赛信息
     * @return 比赛信息
     */
    @Override
    public List<RaceInfoVo> selectRaceInfoList(RaceInfo raceInfo) {

        // 根据传入参数获取比赛信息
        List<RaceInfoVo> raceInfoVos = raceInfoMapper.selectRaceInfoList(raceInfo);
        for (RaceInfoVo raceInfoVo : raceInfoVos) {
            if (raceInfoVo != null) {
                // 设置是否已报名
                setIsJoin(raceInfoVo);
                // 设置是否预报名
                setIsPreJoin(raceInfoVo);
                // 设置比赛详情
                RaceDetail raceDetail = new RaceDetail();
                raceDetail.setRace(raceInfoVo.getId());
                List<RaceDetailVo> raceDetailVos = raceDetailService.selectRaceDetailList(raceDetail);
                if (raceDetailVos != null && raceDetailVos.size() > 0) {
                    raceInfoVo.setRaceDetail(raceDetailVos.get(0));
                }
            }
        }
        return raceInfoVos;
    }


    /**
     * 只能查出已经发布的比赛信息
     * @param raceInfo
     * @return
     */
    @Override
    public List<RaceInfoVo> getRaceInfoList(RaceInfo raceInfo) {
        // 根据传入参数获取比赛信息
        List<RaceInfoVo> raceInfoVos = raceInfoMapper.getRaceInfoList(raceInfo);
        //todo  抽取出一下代码
        for (RaceInfoVo raceInfoVo : raceInfoVos) {
            if (raceInfoVo != null) {
                // 设置是否已报名
                setIsJoin(raceInfoVo);
                // 设置是否预报名
                setIsPreJoin(raceInfoVo);
                // 设置比赛详情
                RaceDetail raceDetail = new RaceDetail();
                raceDetail.setRace(raceInfoVo.getId());
                List<RaceDetailVo> raceDetailVos = raceDetailService.selectRaceDetailList(raceDetail);
                if (raceDetailVos != null && raceDetailVos.size() > 0) {
                    raceInfoVo.setRaceDetail(raceDetailVos.get(0));
                }
            }
        }
        return raceInfoVos;
    }




    /**
     * 为比赛信息添加字段
     *
     * @param raceInfoVos
     */
    private List<RaceInfoVo> addField(List<RaceInfoVo> raceInfoVos) {
        Long userId = SecurityUtils.getFrontLoginUserId();
        for (RaceInfoVo raceInfoVo : raceInfoVos) {
            if (raceInfoVo != null) {
                // 设置是否已报名
                setJoinStatus(raceInfoVo, userId, false);
                // 设置是否预报名
                setJoinStatus(raceInfoVo, userId, true);
            }
        }
        return raceInfoVos;
    }

    private void setJoinStatus(RaceInfoVo raceInfoVo, Long userId, boolean isPreJoin) {
        String cacheKey = isPreJoin ? getIsPreJoinCacheKey(raceInfoVo.getId(), userId) : getIsJoinCacheKey(raceInfoVo.getId(), userId);
        String cacheValue = redisCache.getCacheObject(cacheKey);

        if (StringUtils.isNotNull(cacheValue)) {
            int status = Integer.parseInt(cacheValue);
            setStatus(raceInfoVo, isPreJoin, status);
        } else {
            String lockKey = QUERY_RACE_LOCK + cacheKey;
            RLock rLock = redissonClient.getLock(lockKey);

            try {
                boolean isLock = rLock.tryLock(3, 60 * 3, TimeUnit.SECONDS);
                if (!isLock) {
                    Thread.sleep(50);
                    return;
                }

                int status = checkJoin(userId, raceInfoVo.getId(), null);
                redisCache.setCacheObject(cacheKey, JSON.toJSONString(status), 1, TimeUnit.DAYS);
                setStatus(raceInfoVo, isPreJoin, status);
            } catch (Exception e) {
                throw new ServiceException("查询失败: " + e.getMessage());
            } finally {
                if (rLock.isHeldByCurrentThread()) {
                    rLock.unlock();
                }
            }
        }
    }

    private void setStatus(RaceInfoVo raceInfoVo, boolean isPreJoin, int status) {
        if (isPreJoin) {
            raceInfoVo.setIsPreJoin(status);
        } else {
            raceInfoVo.setIsJoin(status);
        }
    }

    /**
     * 新增比赛信息
     *
     * @param raceInfo 比赛信息
     * @return 结果
     */
    @Transactional
    @Override
    public int insertRaceInfo(RaceInfo raceInfo) {
//        // 获取用户id
//        LoginUser loginUser = SecurityUtils.getLoginUser();
//        Long userId = loginUser.getUser().getUserId();
//        //获取比赛id
//        Long raceId = raceInfo.getId();
//        // 判断报名条件
//        this.checkJoinCondition(userId, raceId);

        raceInfo.setCreateTime(DateUtils.getNowDate());
        raceInfo.setReviewStatus(0L);//待审核
        raceInfo.setStatus(0L);//设置为筹备中
        int rows = raceInfoMapper.insertRaceInfo(raceInfo);
        if (rows > 0) {
            deleteRacesInfoCache();
        }
        insertReward(raceInfo);
        return rows;
    }

    /**
     * 新增奖项信息
     *
     * @param raceInfo 比赛信息对象
     */
    private void insertReward(RaceInfo raceInfo) {
        List<Reward> rewardList = raceInfo.getRewardList();
        Long id = raceInfo.getId();
        if (StringUtils.isNotNull(rewardList)) {
            List<Reward> list = new ArrayList<Reward>();
            for (Reward reward : rewardList) {
                reward.setRace(id);
                reward.setIsDeleted(0);
                reward.setCreateTime(DateUtils.getNowDate());
                list.add(reward);
            }
            if (list.size() > 0) {
                int i = raceInfoMapper.batchReward(list);
                if (i > 0) {
                    deleteRacesInfoCache();
                }
            }
        }
    }


    /**
     * 修改比赛信息
     *
     * @param raceInfo 比赛信息
     * @return 结果
     */
    @Transactional
    @Override
    public int updateRaceInfo(RaceInfo raceInfo) {
        // 获取用户id
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long userId = loginUser.getUser().getUserId();
        //获取比赛id
        Long raceId = raceInfo.getId();
        // 判断报名条件
//        this.checkJoinCondition(userId, raceId);

        raceInfo.setUpdateTime(DateUtils.getNowDate());
        raceInfoMapper.deleteRewardByRace(raceInfo.getId());
        insertReward(raceInfo);
        int i = raceInfoMapper.updateRaceInfo(raceInfo);
        if (i > 0) {
            deleteRacesInfoCache();
        }
        return i;
    }

    /**
     * 批量删除比赛信息
     *
     * @param ids 需要删除的比赛信息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteRaceInfoByIds(Long[] ids) {
        raceInfoMapper.deleteRewardByRaces(ids);
        int i = raceInfoMapper.deleteRaceInfoByIds(ids);
        if (i > 0) {
            deleteRacesInfoCache();
        }
        return i;
    }

    /**
     * 删除比赛信息信息
     *
     * @param id 比赛信息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteRaceInfoById(Long id) {
        raceInfoMapper.deleteRewardByRace(id);
        int i = raceInfoMapper.deleteRaceInfoById(id);
        if (i > 0) {
            deleteRacesInfoCache();
        }
        return i;
    }

    /**
     * 判断用户是否已报名
     *
     * @param userId
     * @param raceId
     * @return
     */
    private int checkJoin(Long userId, Long raceId, Long team) {
        if (userId == null || raceId == null) {
            throw new IllegalArgumentException("userId and raceId cannot be null");
        }
        int res = 0;
        // 判断互选表有没有信息
        res = teamMemberTeacherService.getJoinCount(userId, raceId, team);
        if (res <= 0) {
            res = teamMemberStudentService.getJoinCount(userId, raceId, team);
            if (res <= 0) {
                // 判断队伍有没有信息
                res = teamInfoService.getCountJoin(userId, raceId, team);
            }
        }
        if (res > 0) {
            res = 1;
        }
        return res;
    }

    /**
     * 判断报名是否符合条件
     */
    public boolean checkJoinCondition(Long userId, Long raceId) {
        if (checkJoinTime(raceId)) {
            throw new ServiceException("当前时间不在报名时间内");
        }
        if (checkJoinFull(raceId)) {
            throw new ServiceException("当前比赛已满");
        }
        RaceInfoVo raceInfoVo = selectRaceInfoById(raceId);
        if (raceInfoVo != null) {
            //是学生只能创建一个队伍
            UserInfoVo userInfoVo = userInfoService.selectUserInfoById(userId);
            if (userInfoVo != null && userInfoVo.getIsTeacher() == 0) {
                int countJoin = teamInfoService.getCountJoin(userId, raceId, null);
                if (countJoin > 0) {
                    throw new ServiceException("你已经创建过队伍");
                }
            }
        }
        return true;
    }

    @Override
    public boolean checkGuideLimit(Long raceId, Long teacherId) {
        //老师现在指导数量
        Long nLimit = teamMemberTeacherService.getGuideCount(teacherId);
        //限制老师最大指导人数数量
        RaceDetail raceDetail = raceDetailService.selectRaceDetailByRaceId(raceId);
        int tLimit = raceDetail.getTLimit();
        // 检查老师加入的队伍数量是否超过比赛的指导老师数量限制
        if (tLimit != 0 && nLimit >= tLimit) {
            throw new ServiceException("您已超过最大指导数量");
        }
        return true;
    }

    /**
     * 审批第二课堂
     *
     * @param id
     * @return
     */
    @Override
    public AjaxResult updateApproval(Long id) {
        //获取登录用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id获取角色id
        List<Long> roleIds = sysRoleMapper.selectRoleListByUserId(userId);
        if (roleIds == null || roleIds.isEmpty()) {
            return AjaxResult.error("用户没有分配任何角色");
        }
        // 获取院级和校级角色id
        List<Map<String, Object>> roleIdsList = sysRoleMapper.selectSchoolAndCollegeRoleId();
        if (roleIdsList == null || roleIdsList.isEmpty()) {
            return AjaxResult.error("无法获取校级或院级角色信息");
        }

        Map<String, Long> roleIdsMap = new HashMap<>();
        for (Map<String, Object> map : roleIdsList) {
            String roleType = (String) map.get("roleType");
            Long roleId = (Long) map.get("roleId");
            roleIdsMap.put(roleType, roleId);
        }
        //获取院级和校级角色id
        Long schoolRoleId = roleIdsMap.get("校级id");
        Long collegeRoleId = roleIdsMap.get("院级id");

        // 判断用户是否是校级或院级用户
        boolean isSchoolUser = roleIds.contains(schoolRoleId);
        boolean isCollegeUser = roleIds.contains(collegeRoleId);
        if (isSchoolUser) {
            // 用户是校级用户
            try {
                raceInfoMapper.updateRaceInfoStatus(id, 1);
                return AjaxResult.success("校级审核成功");
            } catch (Exception e) {
                e.printStackTrace();
                return AjaxResult.error("校级审核失败: " + e.getMessage());
            }
        } else if (isCollegeUser) {
            // 用户是院级用户
            try {
                raceInfoMapper.updateRaceInfoStatus(id, 5);
                return AjaxResult.success("院级审核成功");
            } catch (Exception e) {
                e.printStackTrace();
                return AjaxResult.error("院级审核失败: " + e.getMessage());
            }
        }

        return AjaxResult.error("当前用户不是院级或校级用户");
    }


    /**
     * 根据状态查询比赛信息
     *
     * @param i
     * @return
     */
    @Override
    public List<RaceInfo> selectRaceInfoByStatu(Long i) {
        return raceInfoMapper.selectRaceInfoByStatu(i);

    }

    /**
     * 根据用户id查询出用户所参加的所有比赛
     *
     * @param userId
     * @return
     */
    @Override
    public List<RaceInfo> getUserRaceList(Long userId) {
//        //获取登录用户
//        String loginIdStr = StpUtil.getLoginId().toString();
//        Long loginId;
//        try {
//            loginId = Long.parseLong(loginIdStr);
//        } catch (NumberFormatException e) {
//            throw new ServiceException("无效的用户ID格式");
//        }
//        //不是本人不能操作
//        if (!loginId.equals(userId)){
//            throw new ServiceException("无权限,请联系管理员");
//        }

        //从队伍用户表中查出该用户所在信息
        List<TeamMemberStudent> teamMemberStudents = teamMemberStudentService.selectTeamMemberStudentByUserId(userId);
        if (teamMemberStudents == null || teamMemberStudents.isEmpty()) {
            return null;
        }
        //根据查出的信息获取到raceId<List>
        List<Long> raceIds = new ArrayList<>();
        for (TeamMemberStudent teamMemberStudent : teamMemberStudents) {
            Long race = teamMemberStudent.getRace();
            raceIds.add(race);
        }
        //根据raceId<List>查询到对应的比赛信息并返回
        return raceInfoMapper.selectRaceInfoByRaceIds(raceIds);

    }

    @Override
    public List<TeacherExportToDaDto> exportTeacherDaProjectCount(int college, int grade, List<TeacherExportToDaDto> teacherExportDtoList, String name, HttpServletResponse response) {
        Map<Long, Long> teacherExportMap = exportTeacherProjectCount(college, grade, teacherExportDtoList, name, response);
        //遍历map集合, 把值插入到TeacherExportDto
        for (Map.Entry<Long, Long> entry : teacherExportMap.entrySet()) {
            TeacherExportToDaDto teacherExportToDaDto = new TeacherExportToDaDto();
            Long key = entry.getKey();//老师id
            //通过老师id查询到老师的名字
            String teacherName = userInfoService.selectUserInfoById(key).getName();
            teacherExportToDaDto.setTeacherName(teacherName);  //老师名称
            teacherExportToDaDto.setProjectCount(entry.getValue()); //指导数量
            teacherExportDtoList.add(teacherExportToDaDto);
        }
        return teacherExportDtoList;
    }

    /**
     * 导出大数据学院的老师负责的第二课堂项目
     *
     * @param teacherExporToDektDtoList
     * @param name
     * @param response
     * @return
     */
    @Override
    public List<TeacherExportToDiDto> exportTeacherDiProjectCount(int college, int grade, List<TeacherExportToDiDto> teacherExporToDektDtoList, String name, HttpServletResponse response) {
        Map<String, Long> teacherExportMap = new HashMap<>(); //老师id : 指导的项目数量
        //遍历所有race_info
        RaceInfo raceInfo = new RaceInfo();
        List<RaceInfoVo> raceInfoVos = raceInfoMapper.selectRaceInfoList(raceInfo);
        //根据传入的学年来过滤
        List<RaceInfoVo> raceInfoVoList = raceInfoVos.stream()
                .filter(raceInfoVo -> raceInfoVo.getCreateTime().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate().getYear() == grade)
                .collect(Collectors.toList());

        log.debug("查询到原始比赛数据量:{}", raceInfoVoList.size());
        //过滤parent_id != 5 的List<RaceInfoVo>
        List<RaceInfoVo> collect = Optional.ofNullable(raceInfoVoList).orElse(Collections.emptyList())
                .stream()
                .filter(raceInfoVo -> raceInfoVo.getParentTid() != null && raceInfoVo.getParentTid() == 5)
                .collect(Collectors.toList());
        log.info("过滤后第二课堂比赛数量:{}", collect.size());

        //第二课堂的比赛详情
        List<RaceDetail> list = new ArrayList<>();
        for (RaceInfoVo raceInfoVo : collect) {
            Optional.ofNullable(raceDetailService.selectRaceDetailByRaceId(raceInfoVo.getId()))
                    .ifPresent(list::add);
        }
        log.debug("获取到比赛详情数量:{}", list.size());

        //遍历list,计算出每个老师负责的比赛有多少个  并且老师的学院是等于指定学院的
        for (RaceDetail raceDetail : list) {
            String teacherName = Optional.ofNullable(raceDetail)
                    .map(RaceDetail::getName)
                    .orElse("");
            //根据名字查询到用户信息
            UserInfo userInfo;
            //如果名字有多个, 按照"、"分割
            if (teacherName.contains("、")) {
                String[] split = teacherName.split("、");
                for (String tName : split) {
                    //根据名字查询到用户信息
                    userInfo = userInfoService.selectUserInfoByName(tName);
                    //只留下指定学院的
                    if (userInfo == null || userInfo.getCollege() != college) {
                        continue;
                    }
                    if (teacherExportMap.containsKey(tName)) {
                        Long count = teacherExportMap.get(tName);
                        count++;
                        teacherExportMap.put(tName, count);
                    } else {
                        teacherExportMap.put(tName, 1L);
                    }
                }
            } else {
                UserInfoVo userInfoVo = userInfoService.selectUserInfoByName(teacherName);
                //只留下指定的学院的
                if (userInfoVo == null || userInfoVo.getCollege() != college) {
                    continue;
                }
                if (StringUtils.isEmpty(teacherName)) {
                    log.warn("比赛ID:{} 无负责人信息", raceDetail.getId());
                    continue;
                }
                if (teacherExportMap.containsKey(teacherName)) {
                    Long count = teacherExportMap.get(teacherName);
                    count++;
                    teacherExportMap.put(teacherName, count);
                } else {
                    teacherExportMap.put(teacherName, 1L);
                }
            }
        }
        log.info("统计完成，涉及教师人数:{}", teacherExportMap.size());

        //遍历map集合, 把值插入到TeacherExportDto
        for (Map.Entry<String, Long> entry : teacherExportMap.entrySet()) {
            TeacherExportToDiDto teacherExportToDiDto = new TeacherExportToDiDto();
            teacherExportToDiDto.setTeacherName(entry.getKey());  //老师名称
            teacherExportToDiDto.setProjectCount(entry.getValue()); //指导数量
            teacherExporToDektDtoList.add(teacherExportToDiDto);
        }
        log.info("导出数据组装完成，最终记录数:{}", teacherExporToDektDtoList.size());
        return teacherExporToDektDtoList;
    }


    /**
     * 抽取出导出的方法
     */
    public <T> Map<Long, Long> exportTeacherProjectCount(int college, int grade, List<T> teacherExportDtoList, String name, HttpServletResponse response) {
        Map<Long, Long> teacherExportMap = new HashMap<>();
        try {
            // 添加日志记录参数
            log.debug("开始统计{}类型项目，参数: name={}", name, name);

            TeamInfo teamInfo = new TeamInfo();
            List<TeamInfoVo> teamInfoVos;
            try {
                teamInfoVos = teamInfoService.selectTeamInfoList(teamInfo);
            } catch (Exception e) {
                log.error("查询团队信息失败，参数:{}", teamInfo, e);
                throw new ServiceException("团队信息查询失败:" + e);
            }

            for (TeamInfoVo teamInfoVo : teamInfoVos) {
                try {
                    if (name != null && teamInfoVo.getRaceInfoVo() != null
                            && name.equals(teamInfoVo.getRaceInfoVo().getParentName())) {
                        //过滤掉不在指定学年的
                        if (teamInfoVo.getRaceInfoVo().getCreateTime().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate().getYear() != grade) {
                            continue;
                        }

                        Long teacher1 = teamInfoVo.getTeacher1();
                        Long teacher2 = teamInfoVo.getTeacher2();

                        if (teacher1 == null && teacher2 == null) {
                            log.warn("团队ID:{} 无指导老师信息", teamInfoVo.getId());
                            continue;
                        }
                        //过滤不是指定学院,
                        Optional.ofNullable(teacher1)
                                .filter(t -> userInfoService.selectUserInfoById(teacher1).getCollege() == college)
                                .ifPresent(t -> teacherExportMap.merge(t, 1L, (oldVal, newVal) -> {
                                    try {
                                        return oldVal + newVal;
                                    } catch (Exception e) {
                                        log.error("统计合并异常，teacherId:{}, oldVal:{}, newVal:{}", t, oldVal, newVal, e);
                                        throw e;
                                    }
                                }));

                        Optional.ofNullable(teacher2)
                                .filter(t -> userInfoService.selectUserInfoById(teacher2).getCollege() == college)
                                .ifPresent(t -> teacherExportMap.merge(t, 1L, (oldVal, newVal) -> {
                                    try {
                                        return oldVal + newVal;
                                    } catch (Exception e) {
                                        log.error("统计合并异常，teacherId:{}, oldVal:{}, newVal:{}", t, oldVal, newVal, e);
                                        throw e;
                                    }
                                }));
                    }
                } catch (Exception e) {
                    log.error("处理团队ID:{} 数据时发生异常", teamInfoVo.getId(), e);
                    throw new ServiceException("数据处理异常:" + e);
                }
            }
            log.debug("统计完成，共处理{}个团队", teamInfoVos.size());
        } catch (ServiceException e) {
            log.error("项目统计业务异常: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("项目统计未知异常: {}", e.getMessage(), e);
            throw new ServiceException("项目统计服务异常:" + e);
        }
        return teacherExportMap;
    }


    @Override
    public List<StudentExportToDiDto> exportStudentDiProjectCount(int college,
                                                                  int grade,
                                                                  List<StudentExportToDiDto> StudentExportToDiDtoList,
                                                                  String name, HttpServletResponse response) {
        Map<String, Long> studentExportMap = new HashMap<>(); //老师id : 指导的项目数量
        //遍历所有race_info
        RaceInfo raceInfo = new RaceInfo();
        List<RaceInfoVo> raceInfoVos = raceInfoMapper.selectRaceInfoList(raceInfo);
        //根据传入的学年来过滤
        List<RaceInfoVo> raceInfoVoList = raceInfoVos.stream()
                .filter(raceInfoVo -> raceInfoVo.getCreateTime().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate().getYear() == grade)
                .collect(Collectors.toList());

        log.debug("查询到原始比赛数据量:{}", raceInfoVoList.size());
//        过滤parent_id != 5 的List<RaceInfoVo>
        List<RaceInfoVo> collect = Optional.ofNullable(raceInfoVoList).orElse(Collections.emptyList())
                .stream()
                .filter(raceInfoVo -> raceInfoVo.getParentTid() != null && raceInfoVo.getParentTid() == 5)
                .collect(Collectors.toList());
        log.info("过滤后第二课堂比赛数量:{}", collect.size());

        //第二课堂的比赛详情
        List<RaceDetail> list = new ArrayList<>();
        for (RaceInfoVo raceInfoVo : collect) {
            Optional.ofNullable(raceDetailService.selectRaceDetailByRaceId(raceInfoVo.getId()))
                    .ifPresent(list::add);
        }
        log.debug("获取到比赛详情数量:{}", list.size());

        //遍历list,计算出每个老师负责的比赛有多少个  并且老师的学院是等于指定学院的
        for (RaceDetail raceDetail : list) {

            Long race = raceDetail.getRace();
            //通过预报名查出学生
            PreSignStudent preSignStudent = preSignStudentService.selectPreSignStudentByRace(race);
            Long student = preSignStudent.getStudent();


//            String teacherName = Optional.ofNullable(raceDetail)
//                    .map(RaceDetail::getName)
//                    .orElse("");
//            //根据名字查询到用户信息
            UserInfo userInfo;
//            //如果名字有多个, 按照"、"分割
//            if (teacherName.contains("、")) {
//                String[] split = teacherName.split("、");
//                for (String tName : split) {
            //根据名字查询到用户信息
            userInfo = userInfoService.selectUserInfoById(student);
            //只留下指定学院的
            if (userInfo == null || userInfo.getCollege() != college) {
                continue;
            }
            if (studentExportMap.containsKey(name)) {
                Long count = studentExportMap.get(name);
                count++;
                studentExportMap.put(name, count);
            } else {
                studentExportMap.put(name, 1L);
            }

            UserInfoVo userInfoVo = userInfoService.selectUserInfoById(student);
            //只留下指定的学院的
            if (userInfoVo == null || userInfoVo.getCollege() != college) {
                continue;
            }

            String studentName = userInfoVo.getName();

            if (StringUtils.isEmpty(studentName)) {
                log.warn("比赛ID:{} 无负责人信息", raceDetail.getId());
                continue;
            }
            if (studentExportMap.containsKey(studentName)) {
                Long count = studentExportMap.get(studentName);
                count++;
                studentExportMap.put(studentName, count);
            } else {
                studentExportMap.put(studentName, 1L);
            }
        }

//        log.info("统计完成，涉及教师人数:{}",studentExportMap.size());

        //遍历map集合, 把值插入到StudentExportDto
        for (Map.Entry<String, Long> entry : studentExportMap.entrySet()) {
            StudentExportToDiDto studentExportToDiDto = new StudentExportToDiDto();
            studentExportToDiDto.setStudentName(entry.getKey());  //老师名称
            studentExportToDiDto.setProjectCount(entry.getValue()); //指导数量
            StudentExportToDiDtoList.add(studentExportToDiDto);
        }
        log.info("导出数据组装完成，最终记录数:{}", StudentExportToDiDtoList.size());
        return StudentExportToDiDtoList;
    }

    @Override
    public String userAnnualParticipationRate(Long userId, int grade) {
        double count = 0.0;
        double sum = 0.0;
        UserInfoVo userInfoVo = userInfoService.selectUserInfoById(userId);
        String name = userInfoVo.getName();
        List<TeamInfo> teamInfos = teamInfoService.selectTeamInfoByName(name);
        List<RaceInfoVo> raceInfoVos = raceInfoMapper.selectRaceInfoAll();
        for (RaceInfoVo race : raceInfoVos) {
            Date createDate = race.getCreateTime();
            LocalDate localDate = createDate.toInstant()
                    .atZone(ZoneId.systemDefault()) // 指定时区（如系统默认时区）
                    .toLocalDate();
            int year = localDate.getYear();
            if (race.getId() != null && year == grade) {
                sum++;
            }
        }

        if (sum == 0) {
            throw new RuntimeException("该年份没有比赛");
        }

        if (teamInfos.isEmpty()) {
            return 0 + "%";
        } else {

            for (TeamInfo teamInfo : teamInfos) {
                Date createDate = teamInfo.getCreateTime();
                LocalDate localDate = createDate.toInstant()
                        .atZone(ZoneId.systemDefault()) // 指定时区（如系统默认时区）
                        .toLocalDate();
                int year = localDate.getYear();
                if (teamInfo.getRace() != null && year == grade) {
                    count++;
                }
            }
            return String.format("%.2f%%", (count / sum) * 100);
        }

    }

    /**
     * 分页查询第二课堂活动申报
     * @param raceInfo
     * @return
     */
    @Override
    public List<RaceInfo> selectRaceSecondActivities(RaceInfo raceInfo) {
        return raceInfoMapper.selectRaceSecondActivities(raceInfo);
    }

    @Override
    public List<GradeStatistic> getStudentGradeCount() {
        return raceInfoMapper.getStudentGradeCount();
    }

    /**
     * 判断当前时间是否在报名时间内
     *
     * @param raceId
     * @return
     */
    public boolean checkJoinTime(Long raceId) {
        // 查询比赛报名时间
        RaceInfo raceInfo = raceInfoMapper.selectRaceInfoById(raceId);
        if (raceInfo != null) {
            Date joinStart = raceInfo.getJoinStart();
            Date joinEnd = raceInfo.getJoinEnd();
            // 获取当前系统时间
            Date now = DateUtils.getNowDate();
            return !(now.getTime() >= joinStart.getTime() && now.getTime() <= joinEnd.getTime());
        }
        return false;
    }

    /**
     * 检查活动报名是否已满
     */
    private boolean checkJoinFull(Long raceId) {
        // 查询已报名人数
        int teacher = teamMemberTeacherService.getJoinCount(null, raceId, null);
        int student = teamMemberStudentService.getJoinCount(null, raceId, null);
        // 查询比赛信息
        RaceDetail raceDetail = raceDetailService.selectRaceDetailByRaceId(raceId);
        if (raceDetail != null) {
            Long capacity = raceDetail.getCapacity();
            if (capacity != 0) {
                return (teacher + student) >= capacity;
            }
        }
        return false;
    }

    /**
     * 判断对象的所有字段是否为空
     *
     * @param obj
     * @return
     */
    public boolean allFieldsEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals("serialVersionUID")) {
                continue;
            }
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                if (value != null) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 从redis中获取比赛信息列表
     *
     * @param raceInfo
     * @return
     */
    @Override
    public MyPageVo<List<RaceInfoVo>> selectRaceInfoListFromRedis(RaceInfo raceInfo) {
        //无条件查询
        //获取key
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String pageCacheKey = getPageCacheKey(pageNum, pageSize);
        ObjectMapper objectMapper = new ObjectMapper();

        String RacesJson = redisCache.getCacheObject(pageCacheKey);
        if (StringUtils.isNotEmpty(RacesJson)) {
            //存在直接返回
            return addFieldToJson(RacesJson);
        }
        //判断空值
        if (RacesJson != null) {
            return null;
        }

        String lockKey = QUERY_RACE_LOCK + getPageCacheKey(pageNum, pageSize);
        RLock rLock = redissonClient.getLock(lockKey);
        MyPageVo myPageVo = null;

        try {
            boolean isLock = rLock.tryLock(3, 60 * 3, TimeUnit.SECONDS);

            //再次检查缓存
            RacesJson = redisCache.getCacheObject(pageCacheKey);
            //判断是否存在
            if (StringUtils.isNotEmpty(RacesJson)) {
                //存在直接返回
                return addFieldToJson(RacesJson);
            }
            //判断空值
            if (RacesJson != null) {
                return null;
            }

            if (!isLock) {
                Thread.sleep(50);
                return selectRaceInfoListFromRedis(raceInfo);
            }

            //拿到锁，查询数据库
            Page<RaceInfoVo> page = PageUtils.startPage(pageNum, pageSize);
            List<RaceInfoVo> raceInfoVos = raceInfoMapper.selectRaceInfoList(raceInfo);

            //不存在，返回错误信息
            if (raceInfoVos.isEmpty()) {
                log.info("将空值写入redis");
                redisCache.setCacheObject(pageCacheKey, "", 5, TimeUnit.MINUTES);
                return null;
            }
            //存在，写入redis
            myPageVo = new MyPageVo(page, raceInfoVos);
            String json = objectMapper.writeValueAsString(myPageVo);
            redisCache.setCacheObject(pageCacheKey, json, 1, TimeUnit.DAYS);
            myPageVo = addFieldToJson(json);
        } catch (Exception e) {
            throw new ServiceException("查询失败: " + e.getMessage());
        } finally {
            //释放锁
            if (rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
        return myPageVo;
    }

    @Override
    public List<SecondClassCountVo> getSecondClassCountByMonth(Integer month) {
        return raceInfoMapper.getSecondClassCountByMonth(month);
    }

    @Override
    public List<SecondClassCountVo> getSecondClassCountByYear(Integer year) {
        return raceInfoMapper.getSecondClassCountByYear(year);
    }

    /**
     * 获取热门比赛
     *
     * @return
     */
    @Override
    public List<HotRaceVo> getHotRace() {
        ObjectMapper objectMapper = new ObjectMapper();
        String hotRaceJson = redisCache.getCacheObject("hotRace");
        if (StringUtils.isNotEmpty(hotRaceJson)) {
            //存在直接返回
            return JSON.parseArray(hotRaceJson, HotRaceVo.class);
        }
        //判断空值
        if (hotRaceJson != null) {
            return null;
        }

        String lockKey = QUERY_HOT_RACE_LOCK;
        RLock rLock = redissonClient.getLock(lockKey);
        try {
            boolean isLock = rLock.tryLock(3, 60 * 3, TimeUnit.SECONDS);
            //再次检查缓存
            hotRaceJson = redisCache.getCacheObject("hotRace");
            if (StringUtils.isNotEmpty(hotRaceJson)) {
                return JSON.parseArray(hotRaceJson, HotRaceVo.class);
            }
            //判断空值
            if (hotRaceJson != null) {
                return null;
            }
            if (!isLock) {
                Thread.sleep(50);
                return getHotRace();
            }
            //拿到锁，查询数据库
            // 获取所有比赛信息
            RaceInfo r = new RaceInfo();
            List<RaceInfoVo> raceInfoList = raceInfoMapper.selectRaceInfoList(r);

            // 用于存储每个比赛的报名人数
            Map<Long, Integer> raceRegistrationCountMap = new HashMap<>();

            // 查询未删除的报名记录
            PreSignStudent p = new PreSignStudent();
            List<PreSignStudentVo> preSignStudentList = preSignStudentService.selectPreSignStudentList(p);

            // 统计每个比赛的报名人数
            for (PreSignStudent preSignStudent : preSignStudentList) {
                Long raceId = preSignStudent.getRace();
                raceRegistrationCountMap.put(raceId, raceRegistrationCountMap.getOrDefault(raceId, 0) + 1);
            }

            // 封装结果
            List<HotRaceVo> resultList = new ArrayList<>();
            for (RaceInfo raceInfo : raceInfoList) {
                Long raceId = raceInfo.getId();
                Integer count = raceRegistrationCountMap.getOrDefault(raceId, 0);
                HotRaceVo vo = new HotRaceVo();
                vo.setRaceName(raceInfo.getName());
                vo.setRegistrationCount(count);
                resultList.add(vo);
            }

            // 按报名人数降序排序，并取前10个
            resultList.sort((o1, o2) -> o2.getRegistrationCount() - o1.getRegistrationCount());
            if (resultList.size() > 10) {
                List<HotRaceVo> hotRaceVoList = resultList.subList(0, 10);
                redisCache.setCacheObject("hotRace", objectMapper.writeValueAsString(hotRaceVoList), 1, TimeUnit.DAYS);
                return hotRaceVoList;
            } else {
                redisCache.setCacheObject("hotRace", objectMapper.writeValueAsString(resultList), 1, TimeUnit.DAYS);
                return resultList;
            }
        } catch (Exception e) {
            throw new ServiceException("查询失败: " + e.getMessage());
        } finally {
            //释放锁
            if (rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }

    /**
     * 给比赛缓存添加字段
     *
     * @param racesJson
     * @return
     */
    private MyPageVo addFieldToJson(String racesJson) {
        MyPageVo<List<RaceInfoVo>> myPageVo = JSON.parseObject(
                racesJson,
                new TypeReference<MyPageVo<List<RaceInfoVo>>>() {
                }
        );
        List<RaceInfoVo> raceInfoVoList = myPageVo.getRows();
        if (raceInfoVoList != null) {
            myPageVo.setRows(addField(raceInfoVoList));
        }
        return myPageVo;
    }


    /**
     * 获取分页缓存的key
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    private String getPageCacheKey(int pageNum, int pageSize) {
        return String.format("races:page:%d:size:%d", pageNum, pageSize);
    }

    /**
     * 获取是否报名缓存的key
     *
     * @param raceId
     * @param userId
     * @return
     */
    private String getIsJoinCacheKey(Long raceId, Long userId) {
        return String.format("isJoin:race:%d:user:%d", raceId, userId);
    }

    /**
     * 获取是否预报名缓存的key
     *
     * @param raceId
     * @param userId
     * @return
     */
    private String getIsPreJoinCacheKey(Long raceId, Long userId) {
        return String.format("isPreJoin:race:%d:user:%d", raceId, userId);
    }


    //删除比赛信息分页缓存
    private void deleteRacesInfoCache() {
        //删除所有比赛信息分页缓存
        String pattern = "races:page:*:size:*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            // 删除匹配的键
            redisTemplate.delete(keys);
        }

        //删除所有是否报名缓存
        String pattern1 = "isJoin:race:*:user:*";
        Set<String> keys1 = redisTemplate.keys(pattern1);
        if (keys1 != null && !keys1.isEmpty()) {
            // 删除匹配的键
            redisTemplate.delete(keys1);
        }

        //删除所有是否预报名缓存
        String pattern2 = "isPreJoin:race:*:user:*";
        Set<String> keys2 = redisTemplate.keys(pattern2);
        if (keys2 != null && !keys2.isEmpty()) {
            redisTemplate.delete(keys2);
        }
    }
}

