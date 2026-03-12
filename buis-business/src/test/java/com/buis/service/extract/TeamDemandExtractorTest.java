package com.buis.service.extract;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


class TeamRequirementExtractorTest {

    private TeamRequirementExtractor extractor;

    @BeforeEach
    void setUp() {
        extractor = new TeamRequirementExtractor();
    }

    @Test
    @DisplayName("测试空或null文本处理")
    void testNullAndEmptyText() {
        assertAll(
                () -> assertTrue(extractor.extractSkills(null).isEmpty()),
                () -> assertTrue(extractor.extractSkills("").isEmpty()),
                () -> assertTrue(extractor.extractSkills("   ").isEmpty()),
                () -> assertEquals(0.0, extractor.calculateMatchScore(null, Arrays.asList("java"))),
                () -> assertEquals(0.0, extractor.calculateMatchScore(Arrays.asList("java"), null)),
                () -> assertEquals(0.0, extractor.calculateMatchScore(new ArrayList<>(), Arrays.asList("java"))),
                () -> assertEquals(0.0, extractor.calculateMatchScore(Arrays.asList("java"), new ArrayList<>()))
        );
    }

    @Test
    @DisplayName("测试基本技能提取")
    void testBasicSkillExtraction() {
        // Given
        String demandText = "需要Java开发人员";

        // When
        List<String> skills = extractor.extractSkills(demandText);

        // Then
        assertAll(
                () -> assertEquals(1, skills.size()),
                () -> assertEquals("java", skills.get(0)),
                () -> assertTrue(skills.contains("java"))
        );
    }

    // ==================== 技能提取测试 ====================

    @Test
    @DisplayName("测试多技能提取")
    void testMultipleSkillsExtraction() {
        // Given
        String demandText = "需要Java后端和Python数据分析，熟悉MySQL和Redis";

        // When
        List<String> skills = extractor.extractSkills(demandText);

        // Then
        System.out.println("多技能提取结果: " + skills);
        assertAll(
                () -> assertTrue(skills.contains("java")),
                () -> assertTrue(skills.contains("python")),
                () -> assertTrue(skills.contains("mysql")),
                () -> assertTrue(skills.contains("redis")),
                () -> assertTrue(skills.size() >= 4)
        );
    }

    @Test
    @DisplayName("测试特殊字符技能")
    void testSpecialCharacterSkills() {
        // Given
        String demandText1 = "需要C++和C#开发人员";
        String demandText2 = "需要Spring Boot和React Native开发";
        String demandText3 = "需要Vue.js和Node.js开发";

        // When
        List<String> skills1 = extractor.extractSkills(demandText1);
        List<String> skills2 = extractor.extractSkills(demandText2);
        List<String> skills3 = extractor.extractSkills(demandText3);

        // Then
        assertAll(
                () -> assertTrue(skills1.contains("c++"), "应该包含c++: " + skills1),
                () -> assertTrue(skills1.contains("c#"), "应该包含c#: " + skills1),
                () -> assertTrue(skills2.contains("springboot") || skills2.contains("spring"),
                        "应该包含springboot或spring: " + skills2),
                () -> assertTrue(skills2.contains("reactnative") || skills2.contains("react"),
                        "应该包含reactnative或react: " + skills2),
                () -> assertTrue(skills3.contains("vue"), "应该包含vue: " + skills3)
        );
    }

    @Test
    @DisplayName("测试中英文混合文本")
    void testChineseEnglishMixedText() {
        // Given
        String demandText = "需要Java开发engineer，熟悉Spring Boot框架，了解MySQL database，掌握React Native技术";

        // When
        List<String> skills = extractor.extractSkills(demandText);

        // Then
        System.out.println("中英文混合提取结果: " + skills);
        assertAll(
                () -> assertTrue(skills.contains("java")),
                () -> assertTrue(skills.contains("springboot") || skills.contains("spring")),
                () -> assertTrue(skills.contains("mysql")),
                () -> assertTrue(skills.contains("reactnative") || skills.contains("react"))
        );
    }

