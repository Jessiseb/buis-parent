package com.buis.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.DateUtils;
import com.buis.domain.Award;
import com.buis.domain.Score;
import com.buis.domain.TeamInfo;
import com.buis.mapper.AwardMapper;
import com.buis.service.*;
import com.buis.vo.AwardVo;
import com.buis.vo.RaceInfoVo;
import com.buis.vo.TeamInfoVo;
import com.buis.vo.UserInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * 奖状信息Service业务层处理
 *
 * @author zjx
 * @date 2025-03-16
 */
@Service
public class AwardServiceImpl implements IAwardService {
    @Resource
    private AwardMapper awardMapper;

    @Resource
    private IScoreService scoreService;

    @Resource
    private IRaceInfoService raceInfoService;

    @Resource
    private ITeamInfoService teamInfoService;

    @Resource
    private IUserInfoService userInfoService;

    /**
     * 查询奖状信息
     *
     * @param id 奖状信息主键
     * @return 奖状信息
     */
    @Override
    public  List<AwardVo> selectAwardById(Long id) {
        //获取登录用户id
        String loginIdStr = StpUtil.getLoginId().toString();
        Long loginId;
        try {
            loginId = Long.parseLong(loginIdStr);
        } catch (NumberFormatException e) {
            throw new ServiceException("无效的用户ID格式");
        }
        if(!loginId.equals(id)){
            throw new ServiceException("无权限操作");
        }
        //根据登录用户id查询到用户名字
        UserInfoVo userInfoVo = userInfoService.selectUserInfoById(loginId);
        if (userInfoVo == null) {
            throw new ServiceException("用户不存在!");
        }
        String name = userInfoVo.getName();
        AwardVo award = new AwardVo();
        award.setName(name);
        return awardMapper.selectAwardList(award);
    }



    /**
     * 查询奖状信息列表
     *
     * @param award 奖状信息
     * @return 奖状信息
     */
    @Override
    public List<AwardVo> selectAwardList(AwardVo award)
    {
        return awardMapper.selectAwardList(award);
    }

    /**
     * 新增奖状信息
     *
     * @param award 奖状信息
     * @return 结果
     */
    @Override
    public int insertAward(Award award) {
        award.setCreateTime(DateUtils.getNowDate());
        return awardMapper.insertAward(award);
    }

    /**
     * 修改奖状信息
     *
     * @param award 奖状信息
     * @return 结果
     */
    @Override
    public int updateAward(Award award) {
        award.setUpdateTime(DateUtils.getNowDate());
        return awardMapper.updateAward(award);
    }

    /**
     * 批量删除奖状信息
     *
     * @param ids 需要删除的奖状信息主键
     * @return 结果
     */
    @Override
    public int deleteAwardByIds(Long[] ids) {
        return awardMapper.deleteAwardByIds(ids);
    }

    /**
     * 删除奖状信息信息
     *
     * @param id 奖状信息主键
     * @return 结果
     */
    @Override
    public int deleteAwardById(Long id) {
        return awardMapper.deleteAwardById(id);
    }


    @Override
    public Award selectAwardByNameAndRace(String name, Long raceId) {
        return awardMapper.selectAwardByNameAndRace(name, raceId);
    }

    /**
     * @param name
     * @return
     */
//    @Override
//    @Transactional
//    public Award selectAwardByNameAndRace(String name, Long raceId) {
//        Award award = new Award();
//
//        TeamInfo teamInfo = teamInfoService.selectTeamInfoByName(name);
//        Long race = teamInfo.getRace();
//
//        RaceInfoVo raceInfoVo = raceInfoService.selectRaceInfoById(race);
//        Date scoreEnd = raceInfoVo.getScoreEnd(); //评分截止时间
//
//        // 获取指导老师信息
//        String teacherNames = null;
//        Long teacher1 = teamInfo.getTeacher1();
//        Long teacher2 = teamInfo.getTeacher2();
//        if (teacher1 != null || teacher2 != null) {
//            UserInfoVo teacherInfo1 = userInfoService.selectUserInfoById(teacher1);
//            UserInfoVo teacherInfo2 = userInfoService.selectUserInfoById(teacher2);
//            //拼接指导老师名称
//            String teacher1Name = teacherInfo1.getName();
//            String teacher2Name = teacherInfo2.getName();
//            teacherNames = teacher1Name + "、" + teacher2Name;
//        }
//
//
//        // 使用compareTo比较时间，限制查询时间
//        if (scoreEnd.compareTo(DateUtils.getNowDate()) >= 0) {
//            // 评分未截止
//            throw new RuntimeException("评分未截止，请等待评分截止时间~");
//        }
//
//        // 截止时正常查询
////        List<Score> scores = scoreService.selectTeamsRanking();
////        for (int i = 0; i < scores.size(); i++) { // 记录索引
////            Score score = scores.get(i);
////            if (score.getTeam().equals(teamId)) {
////                int rank = i + 1;
////                if (rank > 3) {
////                    throw new RuntimeException("未查询到奖状");
////                }
//
//        Award existingAward = awardMapper.selectAwardByName(name);
//
//        // 查询结果不为空直接返回已存在的奖状
//        if (existingAward != null) {
//            return existingAward;
//        }
//
//        // 插入奖状信息
////        award.setTeamId(teamId);
//        award.setAwardDate(DateUtils.getNowDate());//
//        //获取当前年份
//        int currentYear = LocalDate.now().getYear();
//        award.setYear(currentYear + "-" + currentYear + 1); //学年2025-2026
//        award.setRaceId(race); //比赛id
////        award.setAwardLevel(String.valueOf(rank));
//        award.setAwardLevel(level);
//        award.setTeachers(teacherNames);
//        award.setIsDeleted(0);
//        awardMapper.insertAward(award);
//
////              返回查询出来的奖状信息
//        return award;
//    }
//
////}
////        throw new
////
////RuntimeException("生成奖状信息失败");
////    }
////            }

}
