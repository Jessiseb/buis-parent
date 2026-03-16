package com.buis.service.impl;

import com.buis.common.core.redis.RedisCache;
import com.buis.common.exception.ServiceException;
import com.buis.mapper.DashboardMapper;
import com.buis.model.enums.RaceLevel;
import com.buis.model.vo.*;
import com.buis.service.IDashboardService;
import com.buis.service.IUserInfoService;
import com.buis.service.IUserSkillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 个人大数据面板Service实现
 */
@Service
@Slf4j
public class DashboardServiceImpl implements IDashboardService {

    private static final String CACHE_PREFIX = "dashboard:";
    private static final int CACHE_TTL_MINUTES = 30;

    @Resource
    private DashboardMapper dashboardMapper;

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private IUserSkillService userSkillService;

    @Resource
    private RedisCache redisCache;

    @Override
    public DashboardOverviewVo getOverview(Long userId) {
        String cacheKey = CACHE_PREFIX + "overview:" + userId;
        DashboardOverviewVo cached = redisCache.getCacheObject(cacheKey);
        if (cached != null) {
            return cached;
        }

        DashboardOverviewVo vo = DashboardOverviewVo.builder()
                .raceCount(dashboardMapper.countUserRaces(userId))
                .awardCount(dashboardMapper.countUserAwards(userId))
                .projectCount(dashboardMapper.countUserProjects(userId))
                .teamCount(dashboardMapper.countUserTeams(userId))
                .build();

        redisCache.setCacheObject(cacheKey, vo, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        return vo;
    }

    @Override
    public List<GrowthCurveItemVo> getGrowthCurve(Long userId) {
        String cacheKey = CACHE_PREFIX + "growth:" + userId;
        List<GrowthCurveItemVo> cached = redisCache.getCacheObject(cacheKey);
        if (cached != null) {
            return cached;
        }

        List<GrowthCurveItemVo> list = dashboardMapper.selectGrowthCurve(userId);

        // 填充比赛级别名称
        for (GrowthCurveItemVo item : list) {
            if (item.getRaceLevel() != null) {
                RaceLevel level = RaceLevel.getEnumByCode(item.getRaceLevel());
                item.setRaceLevelName(level != null ? level.getValue() : "未知");
            }
        }

        redisCache.setCacheObject(cacheKey, list, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        return list;
    }

    @Override
    public DashboardRankingVo getRanking(Long userId) {
        String cacheKey = CACHE_PREFIX + "ranking:" + userId;
        DashboardRankingVo cached = redisCache.getCacheObject(cacheKey);
        if (cached != null) {
            return cached;
        }

        UserInfoVo userInfo = userInfoService.selectUserInfoById(userId);
        if (userInfo == null) {
            throw new ServiceException("用户不存在");
        }

        // 1. 获奖等级分布
        List<DashboardRankingVo.AwardLevelDistributionItem> awardDist =
                dashboardMapper.selectAwardLevelDistribution(userId);
        for (DashboardRankingVo.AwardLevelDistributionItem item : awardDist) {
            item.setAwardLevelName(item.getAwardLevel());
        }

        // 2. 比赛类型分布
        List<DashboardRankingVo.RaceCategoryCountItem> raceDist =
                dashboardMapper.selectRaceCategoryDistribution(userId);
        Map<Integer, String> raceTypeNameMap = new HashMap<>();
        raceTypeNameMap.put(0, "创新创业");
        raceTypeNameMap.put(1, "蓝桥杯");
        raceTypeNameMap.put(2, "第二课堂");
        for (DashboardRankingVo.RaceCategoryCountItem item : raceDist) {
            item.setRaceTypeName(raceTypeNameMap.getOrDefault(item.getRaceType(), "其他"));
        }

        // 3. 综合评分
        Map<String, Object> scoreResult = dashboardMapper.selectUserCompositeScore(userId);
        BigDecimal compositeScore = BigDecimal.ZERO;
        if (scoreResult != null && scoreResult.get("compositeScore") != null) {
            compositeScore = new BigDecimal(scoreResult.get("compositeScore").toString())
                    .setScale(2, RoundingMode.HALF_UP);
        }

        // 4. 年级排名
        int grade = userInfo.getGrade();
        Map<String, Object> gradeRankResult = dashboardMapper.selectGradeRank(userId, grade);
        DashboardRankingVo.RankItem gradeRank = buildRankItem(gradeRankResult, userInfo.getStuGrade());

        // 5. 学院排名
        int college = userInfo.getCollege();
        Map<String, Object> collegeRankResult = dashboardMapper.selectCollegeRank(userId, college);
        DashboardRankingVo.RankItem collegeRank = buildRankItem(collegeRankResult, userInfo.getCollegeName());

        // 6. 百分位排名
        BigDecimal percentile = BigDecimal.ZERO;
        if (gradeRank != null && gradeRank.getTotal() != null && gradeRank.getTotal() > 0
                && gradeRank.getRank() != null) {
            percentile = BigDecimal.valueOf(gradeRank.getTotal() - gradeRank.getRank())
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(gradeRank.getTotal()), 2, RoundingMode.HALF_UP);
        }

        DashboardRankingVo vo = DashboardRankingVo.builder()
                .awardLevelDistribution(awardDist)
                .raceCategoryDistribution(raceDist)
                .gradeRank(gradeRank)
                .collegeRank(collegeRank)
                .compositeScore(compositeScore)
                .percentile(percentile)
                .build();

        redisCache.setCacheObject(cacheKey, vo, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        return vo;
    }

    @Override
    public List<SkillCategoryVo> getSkills(Long userId) {
        String cacheKey = CACHE_PREFIX + "skills:" + userId;
        List<SkillCategoryVo> cached = redisCache.getCacheObject(cacheKey);
        if (cached != null) {
            return cached;
        }

        // 复用已有的UserSkillService查询
        List<UserSkillVo> userSkills = userSkillService.selectUserSkillsByUserId(userId);

        Map<String, String> categoryNameMap = new HashMap<>();
        categoryNameMap.put("tech", "技术");
        categoryNameMap.put("design", "设计");
        categoryNameMap.put("business", "商业");
        categoryNameMap.put("other", "其他");

        // 按分类分组
        Map<String, List<UserSkillVo>> grouped = userSkills.stream()
                .collect(Collectors.groupingBy(s -> s.getCategory() != null ? s.getCategory() : "other"));

        List<SkillCategoryVo> result = new ArrayList<>();
        for (Map.Entry<String, List<UserSkillVo>> entry : grouped.entrySet()) {
            List<SkillCategoryVo.SkillItem> items = entry.getValue().stream()
                    .map(s -> SkillCategoryVo.SkillItem.builder()
                            .skillTagId(s.getSkillTagId())
                            .tagName(s.getTagName())
                            .source(s.getSource())
                            .confidence(s.getConfidence())
                            .build())
                    .sorted(Comparator.comparing(SkillCategoryVo.SkillItem::getConfidence,
                            Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());

            result.add(SkillCategoryVo.builder()
                    .category(entry.getKey())
                    .categoryName(categoryNameMap.getOrDefault(entry.getKey(), entry.getKey()))
                    .skills(items)
                    .skillCount(items.size())
                    .build());
        }

        // 按固定顺序排列: tech, design, business, other
        List<String> order = Arrays.asList("tech", "design", "business", "other");
        result.sort(Comparator.comparingInt(c -> {
            int idx = order.indexOf(c.getCategory());
            return idx >= 0 ? idx : order.size();
        }));

        redisCache.setCacheObject(cacheKey, result, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        return result;
    }

    @Override
    public DashboardSummaryVo getSummary(Long userId) {
        return DashboardSummaryVo.builder()
                .overview(getOverview(userId))
                .growthCurve(getGrowthCurve(userId))
                .ranking(getRanking(userId))
                .skills(getSkills(userId))
                .build();
    }

    private DashboardRankingVo.RankItem buildRankItem(Map<String, Object> result, String scope) {
        if (result == null) {
            return DashboardRankingVo.RankItem.builder()
                    .rank(0).total(0).scope(scope).build();
        }
        Object rankObj = result.get("rank");
        Object totalObj = result.get("total");
        return DashboardRankingVo.RankItem.builder()
                .rank(rankObj != null ? ((Number) rankObj).intValue() : 0)
                .total(totalObj != null ? ((Number) totalObj).intValue() : 0)
                .scope(scope)
                .build();
    }
    @Override
    public List<RaceReminderVo> getRaceReminders() {
        String cacheKey = CACHE_PREFIX + "reminders";
        List<RaceReminderVo> cached = redisCache.getCacheObject(cacheKey);
        if (cached != null) {
            return cached;
        }

        List<RaceReminderVo> list = dashboardMapper.selectRaceReminders();
        redisCache.setCacheObject(cacheKey, list, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        return list;
    }
    @Override
    public List<GrowthEventVo> getGrowthTimeline(Long userId) {
        String cacheKey = CACHE_PREFIX + "timeline:" + userId;
        List<GrowthEventVo> cached = redisCache.getCacheObject(cacheKey);
        if (cached != null) {
            return cached;
        }

        List<GrowthEventVo> list = dashboardMapper.selectGrowthTimeline(userId);
        redisCache.setCacheObject(cacheKey, list, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        return list;
    }

    @Override
    public List<AwardRecordVo> getAwardRecords(Long userId) {
        String cacheKey = CACHE_PREFIX + "awards:" + userId;
        List<AwardRecordVo> cached = redisCache.getCacheObject(cacheKey);
        if (cached != null) {
            return cached;
        }

        List<AwardRecordVo> list = dashboardMapper.selectAwardRecords(userId);
        redisCache.setCacheObject(cacheKey, list, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        return list;
    }

    @Override
    public List<ProjectRecordVo> getProjectRecords(Long userId) {
        String cacheKey = CACHE_PREFIX + "projects:" + userId;
        List<ProjectRecordVo> cached = redisCache.getCacheObject(cacheKey);
        if (cached != null) {
            return cached;
        }

        List<ProjectRecordVo> list = dashboardMapper.selectProjectRecords(userId);
        redisCache.setCacheObject(cacheKey, list, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        return list;
    }

    @Override
    public GrowthReportVo getGrowthReport(Long userId) {
        String cacheKey = CACHE_PREFIX + "report:" + userId;
        GrowthReportVo cached = redisCache.getCacheObject(cacheKey);
        if (cached != null) {
            return cached;
        }

        // 获取用户信息
        UserInfoVo userInfo = userInfoService.selectUserInfoById(userId);

        // 获取统计数据
        ReportStatsVo stats = ReportStatsVo.builder()
                .raceCount(dashboardMapper.countUserRaces(userId))
                .awardCount(dashboardMapper.countUserAwards(userId))
                .projectCount(dashboardMapper.countUserProjects(userId))
                .compositeScore(getRanking(userId).getCompositeScore().doubleValue())
                .build();

        // 获取详细记录
        List<AwardRecordVo> awards = getAwardRecords(userId);
        List<ProjectRecordVo> projects = getProjectRecords(userId);
        List<GrowthEventVo> races = getGrowthTimeline(userId).stream()
                .filter(event -> "race".equals(event.getEventType()))
                .collect(Collectors.toList());

        GrowthReportVo report = GrowthReportVo.builder()
                .userInfo(userInfo)
                .stats(stats)
                .awards(awards)
                .projects(projects)
                .races(races)
                .build();

        redisCache.setCacheObject(cacheKey, report, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        return report;
    }
}
