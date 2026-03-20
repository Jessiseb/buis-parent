package com.buis.service;

import com.buis.model.vo.*;

import java.util.List;

/**
 * 个人大数据面板Service接口
 */
public interface IDashboardService {

    /**
     * 获取数据概览
     */
    DashboardOverviewVo getOverview(Long userId);

    /**
     * 获取竞赛成长曲线
     */
    List<GrowthCurveItemVo> getGrowthCurve(Long userId);

    /**
     * 获取排名分析
     */
    DashboardRankingVo getRanking(Long userId);

    /**
     * 获取能力标签(按分类分组)
     */
    List<SkillCategoryVo> getSkills(Long userId);

    /**
     * 获取面板汇总数据
     */
    DashboardSummaryVo getSummary(Long userId);

    /**
     * 获取赛事提醒信息
     */
    List<RaceReminderVo> getRaceReminders();
    /**
     * 获取个人成长时间轴
     */
    List<GrowthEventVo> getGrowthTimeline(Long userId);

    /**
     * 获取获奖档案
     */
    List<AwardRecordVo> getAwardRecords(Long userId);

    /**
     * 获取项目档案
     */
    List<ProjectRecordVo> getProjectRecords(Long userId);

    /**
     * 获取成果明细
     */
    List<AchievementItemVo> getAchievementItems(Long userId);

    /**
     * 获取成长报告数据
     */
    GrowthReportVo getGrowthReport(Long userId);

    /**
     * 获取综合评分
     */
    Double getCompositeScore(Long userId);

    /**
     * 获取成果统计
     */
    AchievementStatsVo getAchievementStats(Long userId);

    /**
     * 获取分布数据
     */
    DistributionVo getDistributionData(Long userId);

}
