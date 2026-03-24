package com.buis.web.controller.api;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.buis.common.core.domain.R;
import com.buis.common.manager.RedisLimiterManager;
import com.buis.common.utils.SecurityUtils;
import com.buis.model.vo.*;
import com.buis.service.IAiService;
import com.buis.service.IDashboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 个人大数据面板Api
 */
@Api(tags = "个人大数据面板Api")
@Slf4j
@RestController
@RequestMapping("/api/dashboard")
public class DashboardApiController {

    @Resource
    private IDashboardService dashboardService;

    @Resource
    private IAiService aiService;

    @Resource
    private RedisLimiterManager redisLimiterManager;

    @ApiOperation("获取数据概览")
    @GetMapping("/overview")
    public R<DashboardOverviewVo> getOverview() {
        Long userId = SecurityUtils.getFrontLoginUserId();
        return R.ok(dashboardService.getOverview(userId));
    }

    @ApiOperation("获取成长曲线")
    @GetMapping("/growthCurve")
    public R<List<GrowthCurveItemVo>> getGrowthCurve() {
        Long userId = SecurityUtils.getFrontLoginUserId();
        return R.ok(dashboardService.getGrowthCurve(userId));
    }

    @ApiOperation("获取排名分析")
    @GetMapping("/ranking")
    public R<DashboardRankingVo> getRanking() {
        Long userId = SecurityUtils.getFrontLoginUserId();
        return R.ok(dashboardService.getRanking(userId));
    }

    @ApiOperation("获取能力标签")
    @GetMapping("/skills")
    public R<List<SkillCategoryVo>> getSkills() {
        Long userId = SecurityUtils.getFrontLoginUserId();
        return R.ok(dashboardService.getSkills(userId));
    }

    @ApiOperation("获取个人面板汇总数据")
    @GetMapping("/summary")
    public R<DashboardSummaryVo> getSummary() {
        Long userId = SecurityUtils.getFrontLoginUserId();
        return R.ok(dashboardService.getSummary(userId));
    }

    @ApiOperation("获取赛事提醒")
    @GetMapping("/reminders")
    public R<List<RaceReminderVo>> getReminders() {
        return R.ok(dashboardService.getRaceReminders());
    }

    @ApiOperation("获取个人成长时间轴")
    @GetMapping("/timeline")
    public R<List<GrowthEventVo>> getGrowthTimeline() {
        Long userId = SecurityUtils.getFrontLoginUserId();
        return R.ok(dashboardService.getGrowthTimeline(userId));
    }

    @ApiOperation("获取获奖档案")
    @GetMapping("/archives/awards")
    public R<List<AwardRecordVo>> getAwardRecords() {
        Long userId = SecurityUtils.getFrontLoginUserId();
        return R.ok(dashboardService.getAwardRecords(userId));
    }

    @ApiOperation("获取项目档案")
    @GetMapping("/archives/projects")
    public R<List<ProjectRecordVo>> getProjectRecords() {
        Long userId = SecurityUtils.getFrontLoginUserId();
        return R.ok(dashboardService.getProjectRecords(userId));
    }

    @ApiOperation("获取成果明细")
    @GetMapping("/achievements/items")
    public R<List<AchievementItemVo>> getAchievementItems() {
        Long userId = SecurityUtils.getFrontLoginUserId();
        return R.ok(dashboardService.getAchievementItems(userId));
    }

    @ApiOperation("获取成长报告数据")
    @GetMapping("/report")
    public R<GrowthReportVo> getGrowthReport() {
        Long userId = SecurityUtils.getFrontLoginUserId();
        return R.ok(dashboardService.getGrowthReport(userId));
    }

    @ApiOperation("获取综合评分")
    @GetMapping("/compositeScore")
    public R<Double> getCompositeScore() {
        Long userId = SecurityUtils.getFrontLoginUserId();
        return R.ok(dashboardService.getCompositeScore(userId));
    }

    @ApiOperation("获取成果统计")
    @GetMapping("/achievements")
    public R<AchievementStatsVo> getAchievementStats() {
        Long userId = SecurityUtils.getFrontLoginUserId();
        return R.ok(dashboardService.getAchievementStats(userId));
    }

    @ApiOperation("获取分布数据")
    @GetMapping("/distribution")
    public R<DistributionVo> getDistributionData() {
        Long userId = SecurityUtils.getFrontLoginUserId();
        return R.ok(dashboardService.getDistributionData(userId));
    }

