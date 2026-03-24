package com.buis.web.controller.api;
import com.buis.common.utils.SecurityUtils;
import com.buis.model.dto.MatchFilterDto;
import com.buis.model.dto.MatchRequestDto;
import com.buis.model.vo.MatchResultVo;
import com.buis.model.vo.TeamRecommendationVo;
import com.buis.model.vo.UserRecommendationVo;
import com.buis.service.ISkillMatchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 技能匹配控制器
 */
@RestController
@RequestMapping("/api/match")
@Api(tags = "技能匹配接口")
public class SkillMatchController {
    @Autowired
    private ISkillMatchService skillMatchService;

    /**
     * 为当前登录用户推荐队伍
     *
     * @param filter 过滤条件
     * @return 队伍推荐列表
     */
    @ApiOperation("为当前登录用户推荐队伍")
    @GetMapping("/teams")
    public List<TeamRecommendationVo> recommendTeamsForUser(
            @ApiParam("过滤条件") MatchFilterDto filter) {
        Long userId = SecurityUtils.getFrontLoginUserId();
        if (filter == null) {
            filter = new MatchFilterDto();
        }
        return skillMatchService.recommendTeamsForUser(userId, filter);
    }

    /**
     * 为队伍推荐用户
     *
     * @param teamId 队伍ID
     * @param filter 过滤条件
     * @return 用户推荐列表
     */
    @ApiOperation("为队伍推荐用户")
    @GetMapping("/users/{teamId}")
    public List<UserRecommendationVo> recommendUsersForTeam(
            @ApiParam("队伍ID") @PathVariable Long teamId,
            @ApiParam("过滤条件") MatchFilterDto filter) {
        if (filter == null) {
            filter = new MatchFilterDto();
        }
        return skillMatchService.recommendUsersForTeam(teamId, filter);
    }

    /**
     * 计算用户与队伍的匹配度
     *
     * @param request 匹配请求
     * @return 匹配结果详情
     */
    @ApiOperation("计算匹配得分")
    @PostMapping("/score")
    public MatchResultVo calculateMatchScore(@ApiParam("匹配请求") @RequestBody MatchRequestDto request) {
        return skillMatchService.calculateMatchScore(request.getUserId(), request.getTeamId());
    }

    /**
     * 批量计算用户与队伍的匹配度
     *
     * @param userId  用户ID
     * @param teamIds 队伍ID列表
     * @return 匹配结果列表
     */
    @ApiOperation("批量计算匹配得分")
    @PostMapping("/batch/score")
    public List<MatchResultVo> batchCalculateMatchScores(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("队伍ID列表") @RequestParam List<Long> teamIds) {
        return skillMatchService.batchCalculateMatchScores(userId, teamIds);
    }

    /**
     * 带过滤条件的匹配
     *
     * @param request 匹配请求
     * @param filter  过滤条件
     * @return 匹配结果详情
     */
    @ApiOperation("带过滤条件的匹配")
    @PostMapping("/filter")
    public MatchResultVo filterMatch(
            @ApiParam("匹配请求") @RequestBody MatchRequestDto request,
            @ApiParam("过滤条件") MatchFilterDto filter) {
        return skillMatchService.calculateMatchScore(request.getUserId(), request.getTeamId());
    }
}
