package com.buis.service.impl;

import com.buis.common.exception.ServiceException;
import com.buis.common.utils.DateUtils;
import com.buis.common.utils.SecurityUtils;
import com.buis.common.utils.StringUtils;
import com.buis.domain.*;
import com.buis.mapper.RaceInfoMapper;
import com.buis.service.*;
import com.buis.vo.RaceInfoVo;
import com.buis.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 比赛信息Service业务层处理
 *
 * @author kaven
 * @date 2024-10-23
 */
@Service
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
    private IRaceLanqiaoStudentService LanqiaoStudentService;
    @Autowired
    private IRaceLanqiaoTeacherService lanqiaoTeacherService;

    /**
     * 查询比赛信息
     *
     * @param id 比赛信息主键
     * @return 比赛信息
     */
    @Override
    public RaceInfoVo selectRaceInfoById(Long id) {
        RaceInfoVo raceInfoVo = raceInfoMapper.selectRaceInfoById(id);

        RaceDetail raceDetail = new RaceDetail();
        if (raceInfoVo != null) {
            // 设置是否已报名
            setIsJoin(raceInfoVo);
            // 设置比赛详情
            raceDetail.setRace(raceInfoVo.getId());
            List<RaceDetail> raceDetailVos = raceDetailService.selectRaceDetailList(raceDetail);
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
                // 设置比赛详情
                RaceDetail raceDetail = new RaceDetail();
                raceDetail.setRace(raceInfoVo.getId());
                List<RaceDetail> raceDetailVos = raceDetailService.selectRaceDetailList(raceDetail);
                if (raceDetailVos != null && raceDetailVos.size() > 0) {
                    raceInfoVo.setRaceDetail(raceDetailVos.get(0));
                }
            }
        }
        return raceInfoVos;
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
        raceInfo.setCreateTime(DateUtils.getNowDate());
        int rows = raceInfoMapper.insertRaceInfo(raceInfo);
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
                raceInfoMapper.batchReward(list);
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
        raceInfo.setUpdateTime(DateUtils.getNowDate());
        raceInfoMapper.deleteRewardByRace(raceInfo.getId());
        insertReward(raceInfo);
        return raceInfoMapper.updateRaceInfo(raceInfo);
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
        return raceInfoMapper.deleteRaceInfoByIds(ids);
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
        return raceInfoMapper.deleteRaceInfoById(id);
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
        if (!checkJoinTime(raceId)) {
            throw new ServiceException("当前时间不在报名时间内");
        }
        if (checkJoinFull(raceId)) {
            throw new ServiceException("当前比赛已满");
        }
        RaceInfoVo raceInfoVo = selectRaceInfoById(raceId);
        if (raceInfoVo != null) {
            //如果比赛是蓝桥杯
            if (raceInfoVo.getRaceType() == 1) {
                checkJoinLanQiaoStudent(raceId, userId);
                checkJoinLanQiaoTeacher(raceId, userId);
            }
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

    /**
     * 判断当前时间是否在报名时间内
     *
     * @param raceId
     * @return
     */
    private boolean checkJoinTime(Long raceId) {
        // 查询比赛报名时间
        RaceInfo raceInfo = raceInfoMapper.selectRaceInfoById(raceId);
        if (raceInfo != null) {
            Date joinStart = raceInfo.getJoinStart();
            Date joinEnd = raceInfo.getJoinEnd();
            // 获取当前系统时间
            Date now = DateUtils.getNowDate();
            return now.getTime() >= joinStart.getTime() && now.getTime() <= joinEnd.getTime();
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
            Long tLimit = raceDetail.getTLimit();
            if (tLimit != 0) {
                return (teacher + student) >= tLimit;
            }
        }
        return false;
    }

    private boolean checkJoinLanQiaoStudent(Long userId, Long raceId) {
        Object o = lanqiaoTeacherService.selectRaceLanqiaoTeacherByUserIdAndRaceId(raceId, userId);
        if (o != null) {
            throw new ServiceException("已提交过老师报名表，请取消后再次提交");
        }
        Object o1 = LanqiaoStudentService.selectRaceLanqiaoStudentByUserIdAndRaceId(raceId, userId);
        if (o1 != null) {
            throw new ServiceException("已提交过学生报名表，请勿重复提交");
        }
        return true;
    }

    private boolean checkJoinLanQiaoTeacher(Long userId, Long raceId) {
        Object o = LanqiaoStudentService.selectRaceLanqiaoStudentByUserIdAndRaceId(raceId, userId);
        if (o != null) {
            throw new ServiceException("已提交过学生报名表，请取消后再次提交");
        }
        Object o1 = lanqiaoTeacherService.selectRaceLanqiaoTeacherByUserIdAndRaceId(raceId, userId);
        if (o1 != null) {
            throw new ServiceException("已提交过老师报名表，请勿重复提交");
        }
        return true;
    }
}

