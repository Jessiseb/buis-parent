package com.buis.service.impl;
import com.buis.mapper.TeamInfoMapper;
import com.buis.mapper.TeamSkillMapper;
import com.buis.mapper.UserInfoMapper;
import com.buis.mapper.UserSkillMapper;
import com.buis.model.domain.TeamInfo;
import com.buis.model.domain.UserInfo;
import com.buis.model.dto.MatchFilterDto;
import com.buis.model.vo.MatchResultVo;
import com.buis.model.vo.TeamInfoVo;
import com.buis.model.vo.TeamRecommendationVo;
import com.buis.model.vo.TeamSkillVo;
import com.buis.model.vo.UserInfoVo;
import com.buis.model.vo.UserRecommendationVo;
import com.buis.model.vo.UserSkillVo;
import com.buis.service.ISkillMatchService;
import com.buis.service.RedisCacheService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 技能匹配服务实现
 */
@Service
public class SkillMatchServiceImpl implements ISkillMatchService {
    @Autowired
    private UserSkillMapper userSkillMapper;
    @Autowired
    private TeamSkillMapper teamSkillMapper;
    @Autowired
    private TeamInfoMapper teamInfoMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private RedisCacheService redisCacheService;

    // 技能重要性权重
    private static final Map<Integer, Double> IMPORTANCE_WEIGHTS = new HashMap<>();
    static {
        IMPORTANCE_WEIGHTS.put(1, 1.3); // 必须
        IMPORTANCE_WEIGHTS.put(2, 0.9); // 重要
        IMPORTANCE_WEIGHTS.put(3, 0.5); // 加分
    }

    // 技能相关性映射
    private static final Map<Long, List<Long>> SKILL_RELATIONS = new HashMap<>();
    static {
        // 设计技能相关性
        SKILL_RELATIONS.put(21L, Arrays.asList(24L, 25L, 119L, 26L, 27L)); // UI设计 -> 原型设计, Figma, 高保真原型, Photoshop, Illustrator
        SKILL_RELATIONS.put(24L, Arrays.asList(21L, 25L, 119L, 28L)); // 原型设计 -> UI设计, Figma, 高保真原型, Sketch
        SKILL_RELATIONS.put(25L, Arrays.asList(21L, 24L, 119L, 28L)); // Figma -> UI设计, 原型设计, 高保真原型, Sketch
        SKILL_RELATIONS.put(28L, Arrays.asList(21L, 24L, 25L, 119L)); // Sketch -> UI设计, 原型设计, Figma, 高保真原型
        SKILL_RELATIONS.put(26L, Arrays.asList(21L, 27L, 29L)); // Photoshop -> UI设计, Illustrator, 动效设计
        SKILL_RELATIONS.put(27L, Arrays.asList(21L, 26L, 29L)); // Illustrator -> UI设计, Photoshop, 动效设计
        
        // 开发技能相关性
        SKILL_RELATIONS.put(7L, Arrays.asList(8L, 9L, 10L)); // Spring Boot -> Spring Cloud, MyBatis, Maven
        SKILL_RELATIONS.put(6L, Arrays.asList(4L, 13L, 14L)); // React -> JavaScript, HTML, CSS
        SKILL_RELATIONS.put(15L, Arrays.asList(4L, 13L, 14L)); // Vue -> JavaScript, HTML, CSS
        SKILL_RELATIONS.put(18L, Arrays.asList(4L, 6L, 15L)); // TypeScript -> JavaScript, React, Vue
        
        // 数据技能相关性
        SKILL_RELATIONS.put(2L, Arrays.asList(41L, 42L, 43L)); // Python -> Pandas, NumPy, Scikit-learn
        SKILL_RELATIONS.put(44L, Arrays.asList(45L, 46L, 47L)); // SQL -> MySQL, PostgreSQL, MongoDB
        
        // 业务技能相关性
        SKILL_RELATIONS.put(35L, Arrays.asList(51L, 52L, 53L)); // 项目管理 -> 团队协作, 沟通能力, 问题解决
        SKILL_RELATIONS.put(32L, Arrays.asList(55L, 56L, 57L)); // 市场分析 -> 营销策划, 商业计划书, 定价策略
    }

