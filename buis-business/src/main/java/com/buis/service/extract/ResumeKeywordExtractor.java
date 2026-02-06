package com.buis.service.extract;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 简历关键词提取器
 */

@Component
@Slf4j
public class ResumeKeywordExtractor {
    // 技能关键词词典（使用简单的小写形式）
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
            "项目管理", "软件测试", "设计模式"
    ));

    // 专业基础技能映射（只在没有技能时使用）
    private static final Map<String, List<String>> MAJOR_SKILLS = new HashMap<>();
    static {
        MAJOR_SKILLS.put("计算机科学与技术", Arrays.asList("算法", "数据结构", "操作系统", "计算机网络"));
        MAJOR_SKILLS.put("软件工程", Arrays.asList("软件开发", "软件测试", "项目管理", "设计模式"));
        MAJOR_SKILLS.put("网络工程", Arrays.asList("网络协议", "网络安全", "网络管理"));
        MAJOR_SKILLS.put("信息安全", Arrays.asList("网络安全", "系统安全", "密码学"));
        MAJOR_SKILLS.put("电子信息工程", Arrays.asList("电路设计", "嵌入式", "单片机"));
        MAJOR_SKILLS.put("通信工程", Arrays.asList("通信原理", "无线通信", "信号处理"));
        MAJOR_SKILLS.put("数据科学与大数据技术", Arrays.asList("数据分析", "数据挖掘", "机器学习"));
        MAJOR_SKILLS.put("统计学", Arrays.asList("统计分析", "概率论", "数据建模"));
        MAJOR_SKILLS.put("数字媒体技术", Arrays.asList("ui设计", "平面设计", "3d建模"));
        MAJOR_SKILLS.put("艺术设计", Arrays.asList("视觉设计", "交互设计", "创意设计"));
    }

    /**
     * 从简历中提取技能关键词
     * @param resume 简历文本
     * @param major 专业
     * @return 技能关键词列表
     */
    public List<String> extractSkills(String resume, String major) {
        if (resume == null || resume.trim().isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> skills = new HashSet<>();
        String lowerResume = resume.toLowerCase();

        // 1. 直接关键词匹配（最核心的方法）
        for (String skill : SKILL_KEYWORDS) {
            // 简单的包含匹配，对于有空格或特殊字符的，用更灵活的方式
            if (skill.contains("+") || skill.contains("#")) {
                // 对于c++、c#等特殊技能
                if (skill.equals("c++") && lowerResume.contains("c++")) {
                    skills.add("c++");
                } else if (skill.equals("c#") && lowerResume.contains("c#")) {
                    skills.add("c#");
                }
            } else if (skill.equals("reactnative")) {
                // 特殊处理React Native
                if (lowerResume.contains("react native") || lowerResume.contains("react-native")) {
                    skills.add("reactnative");
                }
            } else if (skill.equals("springboot")) {
                // 特殊处理Spring Boot
                if (lowerResume.contains("spring boot") || lowerResume.contains("spring-boot")) {
                    skills.add("springboot");
                }
            } else if (skill.equals("springcloud")) {
                // 特殊处理Spring Cloud
                if (lowerResume.contains("spring cloud") || lowerResume.contains("spring-cloud")) {
                    skills.add("springcloud");
                }
            } else if (skill.equals("vue")) {
                // 特殊处理Vue.js
                if (lowerResume.contains("vue") || lowerResume.contains("vue.js")) {
                    skills.add("vue");
                }
            } else if (skill.equals("react")) {
                // 特殊处理React.js
                if (lowerResume.contains("react") || lowerResume.contains("react.js")) {
                    skills.add("react");
                }
            } else {
                // 普通技能直接匹配
                if (lowerResume.contains(skill)) {
                    skills.add(skill);
                }
            }
        }

        // 2. 使用简单的正则匹配技能描述
        extractSkillsByPattern(lowerResume, skills);

        // 3. 如果没提取到技能，使用专业基础技能
        if (skills.isEmpty() && major != null && !major.trim().isEmpty()) {
            List<String> majorSkills = MAJOR_SKILLS.get(major);
            if (majorSkills != null) {
                skills.addAll(majorSkills);
            } else {
                skills.add(major);
            }
        }

        // 4. 排序并返回
        List<String> result = new ArrayList<>(skills);
        Collections.sort(result);
        return result;
    }

    /**
     * 使用正则表达式匹配技能描述
     */
    private void extractSkillsByPattern(String text, Set<String> skills) {
        // 匹配"掌握/熟悉/了解 XXX"模式
        Pattern pattern = Pattern.compile("(掌握|熟悉|了解|精通|擅长)\\s*([^，。,.；;!！?？]{2,15})");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String skillCandidate = matcher.group(2).trim();
            // 检查是否为已知技能
            String cleanedSkill = cleanSkillTerm(skillCandidate);
            if (SKILL_KEYWORDS.contains(cleanedSkill)) {
                skills.add(cleanedSkill);
            }
        }

        // 匹配"使用/基于 XXX"模式
        pattern = Pattern.compile("(使用|采用|基于)\\s*([^，。,.；;!！?？]{2,15})");
        matcher = pattern.matcher(text);

        while (matcher.find()) {
            String skillCandidate = matcher.group(2).trim();
            String cleanedSkill = cleanSkillTerm(skillCandidate);
            if (SKILL_KEYWORDS.contains(cleanedSkill)) {
                skills.add(cleanedSkill);
            }
        }
    }

    /**
     * 清理技能术语
     */
    private String cleanSkillTerm(String term) {
        String cleaned = term.toLowerCase();

        // 移除常见后缀
        cleaned = cleaned.replaceAll("(技术|框架|语言|工具|系统|平台|软件|编程|开发)$", "");

        // 处理特殊技能名称
        if (cleaned.contains("spring boot") || cleaned.contains("spring-boot")) {
            return "springboot";
        } else if (cleaned.contains("spring cloud") || cleaned.contains("spring-cloud")) {
            return "springcloud";
        } else if (cleaned.contains("react native") || cleaned.contains("react-native")) {
            return "reactnative";
        } else if (cleaned.contains("vue.js") || cleaned.contains("vuejs")) {
            return "vue";
        } else if (cleaned.contains("react.js") || cleaned.contains("reactjs")) {
            return "react";
        } else if (cleaned.contains("node.js") || cleaned.contains("nodejs")) {
            return "nodejs";
        }

        // 移除空格
        cleaned = cleaned.replaceAll("\\s+", "");

        return cleaned.trim();
    }

    /**
     * 提取技能及掌握程度（可选功能）
     */
    public Map<String, Integer> extractSkillsWithLevel(String resume) {
        Map<String, Integer> result = new HashMap<>();

        if (resume == null || resume.trim().isEmpty()) {
            return result;
        }

        // 先提取技能
        List<String> skills = extractSkills(resume, null);
        String lowerResume = resume.toLowerCase();

        // 检查每个技能的掌握程度
        for (String skill : skills) {
            int level = 0;

            // 检查是否有掌握程度描述
            if (lowerResume.contains("精通" + skill) || lowerResume.contains("精通 " + skill)) {
                level = 3;
            } else if (lowerResume.contains("熟练" + skill) || lowerResume.contains("熟悉" + skill) ||
                    lowerResume.contains("掌握" + skill) || lowerResume.contains("熟练 " + skill)) {
                level = 2;
            } else if (lowerResume.contains("了解" + skill) || lowerResume.contains("了解 " + skill)) {
                level = 1;
            }

            if (level > 0) {
                result.put(skill, level);
            }
        }

        return result;
    }

    /**
     * 计算技能相似度
     */
    public double calculateSkillSimilarity(List<String> skills1, List<String> skills2) {
        if (skills1.isEmpty() && skills2.isEmpty()) {
            return 0.0;
        }

        Set<String> set1 = new HashSet<>(skills1);
        Set<String> set2 = new HashSet<>(skills2);

        // 交集
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        // 并集
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }

}
