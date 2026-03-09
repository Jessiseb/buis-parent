package com.buis.service;
import com.buis.model.dto.MatchFilterDto;
import com.buis.model.vo.MatchResultVo;
import com.buis.model.vo.TeamRecommendationVo;
import com.buis.model.vo.UserRecommendationVo;

import java.util.List;

/**
 * 技能匹配服务接口
 */
public interface ISkillMatchService {
    /**
     * 为用户推荐队伍
     *
     * @param userId 用户ID
     * @param filter 过滤条件
     * @return 队伍推荐列表
     */
    List<TeamRecommendationVo> recommendTeamsForUser(Long userId, MatchFilterDto filter);

    /**
     * 为队伍推荐用户
     *
     * @param teamId 队伍ID
     * @param filter 过滤条件
     * @return 用户推荐列表
     */
    List<UserRecommendationVo> recommendUsersForTeam(Long teamId, MatchFilterDto filter);

    /**
     * 计算用户与队伍的匹配度
     *
     * @param userId 用户ID
     * @param teamId 队伍ID
     * @return 匹配结果详情
     */
    MatchResultVo calculateMatchScore(Long userId, Long teamId);

    /**
     * 批量计算用户与队伍的匹配度
     *
     * @param userId 用户ID
     * @param teamIds 队伍ID列表
     * @return 匹配结果列表
     */
    List<MatchResultVo> batchCalculateMatchScores(Long userId, List<Long> teamIds);

    /**
     * 批量计算队伍与用户的匹配度
     *
     * @param teamId 队伍ID
     * @param userIds 用户ID列表
     * @return 匹配结果列表
     */
    List<MatchResultVo> batchCalculateUserMatchScores(Long teamId, List<Long> userIds);
}