    @Override
    public List<TeamRecommendationVo> recommendTeamsForUser(Long userId, MatchFilterDto filter) {
        // 从缓存获取用户技能
        List<UserSkillVo> userSkills = (List<UserSkillVo>) redisCacheService.getUserSkills(userId);
        if (userSkills == null) {
            userSkills = userSkillMapper.selectUserSkillsByUserId(userId);
            redisCacheService.cacheUserSkills(userId, userSkills);
        }

        // 获取用户信息
        UserInfo userInfo = (UserInfo) redisCacheService.getUserInfo(userId);
        if (userInfo == null) {
            userInfo = userInfoMapper.selectUserInfoById(userId);
            redisCacheService.cacheUserInfo(userId, userInfo);
        }

        // 查询所有招募中的队伍
        TeamInfo teamInfoQuery = new TeamInfo();
        teamInfoQuery.setIsPublish("Y"); // 只查询已发布招募的队伍
        List<TeamInfo> teams = teamInfoMapper.selectTeamInfoList(teamInfoQuery);

        // 计算匹配度并排序
        List<TeamRecommendationVo> recommendations = new ArrayList<>();
        for (TeamInfo team : teams) {
            // 从缓存获取队伍技能需求
            List<TeamSkillVo> teamSkills = (List<TeamSkillVo>) redisCacheService.getTeamSkills(team.getId());
            if (teamSkills == null) {
                teamSkills = teamSkillMapper.selectTeamSkillsByTeamId(team.getId());
                redisCacheService.cacheTeamSkills(team.getId(), teamSkills);
            }

            // 计算匹配度
            MatchResultVo matchResult = calculateMatchScore(userId, team.getId());
            if (matchResult.getTotalMatchScore() >= filter.getMinMatchScore()) {
                TeamRecommendationVo vo = new TeamRecommendationVo();
                TeamInfoVo teamInfoVo = new TeamInfoVo();
                BeanUtils.copyProperties(team, teamInfoVo);
                BeanUtils.copyProperties(teamInfoVo, vo);
                vo.setMatchScore(matchResult.getTotalMatchScore());
                vo.setSkillMatchScore(matchResult.getSkillMatchScore());
                vo.setMajorMatchBonus(matchResult.getMajorMatchBonus());
                vo.setMatchedSkills(matchResult.getMatchedSkills().stream()
                        .map(MatchResultVo.MatchedSkillVo::getSkillName)
                        .collect(Collectors.toList()));
                vo.setRequiredSkills(matchResult.getMatchedSkills().stream()
                        .map(MatchResultVo.MatchedSkillVo::getSkillName)
                        .collect(Collectors.toList()));
                vo.setIsMajorMatch(matchResult.getMajorMatchBonus() > 0);
                recommendations.add(vo);
            }
        }

        // 按匹配度排序
        recommendations.sort((a, b) -> Double.compare(b.getMatchScore(), a.getMatchScore()));

        // 限制结果数量
        if (recommendations.size() > filter.getLimit()) {
            recommendations = recommendations.subList(0, filter.getLimit());
        }

        return recommendations;
    }

    @Override
    public List<UserRecommendationVo> recommendUsersForTeam(Long teamId, MatchFilterDto filter) {
        // 从缓存获取队伍技能需求
        List<TeamSkillVo> teamSkills = (List<TeamSkillVo>) redisCacheService.getTeamSkills(teamId);
        if (teamSkills == null) {
            teamSkills = teamSkillMapper.selectTeamSkillsByTeamId(teamId);
            redisCacheService.cacheTeamSkills(teamId, teamSkills);
        }

        // 获取队伍信息
        TeamInfo teamInfo = (TeamInfo) redisCacheService.getTeamInfo(teamId);
        if (teamInfo == null) {
            teamInfo = teamInfoMapper.selectTeamInfoById(teamId);
            redisCacheService.cacheTeamInfo(teamId, teamInfo);
        }

        // 获取所有用户（实际应用中应该分页或有其他过滤条件）
        List<UserInfo> users = userInfoMapper.selectUserInfoList(new UserInfo());

        // 计算匹配度并排序
        List<UserRecommendationVo> recommendations = new ArrayList<>();
        for (UserInfo user : users) {
            // 从缓存获取用户技能
            List<UserSkillVo> userSkills = (List<UserSkillVo>) redisCacheService.getUserSkills(user.getId());
            if (userSkills == null) {
                userSkills = userSkillMapper.selectUserSkillsByUserId(user.getId());
                redisCacheService.cacheUserSkills(user.getId(), userSkills);
            }

            // 计算匹配度
            MatchResultVo matchResult = calculateMatchScore(user.getId(), teamId);
            if (matchResult.getTotalMatchScore() >= filter.getMinMatchScore()) {
                UserRecommendationVo vo = new UserRecommendationVo();
                UserInfoVo userInfoVo = new UserInfoVo();
                BeanUtils.copyProperties(user, userInfoVo);
                BeanUtils.copyProperties(userInfoVo, vo);
                vo.setMatchScore(matchResult.getTotalMatchScore());
                vo.setSkillMatchScore(matchResult.getSkillMatchScore());
                vo.setMajorMatchBonus(matchResult.getMajorMatchBonus());
                vo.setMatchedSkills(matchResult.getMatchedSkills().stream()
                        .map(MatchResultVo.MatchedSkillVo::getSkillName)
                        .collect(Collectors.toList()));
                vo.setUserSkills(userSkills.stream()
                        .map(UserSkillVo::getTagName)
                        .collect(Collectors.toList()));
                vo.setIsMajorMatch(matchResult.getMajorMatchBonus() > 0);
                recommendations.add(vo);
            }
        }

        // 按匹配度排序
        recommendations.sort((a, b) -> Double.compare(b.getMatchScore(), a.getMatchScore()));

        // 限制结果数量
        if (recommendations.size() > filter.getLimit()) {
            recommendations = recommendations.subList(0, filter.getLimit());
        }

        return recommendations;
    }