    @Test
    @DisplayName("测试设计相关技能")
    void testDesignSkills() {
        // Given
        String demandText = "需要UI设计、UX设计、平面设计和视觉设计";

        // When
        List<String> skills = extractor.extractSkills(demandText);

        // Then
        System.out.println("设计技能提取结果: " + skills);
        assertAll(
                () -> assertTrue(skills.contains("ui设计")),
                () -> assertTrue(skills.contains("ux设计")),
                () -> assertTrue(skills.contains("平面设计")),
                () -> assertTrue(skills.contains("视觉设计"))
        );
    }

    @Test
    @DisplayName("测试业务领域技能")
    void testBusinessDomainSkills() {
        // Given
        String demandText = "需要金融、电商、医疗和教育领域的开发经验";

        // When
        List<String> skills = extractor.extractSkills(demandText);

        // Then
        System.out.println("业务领域技能提取结果: " + skills);
        assertAll(
                () -> assertTrue(skills.contains("金融")),
                () -> assertTrue(skills.contains("电商")),
                () -> assertTrue(skills.contains("医疗")),
                () -> assertTrue(skills.contains("教育"))
        );
    }

    // ==================== 正则模式匹配测试 ====================

    @Test
    @DisplayName("测试正则模式1：需要/要求/掌握 XXX")
    void testPattern1Extraction() {
        // Given
        String[] testCases = {
                "需要Java开发",
                "要求Python编程",
                "掌握Spring框架",
                "熟悉MySQL数据库",
                "了解Docker技术",
                "精通Vue.js"
        };

        for (String testCase : testCases) {
            // When
            List<String> skills = extractor.extractSkills(testCase);

            // Then
            assertFalse(skills.isEmpty(), "应该提取到技能: " + testCase);
            System.out.println("模式1测试 - " + testCase + ": " + skills);
        }
    }

    @Test
    @DisplayName("测试正则模式2：具备XXX能力/经验")
    void testPattern2Extraction() {
        // Given
        String[] testCases = {
                "具备Java开发能力",
                "具备Python数据分析经验",
                "具备MySQL优化技能"
        };

        for (String testCase : testCases) {
            // When
            List<String> skills = extractor.extractSkills(testCase);

            // Then
            assertFalse(skills.isEmpty(), "应该提取到技能: " + testCase);
            System.out.println("模式2测试 - " + testCase + ": " + skills);
        }
    }

//    @Test
//    @DisplayName("测试正则模式3：XXX开发/设计/测试")
//    void testPattern3Extraction() {
//        // Given
//        String[] testCases = {
//                "Java开发",
//                "前端设计",
//                "软件测试",
//                "Python工程师"
//        };
//
//        for (String testCase : testCases) {
//            // When
//            List<String> skills = extractor.extractSkills(testCase);
//
//            // Then
//            assertFalse(skills.isEmpty(), "应该提取到技能: " + testCase);
//            System.out.println("模式3测试 - " + testCase + ": " + skills);
//        }
//    }

    // ==================== 边界条件测试 ====================

    @Test
    @DisplayName("测试技能去重")
    void testSkillDeduplication() {
        // Given
        String demandText = "需要Java，需要Java，需要Java，需要Python，需要Python";

        // When
        List<String> skills = extractor.extractSkills(demandText);

        // Then
        long javaCount = skills.stream().filter(s -> s.equals("java")).count();
        long pythonCount = skills.stream().filter(s -> s.equals("python")).count();

        assertAll(
                () -> assertEquals(1, javaCount, "Java应该只出现一次"),
                () -> assertEquals(1, pythonCount, "Python应该只出现一次"),
                () -> assertEquals(2, skills.size(), "应该只有2个不同的技能")
        );
    }

    @Test
    @DisplayName("测试技能排序")
    void testSkillSorting() {
        // Given
        String demandText = "需要Python, Java, C++, MySQL, Vue, React, Docker";

        // When
        List<String> skills = extractor.extractSkills(demandText);

        // Then
        List<String> sortedSkills = new ArrayList<>(skills);
        Collections.sort(sortedSkills);
        assertEquals(sortedSkills, skills, "技能应该按字母顺序排序");
    }

    @Test
    @DisplayName("测试长文本提取")
    void testLongTextExtraction() {
        // Given
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            longText.append("需要Java开发、Python数据分析、MySQL数据库、Redis缓存、");
            longText.append("Spring Boot框架、Vue前端技术、Docker容器化。");
        }

