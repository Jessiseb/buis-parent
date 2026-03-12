package com.buis.service.impl;

import com.buis.service.ITeamInfoService;
import com.buis.service.IUserInfoService;
import com.buis.service.extract.ResumeKeywordExtractor;
import com.buis.service.extract.TeamRequirementExtractor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 智能推荐功能测试类（JDK 8版本）
 * 用于测试队伍推荐功能的各个组件
 */

@SpringBootTest
@Transactional
@Rollback(false)  // 设置为false以便查看数据库变化
class TeamRecommendationTest {

    @Resource
    private ITeamInfoService teamInfoService;

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private ResumeKeywordExtractor resumeKeywordExtractor;

    @Resource
    private TeamRequirementExtractor teamRequirementExtractor;

    /**
     * 测试1：验证提取器是否能正常工作
     * 测试技能提取器从简历中提取关键词
     */
    @Test
    public void testResumeKeywordExtractor() {
        System.out.println("=== 测试1：简历关键词提取器 ===");

        // 测试用例1：包含多种技能的简历
        String resume1 = "熟练掌握Java和Spring Boot框架，有MySQL数据库使用经验，"
                + "熟悉前端Vue.js开发，了解Python和机器学习基础。";
        String major1 = "计算机科学与技术";

        List<String> skills1 = resumeKeywordExtractor.extractSkills(resume1, major1);
        System.out.println("测试用例1 - 复杂简历：");
        System.out.println("简历内容：" + resume1);
        System.out.println("提取技能：" + skills1);
        System.out.println("提取数量：" + skills1.size());
        System.out.println();

        // 测试用例2：简单简历
        String resume2 = "会前端开发，熟悉HTML、CSS、JavaScript";
        String major2 = "软件工程";

        List<String> skills2 = resumeKeywordExtractor.extractSkills(resume2, major2);
        System.out.println("测试用例2 - 简单简历：");
        System.out.println("简历内容：" + resume2);
        System.out.println("提取技能：" + skills2);
        System.out.println("提取数量：" + skills2.size());
        System.out.println();

        // 测试用例3：空简历
        String resume3 = "";
        String major3 = "网络工程";

        List<String> skills3 = resumeKeywordExtractor.extractSkills(resume3, major3);
        System.out.println("测试用例3 - 空简历：");
        System.out.println("简历内容：" + resume3);
        System.out.println("提取技能：" + skills3);
        System.out.println("提取数量：" + skills3.size());
        System.out.println("===========================\n");
    }

    /**
     * 测试2：验证队伍需求提取器是否能正常工作
     * 测试从队伍需求中提取关键词
     */
    @Test
    public void testTeamRequirementExtractor() {
        System.out.println("=== 测试2：队伍需求提取器 ===");

        // 测试用例1：复杂需求
        String requirement1 = "需要3名Java后端开发，熟练掌握Spring Boot和MyBatis，"
                + "有MySQL数据库优化经验，了解Redis缓存技术。前端需要2名Vue.js开发者，"
                + "熟悉Element UI，有移动端开发经验者优先。";

        List<String> requirements1 = teamRequirementExtractor.extractSkills(requirement1);
        System.out.println("测试用例1 - 复杂需求：");
        System.out.println("需求内容：" + requirement1);
        System.out.println("提取技能：" + requirements1);
        System.out.println("提取数量：" + requirements1.size());
        System.out.println();

        // 测试用例2：简单需求
        String requirement2 = "需要会Python和数据分析的成员";

        List<String> requirements2 = teamRequirementExtractor.extractSkills(requirement2);
        System.out.println("测试用例2 - 简单需求：");
        System.out.println("需求内容：" + requirement2);
        System.out.println("提取技能：" + requirements2);
        System.out.println("提取数量：" + requirements2.size());
        System.out.println();

        // 测试用例3：空需求
        String requirement3 = "";

        List<String> requirements3 = teamRequirementExtractor.extractSkills(requirement3);
        System.out.println("测试用例3 - 空需求：");
        System.out.println("需求内容：" + requirement3);
        System.out.println("提取技能：" + requirements3);
        System.out.println("提取数量：" + requirements3.size());
        System.out.println("===========================\n");
    }

    /**
     * 测试3：验证匹配度计算功能
     */
    @Test
    public void testMatchScoreCalculation() {
        System.out.println("=== 测试3：匹配度计算 ===");

        // 用户技能
        List<String> userSkills = Arrays.asList("java", "springboot", "mysql", "vue");

        // 队伍需求1：完全匹配
        List<String> teamRequirements1 = Arrays.asList("java", "springboot", "mysql", "vue", "redis");

        // 队伍需求2：部分匹配
        List<String> teamRequirements2 = Arrays.asList("python", "django", "mysql");

        // 队伍需求3：无匹配
        List<String> teamRequirements3 = Arrays.asList("python", "tensorflow", "pytorch");

        // 队伍需求4：空需求
        List<String> teamRequirements4 = Arrays.asList();

        double score1 = teamRequirementExtractor.calculateMatchScore(userSkills, teamRequirements1);
        double score2 = teamRequirementExtractor.calculateMatchScore(userSkills, teamRequirements2);
        double score3 = teamRequirementExtractor.calculateMatchScore(userSkills, teamRequirements3);
        double score4 = teamRequirementExtractor.calculateMatchScore(userSkills, teamRequirements4);

        System.out.println("用户技能：" + userSkills);
        System.out.println("队伍需求1（完全匹配）：" + teamRequirements1 + " -> 匹配度：" + score1);
        System.out.println("队伍需求2（部分匹配）：" + teamRequirements2 + " -> 匹配度：" + score2);
        System.out.println("队伍需求3（无匹配）：" + teamRequirements3 + " -> 匹配度：" + score3);
        System.out.println("队伍需求4（空需求）：" + teamRequirements4 + " -> 匹配度：" + score4);
        System.out.println("===========================\n");
    }
}