    @ApiOperation("AI学生数据分析与发展建议(流式)")
    @GetMapping(value = "/ai/analysis", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getAiAnalysis(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");

        Long userId = SecurityUtils.getFrontLoginUserId();

        // 限流: 每用户独立限流器
        redisLimiterManager.doRateLimit("dashboard_ai_analysis_" + userId);

        try {
            // 1. 聚合面板数据(全部走Redis缓存，无额外DB压力)
            GrowthReportVo report = dashboardService.getGrowthReport(userId);
            DashboardRankingVo ranking = dashboardService.getRanking(userId);
            List<SkillCategoryVo> skills = dashboardService.getSkills(userId);

            // 2. 构建AI分析提示词
            String prompt = buildAnalysisPrompt(report, ranking, skills);

            // 3. 调用AI流式接口
            Generation gen = new Generation();
            Message userMsg = Message.builder()
                    .role(Role.USER.getValue())
                    .content(prompt)
                    .build();

            return aiService.streamCallWithMessage(gen, userMsg);

        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            log.error("AI分析调用失败: {}", e.getMessage(), e);
            return Flux.just("[ERROR] AI服务暂时不可用，请稍后重试");
        } catch (Exception e) {
            log.error("获取AI分析失败: {}", e.getMessage(), e);
            return Flux.just("[ERROR] " + e.getMessage());
        }
    }

    /**
     * 构建AI学生分析提示词
     */
    private String buildAnalysisPrompt(GrowthReportVo report,
                                       DashboardRankingVo ranking,
                                       List<SkillCategoryVo> skills) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        sb.append("你是一位资深的大学生竞赛发展顾问和职业规划专家。请根据以下学生的竞赛大数据面板信息，");
        sb.append("进行全面深入的分析，并给出个性化的发展建议。篇幅不要过长，简明精要\n\n");

        // === 基本信息 ===
        sb.append("## 一、学生基本信息\n");
        UserInfoVo user = report.getUserInfo();
        if (user != null) {
            sb.append("- 姓名：").append(nullSafe(user.getName())).append("\n");
            sb.append("- 学院：").append(nullSafe(user.getCollegeName())).append("\n");
            sb.append("- 专业：").append(nullSafe(user.getMajorName())).append("\n");
            sb.append("- 年级：").append(nullSafe(user.getStuGrade())).append("\n");
            sb.append("- 班级：").append(nullSafe(user.getStuClass())).append("\n");
            if (user.getDescription() != null && !user.getDescription().isEmpty()) {
                sb.append("- 个人简介：").append(user.getDescription()).append("\n");
            }
        }

        // === 统计概览 ===
        sb.append("\n## 二、竞赛统计概览\n");
        ReportStatsVo stats = report.getStats();
        if (stats != null) {
            sb.append("- 参赛总数：").append(stats.getRaceCount()).append("次\n");
            sb.append("- 获奖总数：").append(stats.getAwardCount()).append("次\n");
            sb.append("- 参与项目数：").append(stats.getProjectCount()).append("个\n");
            sb.append("- 综合评分：").append(stats.getCompositeScore()).append("\n");
        }

        // === 排名信息 ===
        sb.append("\n## 三、排名与竞争力\n");
        if (ranking != null) {
            sb.append("- 综合评分：").append(ranking.getCompositeScore()).append("\n");
            sb.append("- 百分位排名：超过了").append(ranking.getPercentile()).append("%的同年级学生\n");
            if (ranking.getGradeRank() != null) {
                sb.append("- 年级排名：第").append(ranking.getGradeRank().getRank())
                        .append("名 / 共").append(ranking.getGradeRank().getTotal())
                        .append("人（").append(nullSafe(ranking.getGradeRank().getScope())).append("）\n");
            }
            if (ranking.getCollegeRank() != null) {
                sb.append("- 学院排名：第").append(ranking.getCollegeRank().getRank())
                        .append("名 / 共").append(ranking.getCollegeRank().getTotal())
                        .append("人（").append(nullSafe(ranking.getCollegeRank().getScope())).append("）\n");
            }
            // 获奖等级分布
            if (ranking.getAwardLevelDistribution() != null && !ranking.getAwardLevelDistribution().isEmpty()) {
                sb.append("- 获奖等级分布：");
                sb.append(ranking.getAwardLevelDistribution().stream()
                        .map(item -> item.getAwardLevelName() + item.getCount() + "项")
                        .collect(Collectors.joining("、")));
                sb.append("\n");
            }
            // 比赛类型分布
            if (ranking.getRaceCategoryDistribution() != null && !ranking.getRaceCategoryDistribution().isEmpty()) {
                sb.append("- 比赛类型分布：");
                sb.append(ranking.getRaceCategoryDistribution().stream()
                        .map(item -> item.getRaceTypeName() + item.getCount() + "次")
                        .collect(Collectors.joining("、")));
                sb.append("\n");
            }
        }

        // === 获奖记录 ===
        sb.append("\n## 四、获奖记录\n");
        List<AwardRecordVo> awards = report.getAwards();
        if (awards != null && !awards.isEmpty()) {
            for (AwardRecordVo award : awards) {
                sb.append("- 【").append(nullSafe(award.getAwardLevel())).append("】")
                        .append(nullSafe(award.getAwardName()))
                        .append("（比赛：").append(nullSafe(award.getRaceName())).append("）");
                if (award.getAwardTime() != null) {
                    sb.append("，获奖时间：").append(sdf.format(award.getAwardTime()));
                }
                sb.append("\n");
            }
        } else {
            sb.append("暂无获奖记录\n");
        }

        // === 项目经历 ===
        sb.append("\n## 五、项目经历\n");
        List<ProjectRecordVo> projects = report.getProjects();
        if (projects != null && !projects.isEmpty()) {
            for (ProjectRecordVo project : projects) {
                sb.append("- ").append(nullSafe(project.getProjectName()))
                        .append("（比赛：").append(nullSafe(project.getRaceName())).append("）");
                if (project.getCreateTime() != null) {
                    sb.append("，创建时间：").append(sdf.format(project.getCreateTime()));
                }
                sb.append("\n");
            }
        } else {
            sb.append("暂无项目记录\n");
        }

        // === 成长轨迹 ===
        sb.append("\n## 六、成长轨迹\n");
        List<GrowthEventVo> races = report.getRaces();
        if (races != null && !races.isEmpty()) {
            for (GrowthEventVo event : races) {
                sb.append("- ").append(nullSafe(event.getEventName()));
                if (event.getEventDescription() != null) {
                    sb.append("：").append(event.getEventDescription());
                }
                if (event.getEventTime() != null) {
                    sb.append("（").append(sdf.format(event.getEventTime())).append("）");
                }
                sb.append("\n");
            }
        } else {
            sb.append("暂无成长轨迹记录\n");
        }

        // === 能力标签 ===
        sb.append("\n## 七、能力标签\n");
        if (skills != null && !skills.isEmpty()) {
            for (SkillCategoryVo category : skills) {
                sb.append("### ").append(category.getCategoryName())
                        .append("（共").append(category.getSkillCount()).append("项）\n");
                if (category.getSkills() != null) {
                    for (SkillCategoryVo.SkillItem skill : category.getSkills()) {
                        sb.append("- ").append(skill.getTagName());
                        if (skill.getConfidence() != null) {
                            sb.append("（置信度：").append(skill.getConfidence()).append("）");
                        }
                        sb.append("\n");
                    }
                }
            }
        } else {
            sb.append("暂无能力标签\n");
        }

        // === 分析要求 ===
        sb.append("\n## 请你从以下几个维度进行分析和建议：\n\n");
        sb.append("### 1. 竞赛表现综合评价\n");
        sb.append("评估该学生在竞赛中的整体表现，包括参赛频率、获奖率、获奖含金量、竞赛类型的多样性等。\n\n");
        sb.append("### 2. 技能画像分析\n");
        sb.append("根据技能标签和竞赛经历，分析该学生的核心竞争力、技能优势与短板，指出需要加强的技能领域。\n\n");
        sb.append("### 3. 成长趋势判断\n");
        sb.append("根据时间轴上的参赛和获奖记录，判断该学生的成长速度和趋势，是处于上升期、平台期还是需要转型。\n\n");
        sb.append("### 4. 短期目标建议（1-3个月）\n");
        sb.append("给出具体可执行的短期行动计划，包括推荐参加的竞赛类型、需要学习的技能等。\n\n");
        sb.append("### 5. 中期发展规划（3-12个月）\n");
        sb.append("基于当前基础规划中期发展路径，包括技能提升方向、竞赛升级策略、团队合作建议等。\n\n");
        sb.append("### 6. 长期职业方向建议\n");
        sb.append("结合该学生的专业背景、技能画像和竞赛经历，推荐适合的职业发展方向（如技术研发、产品经理、创业等），");
        sb.append("并说明理由。\n\n");
        sb.append("请用友好专业的语气，给出有针对性的、具体的建议，避免笼统空泛的表述。");
        sb.append("建议中可以引用该学生的实际数据来支撑你的分析。");

        return sb.toString();
    }

    private String nullSafe(String value) {
        return value != null ? value : "未知";
    }
}
