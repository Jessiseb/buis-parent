package com.buis.service.extract;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 队伍需求提取器
 */
@Component
@Slf4j
public class TeamRequirementExtractor {


    // 技能关键词词典（与简历提取器保持一致）
    private static final Set<String> SKILL_KEYWORDS = new HashSet<>(Arrays.asList(
            // 编程语言
            "java", "python", "javascript", "typescript", "c++", "c#", "go", "php",
            "swift", "kotlin", "html", "css", "sql", "r", "matlab", "scala", "ruby",

            // 前端技术
            "vue", "react", "angular", "jquery", "bootstrap", "webpack", "vite",

            // 后端框架
            "spring", "springboot", "springcloud", "mybatis", "hibernate", "django",
            "flask", "fastapi", "express", "koa", "nest", "dubbo", "netty",

            // 数据库
            "mysql", "oracle", "sqlserver", "postgresql", "mongodb", "redis",
            "elasticsearch", "kafka", "rabbitmq", "hbase", "cassandra",

            // 运维和云
            "docker", "kubernetes", "jenkins", "git", "maven", "gradle", "nginx",
            "linux", "aws", "azure", "gcp",

            // 移动开发
            "android", "ios", "flutter", "reactnative", "uniapp",

            // 大数据和AI
            "hadoop", "spark", "flink", "tensorflow", "pytorch", "机器学习",
            "深度学习", "数据分析", "数据挖掘", "自然语言处理", "计算机视觉",

            // 工具和方法
            "敏捷开发", "devops", "微服务", "restful", "grpc", "单元测试", "集成测试",
            "项目管理", "软件测试", "设计模式",

            // 前端设计相关
            "ui设计", "ux设计", "平面设计", "视觉设计", "交互设计", "原型设计",

            // 业务领域
            "金融", "电商", "医疗", "教育", "游戏", "社交", "物联网", "区块链",

            // 其他常见需求
            "前端开发", "后端开发", "全栈开发", "移动端开发", "测试工程师", "产品经理",
            "ui设计师", "项目经理", "需求分析", "系统架构"
    ));

    /**
     * 从队伍需求文本中提取技能关键词
     *
     * @param requirementText 队伍需求文本
     * @return 提取到的技能关键词列表（去重、排序）
     */
    public List<String> extractSkills(String requirementText) {
        if (requirementText == null || requirementText.trim().isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> skills = new HashSet<>();
        String lowerText = requirementText.toLowerCase();

        // 1. 直接关键词匹配（最核心的方法）
        for (String skill : SKILL_KEYWORDS) {
            // 处理特殊技能名称
            String processedSkill = processSpecialSkill(skill);
            String searchPattern = buildSearchPattern(processedSkill);

            if (containsSkill(lowerText, processedSkill, searchPattern)) {
                skills.add(skill);
            }
        }

        // 2. 使用正则表达式匹配技能描述模式
        extractSkillsByPattern(lowerText, skills);

        // 3. 排序并返回结果
        List<String> result = new ArrayList<>(skills);
        Collections.sort(result);
        return result;
    }

    /**
     * 处理特殊技能名称
     */
    private String processSpecialSkill(String skill) {
        // 处理特殊格式的技能名称
        switch (skill) {
            case "c++":
                return "c\\+\\+";
            case "c#":
                return "c#";
            case "reactnative":
                return "react[\\s-]*native|reactnative";
            case "springboot":
                return "spring[\\s-]*boot|springboot";
            case "springcloud":
                return "spring[\\s-]*cloud|springcloud";
            case "vue":
                return "vue(\\.js)?|vuejs";
            case "react":
                return "react(\\.js)?|reactjs";
            case "node.js":
                return "node(\\.js)?|nodejs";
            default:
                return skill;
        }
    }

    /**
     * 构建搜索模式
     */
    private String buildSearchPattern(String skill) {
        // 如果是英文技能，添加单词边界
        if (skill.matches("[a-zA-Z]+")) {
            return "\\b" + skill + "\\b";
        }
        return skill;
    }

    /**
     * 检查是否包含技能
     */
    private boolean containsSkill(String text, String skill, String pattern) {
        // 对于简单技能，直接使用contains
        if (!skill.contains("\\")) {
            return text.contains(skill);
        }

        // 对于需要正则匹配的技能
        Pattern regex = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = regex.matcher(text);
        return matcher.find();
    }

    /**
     * 使用正则表达式匹配技能描述模式
     */
    private void extractSkillsByPattern(String text, Set<String> skills) {
        // 模式1：匹配"需要/要求/掌握 XXX" 模式
        Pattern pattern1 = Pattern.compile("(需要|要求|掌握|熟悉|了解|精通)\\s*([^，。,.；;!！?？]{2,20})");
        extractSkillsWithPattern(text, skills, pattern1, 2);

        // 模式2：匹配"具备XXX能力/经验" 模式
        Pattern pattern2 = Pattern.compile("具备\\s*([^，。,.；;!！?？]{2,20})\\s*(能力|经验|技能)");
        extractSkillsWithPattern(text, skills, pattern2, 1);

        // 模式3：匹配"XXX开发/设计/测试" 模式
        Pattern pattern3 = Pattern.compile("([^，。,.；;!！?？]{2,10})(开发|设计|测试|工程师)");
        extractSkillsWithPattern(text, skills, pattern3, 1);
    }

    /**
     * 使用指定模式提取技能
     */
    private void extractSkillsWithPattern(String text, Set<String> skills, Pattern pattern, int groupIndex) {
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String candidate = matcher.group(groupIndex).trim().toLowerCase();

            // 清理候选词
            String cleaned = cleanSkillCandidate(candidate);

            // 检查是否为已知技能
            if (isKnownSkill(cleaned)) {
                skills.add(getOriginalSkillName(cleaned));
            }
        }
    }

