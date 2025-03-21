package com.buis.service.impl;

import java.util.Date;
import java.util.List;

import com.buis.common.core.domain.R;
import com.buis.common.utils.DateUtils;
import com.buis.domain.Score;
import com.buis.domain.UserInfo;
import com.buis.service.*;
import com.buis.vo.RaceInfoVo;
import com.buis.vo.TeamInfoVo;
import com.buis.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.AwardMapper;
import com.buis.domain.Award;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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
    public Award selectAwardById(Long id) {
        return awardMapper.selectAwardById(id);
    }

    /**
     * 查询奖状信息列表
     *
     * @param award 奖状信息
     * @return 奖状信息
     */
    @Override
    public List<Award> selectAwardList(Award award) {
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

    /**
     * @param teamId
     * @return
     */
    @Override
    @Transactional
    public Award selectAwardByTeam(Long teamId) {
        Award award = new Award();
        TeamInfoVo teamInfoVo = teamInfoService.selectTeamInfoById(teamId);
        Long race = teamInfoVo.getRace();
        RaceInfoVo raceInfoVo = raceInfoService.selectRaceInfoById(race);
        Date scoreEnd = raceInfoVo.getScoreEnd();

        // 获取指导老师信息
        UserInfoVo userInfoVo = userInfoService.selectUserInfoById(teamInfoVo.getTeacher());

        // 使用compareTo比较时间，限制查询时间
        if (scoreEnd.compareTo(DateUtils.getNowDate()) >= 0) {
            // 评分未截止
            throw new RuntimeException("评分未截止，请等待评分截止时间");
        }

        // 截止时正常查询
        List<Score> scores = scoreService.selectTeamsRanking();
        for (int i = 0; i < scores.size(); i++) { // 记录索引
            Score score = scores.get(i);
            if (score.getTeam().equals(teamId)) {
                int rank = i + 1;
                if (rank > 3) {
                    throw new RuntimeException("未查询到奖状");
                }

                Award existingAward = awardMapper.selectAwardByTeam(teamId);

                // 查询结果不为空直接返回已存在的奖状
                if (existingAward != null) {
                    return existingAward;
                }

                // 插入奖状信息
                award.setTeamId(teamId);
                award.setAwardDate(DateUtils.getNowDate());//todo
                award.setYear("2024-2025");
                award.setRaceId(race);
                award.setAwardLevel(String.valueOf(rank));
                award.setTeachers(userInfoVo.getName());
                award.setIsDeleted(0);

                awardMapper.insertAward(award);
                awardMapper.updateAward(award);

//              返回查询出来的奖状信息
                return award;
            }
        }

        throw new RuntimeException("未查询到奖状");
    }
}
