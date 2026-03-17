package com.buis.mapper;

import com.buis.model.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 个人大数据面板Mapper接口
 */
@Repository
public interface DashboardMapper {

    /**
     * 统计用户参赛总数(去重比赛ID)
     */
    Integer countUserRaces(@Param("userId") Long userId);

    /**
     * 统计用户获奖总数(award表按teamId匹配 + award_records表按userId匹配)
     */
    Integer countUserAwards(@Param("userId") Long userId);

    /**
     * 统计用户参加项目数(去重队伍ID)
     */
    Integer countUserProjects(@Param("userId") Long userId);

    /**
     * 统计用户组队次数(含作为队长和成员)
     */
    Integer countUserTeams(@Param("userId") Long userId);

    /**
     * 获取竞赛成长曲线数据
     */
    List<GrowthCurveItemVo> selectGrowthCurve(@Param("userId") Long userId);

    /**
     * 获取获奖等级分布
     */
    List<DashboardRankingVo.AwardLevelDistributionItem> selectAwardLevelDistribution(@Param("userId") Long userId);

    /**
     * 获取比赛类型分布
     */
    List<DashboardRankingVo.RaceCategoryCountItem> selectRaceCategoryDistribution(@Param("userId") Long userId);

    /**
     * 获取用户综合评分
     */
    Map<String, Object> selectUserCompositeScore(@Param("userId") Long userId);

    /**
     * 获取年级排名
     */
    Map<String, Object> selectGradeRank(@Param("userId") Long userId, @Param("grade") int grade);

    /**
     * 获取学院排名
     */
    Map<String, Object> selectCollegeRank(@Param("userId") Long userId, @Param("college") int college);

    /**
     * 获取赛事提醒信息
     */
    List<RaceReminderVo> selectRaceReminders();
    /**
     * 获取个人成长时间轴数据
     */
    List<GrowthEventVo> selectGrowthTimeline(@Param("userId") Long userId);

    /**
     * 获取获奖档案
     */
    List<AwardRecordVo> selectAwardRecords(@Param("userId") Long userId);

    /**
     * 获取项目档案
     */
    List<ProjectRecordVo> selectProjectRecords(@Param("userId") Long userId);

    /**
     * 获取成长报告数据
     */
    GrowthReportVo selectGrowthReport(@Param("userId") Long userId);
}
