package com.buis.service.extract;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ResumeKeywordExtractorTest {
    ResumeKeywordExtractor extractor=new ResumeKeywordExtractor();

    @BeforeEach
    void setUp() {
        extractor = new ResumeKeywordExtractor();
    }

    @Test
    void testExtractSkillsWithNullResume() {
        List<String> result = extractor.extractSkills(null, "计算机科学");
        assertTrue(result.isEmpty());
    }

    @Test
    void testExtractSkillsWithEmptyResume() {
        List<String> result = extractor.extractSkills("", "软件工程");
        assertTrue(result.isEmpty());
    }

    @Test
    void testExtractBasicSkills() {
        String resume = "我熟悉Java和Python编程";
        List<String> result = extractor.extractSkills(resume, "软件工程");

        // 应该提取Java和Python
        assertTrue(result.contains("java"), "应包含Java");
        assertTrue(result.contains("python"), "应包含Python");
    }

    @Test
    void testExtractSkillsByPattern() {
        String resume = "掌握Spring Boot框架，使用MySQL数据库";
        List<String> result = extractor.extractSkills(resume, "后端开发");

        assertTrue(result.contains("springboot"), "应包含Spring Boot");
        assertTrue(result.contains("mysql"), "应包含MySQL");
    }

    @Test
    void testExtractMultipleSkills() {
        String resume = "精通Java和Spring Boot，熟悉MySQL、Redis和Docker，了解Kubernetes";
        List<String> result = extractor.extractSkills(resume, "后端开发");

        // 验证关键技能
        assertTrue(result.contains("java"), "应包含Java");
        assertTrue(result.contains("springboot"), "应包含Spring Boot");
        assertTrue(result.contains("mysql"), "应包含MySQL");
        assertTrue(result.contains("redis"), "应包含Redis");
        assertTrue(result.contains("docker"), "应包含Docker");
        assertTrue(result.contains("kubernetes"), "应包含Kubernetes");
    }

    @Test
    void testComplexResumeExtraction() {
        String resume = "具有5年Java开发经验，精通Spring Boot、Spring Cloud微服务架构。" +
                "熟悉MySQL、Redis、MongoDB等数据库，熟练使用Docker和Kubernetes进行容器化部署。" +
                "了解前端技术Vue.js和React，有移动端React Native开发经验。" +
                "掌握消息队列Kafka和RabbitMQ。";

        List<String> result = extractor.extractSkills(resume, "软件工程");

        // 打印实际提取的技能，便于调试
        System.out.println("提取的技能: " + result);

        // 检查一些关键技能
        assertTrue(result.contains("java"), "应包含Java");
        assertTrue(result.contains("springboot"), "应包含Spring Boot");
        assertTrue(result.contains("mysql") || result.contains("mongodb"), "应包含数据库技能");
        assertTrue(result.contains("docker") || result.contains("kubernetes"), "应包含容器化技能");
    }

    @Test
    void testSkillNormalization() {
        String resume = "熟悉Spring Boot和React Native开发";
        List<String> result = extractor.extractSkills(resume, "后端开发");

        assertTrue(result.contains("springboot"), "应包含springboot");
        assertTrue(result.contains("reactnative"), "应包含reactnative");
    }

    @Test
    void testNoSkillsUseMajor() {
        String resume = "热爱编程，学习能力强";
        List<String> result = extractor.extractSkills(resume, "软件工程");

        // 应该使用专业基础技能
        assertFalse(result.isEmpty());
        assertTrue(result.contains("软件开发") || result.contains("软件测试") ||
                result.contains("项目管理") || result.contains("设计模式"));
    }

    @Test
    void testCalculateSkillSimilarity() {
        List<String> skills1 = Arrays.asList("java", "springboot", "mysql");
        List<String> skills2 = Arrays.asList("java", "mysql", "redis");

        double similarity = extractor.calculateSkillSimilarity(skills1, skills2);

        // 交集: java, mysql (2个)
        // 并集: java, springboot, mysql, redis (4个)
        // 相似度: 2/4 = 0.5
        assertEquals(0.5, similarity, 0.01);
    }
}