    @Override
    public MatchResultVo calculateMatchScore(Long userId, Long teamId) {
        // 获取用户技能
        List<UserSkillVo> userSkills = (List<UserSkillVo>) redisCacheService.getUserSkills(userId);
        if (userSkills == null) {
            userSkills = userSkillMapper.selectUserSkillsByUserId(userId);
            redisCacheService.cacheUserSkills(userId, userSkills);
        }

        // 获取队伍技能需求
        List<TeamSkillVo> teamSkills = (List<TeamSkillVo>) redisCacheService.getTeamSkills(teamId);
        if (teamSkills == null) {
            teamSkills = teamSkillMapper.selectTeamSkillsByTeamId(teamId);
            redisCacheService.cacheTeamSkills(teamId, teamSkills);
        }

        // 获取用户信息
        UserInfo userInfo = (UserInfo) redisCacheService.getUserInfo(userId);
        if (userInfo == null) {
            userInfo = userInfoMapper.selectUserInfoById(userId);
            redisCacheService.cacheUserInfo(userId, userInfo);
        }

        // 获取队伍信息
        TeamInfo teamInfo = (TeamInfo) redisCacheService.getTeamInfo(teamId);
        if (teamInfo == null) {
            teamInfo = teamInfoMapper.selectTeamInfoById(teamId);
            redisCacheService.cacheTeamInfo(teamId, teamInfo);
        }

        // 计算技能匹配度
        double skillMatchScore = calculateSkillMatchScore(userSkills, teamSkills);

        // 计算专业匹配加分
        double majorMatchBonus = calculateMajorMatchBonus(userInfo, teamInfo);

        // 计算总匹配度
        double totalMatchScore = skillMatchScore + majorMatchBonus;
        if (totalMatchScore > 1.0) {
            totalMatchScore = 1.0;
        }

        // 构建匹配结果
        MatchResultVo result = new MatchResultVo();
        result.setUserId(userId);
        result.setUserName(userInfo.getName());
        result.setTeamId(teamId);
        result.setTeamName(teamInfo.getName());
        result.setTotalMatchScore(totalMatchScore);
        result.setSkillMatchScore(skillMatchScore);
        result.setMajorMatchBonus(majorMatchBonus);

        // 计算匹配的技能
        List<MatchResultVo.MatchedSkillVo> matchedSkills = calculateMatchedSkills(userSkills, teamSkills);
        result.setMatchedSkills(matchedSkills);

        // 计算缺失的技能
        List<String> missingSkills = calculateMissingSkills(userSkills, teamSkills);
        result.setMissingSkills(missingSkills);

        // 计算额外的技能
        List<String> extraSkills = calculateExtraSkills(userSkills, teamSkills);
        result.setExtraSkills(extraSkills);

        return result;
    }

    @Override
    public List<MatchResultVo> batchCalculateMatchScores(Long userId, List<Long> teamIds) {
        List<MatchResultVo> results = new ArrayList<>();
        for (Long teamId : teamIds) {
            results.add(calculateMatchScore(userId, teamId));
        }
        return results;
    }

    @Override
    public List<MatchResultVo> batchCalculateUserMatchScores(Long teamId, List<Long> userIds) {
        List<MatchResultVo> results = new ArrayList<>();
        for (Long userId : userIds) {
            results.add(calculateMatchScore(userId, teamId));
        }
        return results;
    }