    /**
     * 清理技能候选词
     */
    private String cleanSkillCandidate(String candidate) {
        // 移除常见后缀
        String cleaned = candidate
                .replaceAll("(开发|设计|测试|工程师|能力|经验|技能|知识|基础|熟悉|了解|掌握|精通|熟练)$", "")
                .replaceAll("\\s+", "")
                .trim();

        return cleaned;
    }

    /**
     * 检查是否为已知技能
     */
    private boolean isKnownSkill(String skill) {
        // 直接检查是否在关键词库中
        if (SKILL_KEYWORDS.contains(skill)) {
            return true;
        }

        // 检查是否为特殊技能的变体
        for (String knownSkill : SKILL_KEYWORDS) {
            String processed = processSpecialSkill(knownSkill);
            if (skill.matches(".*" + processed.replace("\\", "") + ".*")) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取原始技能名称
     */
    private String getOriginalSkillName(String cleanedSkill) {
        // 如果是已知技能，直接返回
        if (SKILL_KEYWORDS.contains(cleanedSkill)) {
            return cleanedSkill;
        }

        // 检查是否为特殊技能的变体
        for (String skill : SKILL_KEYWORDS) {
            String processed = processSpecialSkill(skill);
            String pattern = processed.replace("\\", "");
            if (cleanedSkill.matches(".*" + pattern + ".*")) {
                return skill;
            }
        }

        return cleanedSkill;
    }

    /**
     * 计算需求匹配度（简单版本）
     *
     * @param userSkills 用户技能列表
     * @param teamRequirements 队伍需求技能列表
     * @return 匹配度分数 0.0-1.0
     */
    public double calculateMatchScore(List<String> userSkills, List<String> teamRequirements) {
        if (teamRequirements == null || teamRequirements.isEmpty()) {
            return 0.0;
        }

        if (userSkills == null || userSkills.isEmpty()) {
            return 0.0;
        }

        Set<String> userSkillSet = new HashSet<>(userSkills);
        Set<String> requirementSet = new HashSet<>(teamRequirements);

        // 计算交集
        Set<String> intersection = new HashSet<>(userSkillSet);
        intersection.retainAll(requirementSet);

        // 计算匹配度：交集数量 / 需求数量
        return requirementSet.isEmpty() ? 0.0 : (double) intersection.size() / requirementSet.size();
    }
}