        // When
        List<String> skills = extractor.extractSkills(longText.toString());

        // Then
        System.out.println("长文本提取结果数量: " + skills.size());
        assertAll(
                () -> assertFalse(skills.isEmpty()),
                () -> assertTrue(skills.size() <= 20, "技能数量应该合理")
        );
    }

    @Test
    @DisplayName("测试标点符号处理")
    void testPunctuationHandling() {
        // Given
        String demandText = "需要Java、Python；C++。还有MySQL！以及Redis？";

        // When
        List<String> skills = extractor.extractSkills(demandText);

        // Then
        assertAll(
                () -> assertTrue(skills.contains("java")),
                () -> assertTrue(skills.contains("python")),
                () -> assertTrue(skills.contains("c++")),
                () -> assertTrue(skills.contains("mysql")),
                () -> assertTrue(skills.contains("redis"))
        );

        // 验证不包含标点符号
        for (String skill : skills) {
            assertFalse(skill.contains("、"), "不应该包含顿号: " + skill);
            assertFalse(skill.contains("；"), "不应该包含分号: " + skill);
            assertFalse(skill.contains("。"), "不应该包含句号: " + skill);
            assertFalse(skill.contains("！"), "不应该包含感叹号: " + skill);
            assertFalse(skill.contains("？"), "不应该包含问号: " + skill);
        }
    }

    // ==================== calculateMatchScore 测试 ====================

    @Test
    @DisplayName("测试完全匹配")
    void testPerfectMatchScore() {
        // Given
        List<String> userSkills = Arrays.asList("java", "spring", "mysql", "vue");
        List<String> teamRequirements = Arrays.asList("java", "spring", "mysql", "vue");

        // When
        double score = extractor.calculateMatchScore(userSkills, teamRequirements);

        // Then
        assertEquals(1.0, score, 0.001, "完全匹配应该得1分");
    }

    @Test
    @DisplayName("测试部分匹配")
    void testPartialMatchScore() {
        // Given
        List<String> userSkills = Arrays.asList("java", "spring", "python");
        List<String> teamRequirements = Arrays.asList("java", "spring", "mysql", "vue");

        // When
        double score = extractor.calculateMatchScore(userSkills, teamRequirements);

        // Then
        assertEquals(0.5, score, 0.001, "匹配2/4个技能应该得0.5分");
    }

    @Test
    @DisplayName("测试无匹配")
    void testNoMatchScore() {
        // Given
        List<String> userSkills = Arrays.asList("python", "django");
        List<String> teamRequirements = Arrays.asList("java", "spring", "mysql");

        // When
        double score = extractor.calculateMatchScore(userSkills, teamRequirements);

        // Then
        assertEquals(0.0, score, 0.001, "没有匹配应该得0分");
    }

    @Test
    @DisplayName("测试技能重复时的匹配度计算")
    void testMatchScoreWithDuplicates() {
        // Given
        List<String> userSkills = Arrays.asList("java", "java", "java", "spring");
        List<String> teamRequirements = Arrays.asList("java", "java", "mysql");

        // When
        double score = extractor.calculateMatchScore(userSkills, teamRequirements);

        // Then - 应该去重，用户技能集合为[java, spring]，需求集合为[java, mysql]
        assertEquals(0.5, score, 0.001, "匹配1/2个技能应该得0.5分");
    }

    @Test
    @DisplayName("测试匹配度边界值")
    void testMatchScoreBoundaries() {
        // Given
        List<String> userSkills = Arrays.asList("java", "spring");

        // When & Then
        assertAll(
                () -> assertEquals(0.0, extractor.calculateMatchScore(userSkills, null), "null需求应该得0分"),
                () -> assertEquals(0.0, extractor.calculateMatchScore(userSkills, new ArrayList<>()), "空需求应该得0分"),
                () -> assertEquals(0.0, extractor.calculateMatchScore(null, userSkills), "null用户技能应该得0分"),
                () -> assertEquals(0.0, extractor.calculateMatchScore(new ArrayList<>(), userSkills), "空用户技能应该得0分")
        );
    }

    // ==================== 集成测试 ====================

    @Test
    @DisplayName("集成测试：从需求提取到匹配度计算")
    void testCompleteWorkflow() {
        // Given
        String demandText = "招聘全栈开发工程师：\n" +
                "1. 需要熟练掌握Java和Spring Boot\n" +
                "2. 要求熟悉Vue.js或React\n" +
                "3. 具备MySQL和Redis使用经验\n" +
                "4. 了解Docker容器化技术\n" +
                "5. 有微服务开发经验者优先";

        List<String> userSkills = Arrays.asList("java", "springboot", "vue", "mysql", "docker", "微服务");

        // When
        List<String> extractedSkills = extractor.extractSkills(demandText);
        double matchScore = extractor.calculateMatchScore(userSkills, extractedSkills);

        // Then
        System.out.println("=== 集成测试结果 ===");
        System.out.println("需求文本: " + demandText);
        System.out.println("提取的技能: " + extractedSkills);
        System.out.println("用户技能: " + userSkills);
        System.out.println("匹配度: " + matchScore);

        assertAll(
                () -> assertTrue(extractedSkills.size() >= 5, "应该提取到多个技能"),
                () -> assertTrue(matchScore > 0 && matchScore <= 1, "匹配度应该在0-1之间"),
                () -> assertTrue(extractedSkills.contains("java") || extractedSkills.contains("springboot"),
                        "应该包含Java或Spring Boot"),
                () -> assertTrue(extractedSkills.contains("vue") || extractedSkills.contains("react"),
                        "应该包含Vue或React"),
                () -> assertTrue(extractedSkills.contains("mysql") || extractedSkills.contains("redis"),
                        "应该包含MySQL或Redis")
        );
    }

    @Test
    @DisplayName("测试实际招聘需求")
    void testRealWorldJobRequirements() {
        // Given - 实际招聘需求示例
        String[] jobRequirements = {
                "Java开发工程师要求：3年以上Java开发经验，精通Spring Boot、Spring Cloud微服务框架，熟悉MySQL、Redis，了解Docker、Kubernetes，有高并发系统开发经验者优先。",
                "前端开发工程师要求：精通Vue.js或React框架，熟悉HTML5、CSS3、JavaScript，了解Webpack、Vite等构建工具，有移动端开发经验者优先。",
                "Python算法工程师要求：熟练掌握Python，熟悉机器学习、深度学习算法，有TensorFlow或PyTorch项目经验，具备数据分析、数据挖掘能力。",
                "运维工程师要求：熟悉Linux操作系统，掌握Docker、Kubernetes容器技术，了解Jenkins、Git等CI/CD工具，有云平台（AWS/Azure/GCP）使用经验。"
        };

        for (int i = 0; i < jobRequirements.length; i++) {
            String requirement = jobRequirements[i];

            // When
            List<String> skills = extractor.extractSkills(requirement);

            // Then
            System.out.println("\n=== 实际需求测试 " + (i+1) + " ===");
            System.out.println("需求: " + requirement.substring(0, Math.min(50, requirement.length())) + "...");
            System.out.println("提取技能: " + skills);

            assertAll(
                    () -> assertTrue(skills.size() >= 4, "应该提取到多个技能"),
                    () -> assertFalse(skills.isEmpty(), "不应该返回空列表")
            );
        }
    }

    // ==================== 参数化测试 ====================

    @ParameterizedTest
    @ValueSource(strings = {
            "需要Java开发",
            "要求Python编程",
            "掌握Spring框架",
            "熟悉MySQL",
            "了解Docker",
            "精通Vue.js"
    })
    @DisplayName("参数化测试需求动词")
    void testDemandVerbsParameterized(String demandText) {
        // When
        List<String> skills = extractor.extractSkills(demandText);

        // Then
        assertFalse(skills.isEmpty(), "应该提取到技能: " + demandText);
    }

    @ParameterizedTest
    @CsvSource({
            "'需要Java和Python', 'java,python'",
            "'需要Vue和React', 'vue,react'",
            "'需要MySQL和Redis', 'mysql,redis'",
            "'需要Docker和Kubernetes', 'docker,kubernetes'"
    })
    @DisplayName("参数化测试技能组合")
    void testSkillCombinationsParameterized(String demandText, String expectedSkillsStr) {
        // Given
        Set<String> expectedSkills = new HashSet<>(Arrays.asList(expectedSkillsStr.split(",")));

        // When
        List<String> actualSkills = extractor.extractSkills(demandText);

        // Then
        Set<String> actualSkillSet = new HashSet<>(actualSkills);
        assertTrue(actualSkillSet.containsAll(expectedSkills),
                String.format("应该包含技能: %s，实际技能: %s", expectedSkills, actualSkills));
    }

    // ==================== 特殊场景测试 ====================

    @Test
    @DisplayName("测试大小写不敏感")
    void testCaseInsensitivity() {
        // Given
        String[] testCases = {
                "需要JAVA开发",
                "需要Java开发",
                "需要java开发",
                "需要JavA开发"
        };

        for (String testCase : testCases) {
            // When
            List<String> skills = extractor.extractSkills(testCase);

            // Then
            assertTrue(skills.contains("java"), "应该识别为java: " + testCase);
        }
    }

    @Test
    @DisplayName("测试特殊格式技能")
    void testSpecialFormatSkills() {
        // Given
        String demandText = "需要Spring Boot、React Native、Vue.js、Node.js";

        // When
        List<String> skills = extractor.extractSkills(demandText);

        // Then
        assertAll(
                () -> assertTrue(skills.contains("springboot") || skills.contains("spring"), "应该识别Spring Boot"),
                () -> assertTrue(skills.contains("reactnative") || skills.contains("react"), "应该识别React Native"),
                () -> assertTrue(skills.contains("vue"), "应该识别Vue.js")
        );
    }

    @Test
    @DisplayName("测试技能清理逻辑")
    void testSkillCleaning() {
        // Given - 包含后缀的技能描述
        String[] testCases = {
                "Java开发",
                "Spring框架",
                "Python语言",
                "MySQL数据库",
                "Docker技术",
                "Vue.js框架"
        };

        for (String testCase : testCases) {
            String demandText = "需要" + testCase;

            // When
            List<String> skills = extractor.extractSkills(demandText);

            // Then - 验证技能被正确提取，不包含后缀
            assertFalse(skills.isEmpty(), "应该提取到技能: " + testCase);

            // 验证不包含常见的后缀词
            for (String skill : skills) {
                assertFalse(skill.contains("开发"), "技能不应该包含'开发': " + skill);
                assertFalse(skill.contains("框架"), "技能不应该包含'框架': " + skill);
                assertFalse(skill.contains("语言"), "技能不应该包含'语言': " + skill);
                assertFalse(skill.contains("数据库"), "技能不应该包含'数据库': " + skill);
                assertFalse(skill.contains("技术"), "技能不应该包含'技术': " + skill);
            }
        }
    }

    @Test
    @DisplayName("测试复杂需求文本")
    void testComplexRequirementText() {
        // Given - 复杂的实际需求
        String demandText = "岗位职责：\n" +
                "1. 负责公司核心业务系统的设计与开发，确保系统高性能、高可用；\n" +
                "2. 参与技术方案设计，完成核心模块编码；\n" +
                "3. 持续优化系统性能，解决技术难题。\n" +
                "任职要求：\n" +
                "1. 计算机相关专业本科及以上学历，3年以上Java开发经验；\n" +
                "2. 精通Java编程，熟悉JVM原理、多线程、集合等；\n" +
                "3. 熟练使用Spring Boot、Spring Cloud等微服务框架；\n" +
                "4. 熟悉MySQL、Redis等数据库，有SQL优化经验；\n" +
                "5. 了解分布式系统原理，熟悉Dubbo、ZooKeeper等；\n" +
                "6. 有Docker、Kubernetes使用经验者优先；\n" +
                "7. 良好的沟通能力和团队协作精神。";

        // When
        List<String> skills = extractor.extractSkills(demandText);

        // Then
        System.out.println("复杂需求提取结果: " + skills);
        assertAll(
                () -> assertTrue(skills.size() >= 8, "应该提取到多个技能"),
                () -> assertTrue(skills.contains("java")),
                () -> assertTrue(skills.contains("springboot") || skills.contains("spring")),
                () -> assertTrue(skills.contains("springcloud") || skills.contains("spring")),
                () -> assertTrue(skills.contains("mysql")),
                () -> assertTrue(skills.contains("redis")),
                () -> assertTrue(skills.contains("docker") || skills.contains("kubernetes"))
        );
    }
}