    /**
     * 计算技能匹配度分数
     */
    private double calculateSkillMatchScore(List<UserSkillVo> userSkills, List<TeamSkillVo> teamSkills) {
        if (teamSkills.isEmpty()) {
            return 0.0;
        }

        double totalScore = 0.0;
        int matchedCount = 0;

        for (TeamSkillVo teamSkill : teamSkills) {
            boolean matched = false;
            for (UserSkillVo userSkill : userSkills) {
                if (teamSkill.getSkillTagId().equals(userSkill.getSkillTagId())) {
                    // 完全匹配
                    double importanceWeight = IMPORTANCE_WEIGHTS.getOrDefault(teamSkill.getImportanceLevel(), 0.0);
                    totalScore += importanceWeight * userSkill.getConfidence().doubleValue();
                    matchedCount++;
                    matched = true;
                    break;
                } else if (SKILL_RELATIONS.containsKey(teamSkill.getSkillTagId()) && 
                           SKILL_RELATIONS.get(teamSkill.getSkillTagId()).contains(userSkill.getSkillTagId())) {
                    // 相关技能匹配
                    double importanceWeight = IMPORTANCE_WEIGHTS.getOrDefault(teamSkill.getImportanceLevel(), 0.0) * 0.7;
                    totalScore += importanceWeight * userSkill.getConfidence().doubleValue();
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                // 没有匹配，给予基础分
                totalScore += 0.1;
            }
        }

        // 计算匹配度：考虑技能质量和匹配数量
        double baseScore = totalScore / teamSkills.size();
        double matchRate = (double) matchedCount / teamSkills.size();
        return baseScore * 0.8 + matchRate * 0.2;
    }

    /**
     * 计算专业匹配加分
     */
    private double calculateMajorMatchBonus(UserInfo userInfo, TeamInfo teamInfo) {
        // 简单实现：如果用户学院与队伍负责人学院相同，给予加分
        if (userInfo.getCollege() == teamInfo.getCollege()) {
            return 0.1; // 10%的加分
        }
        return 0.0;
    }

    /**
     * 计算匹配的技能
     */
    private List<MatchResultVo.MatchedSkillVo> calculateMatchedSkills(List<UserSkillVo> userSkills, List<TeamSkillVo> teamSkills) {
        List<MatchResultVo.MatchedSkillVo> matchedSkills = new ArrayList<>();

        for (TeamSkillVo teamSkill : teamSkills) {
            for (UserSkillVo userSkill : userSkills) {
                if (teamSkill.getSkillTagId().equals(userSkill.getSkillTagId())) {
                    MatchResultVo.MatchedSkillVo matchedSkill = new MatchResultVo.MatchedSkillVo();
                    matchedSkill.setSkillName(teamSkill.getTagName());
                    matchedSkill.setImportanceLevel(teamSkill.getImportanceLevel());
                    matchedSkill.setImportanceLevelName(getImportanceLevelName(teamSkill.getImportanceLevel()));
                    matchedSkill.setConfidence(userSkill.getConfidence().doubleValue());
                    matchedSkill.setSkillScore(IMPORTANCE_WEIGHTS.getOrDefault(teamSkill.getImportanceLevel(), 0.0) * userSkill.getConfidence().doubleValue());
                    matchedSkills.add(matchedSkill);
                    break;
                }
            }
        }

        return matchedSkills;
    }

    /**
     * 计算队伍需要但用户缺少的技能
     */
    private List<String> calculateMissingSkills(List<UserSkillVo> userSkills, List<TeamSkillVo> teamSkills) {
        Set<Long> userSkillTagIds = userSkills.stream()
                .map(UserSkillVo::getSkillTagId)
                .collect(Collectors.toSet());

        return teamSkills.stream()
                .filter(teamSkill -> !userSkillTagIds.contains(teamSkill.getSkillTagId()))
                .map(TeamSkillVo::getTagName)
                .collect(Collectors.toList());
    }

    /**
     * 计算用户拥有但队伍不需要的技能
     */
    private List<String> calculateExtraSkills(List<UserSkillVo> userSkills, List<TeamSkillVo> teamSkills) {
        Set<Long> teamSkillTagIds = teamSkills.stream()
                .map(TeamSkillVo::getSkillTagId)
                .collect(Collectors.toSet());

        return userSkills.stream()
                .filter(userSkill -> !teamSkillTagIds.contains(userSkill.getSkillTagId()))
                .map(UserSkillVo::getTagName)
                .collect(Collectors.toList());
    }

    /**
     * 获取技能重要性等级名称
     */
    private String getImportanceLevelName(Integer importanceLevel) {
        switch (importanceLevel) {
            case 1: return "必须";
            case 2: return "重要";
            case 3: return "加分";
            default: return "未知";
        }
    }
}
