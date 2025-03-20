package com.buis.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.buis.common.core.domain.R;
import com.buis.common.utils.DateUtils;
import com.buis.domain.Judge;
import com.buis.domain.UserInfo;
import com.buis.mapper.UserInfoMapper;
import com.buis.service.*;
import com.buis.vo.ScoreVo;
import com.buis.vo.TeamInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.ScoreMapper;
import com.buis.domain.Score;

/**
 * 评分管理Service业务层处理
 *
 * @author buis
 * @date 2024-10-11
 */
@Service
@Slf4j
public class ScoreServiceImpl implements IScoreService {
    @Autowired
    private ScoreMapper scoreMapper;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IJudgeService judgeService;
    @Autowired
    private ITeamInfoService teamInfoService;
    @Autowired
    private IRaceInfoService raceInfoService;

    /**
     * 查询评分管理
     *
     * @param id 评分管理主键
     * @return 评分管理
     */
    @Override
    public Score selectScoreById(Long id) {
        return scoreMapper.selectScoreById(id);
    }

    /**
     * 新增评分（带业务逻辑）
     *
     * @param score
     * @return
     */
    @Override
    public R addScore(Score score) {
        // 判断是否是评委
        UserInfo loginUser = userInfoService.getLoginUser();
        if (loginUser != null) {
            Long id = loginUser.getId();
            Judge judge = judgeService.selectJudgeByTeacherId(id);

            if (judge != null) {
                //验证评委是否为该比赛的评委
                if (judge.getRace() != score.getRace()) {
                    return R.fail("您不是该比赛的评委");
                }
                //验证队伍是否存在
                Long team = score.getTeam();
                TeamInfoVo teamInfoVo = teamInfoService.selectTeamInfoById(team);
                String raceName = raceInfoService.selectRaceInfoById(score.getRace()).getName();
                if (teamInfoVo == null || !teamInfoVo.getRaceName().equals(raceName)) {
                    return R.fail("队伍不存在/队伍未参加该比赛");
                }

                //验证评委在当前比赛是否已经为该队伍评分
                Score score1 = new Score();
                score1.setTeacher(id);
                List<ScoreVo> scoreVos = scoreMapper.selectScoreList(score1);
                for (ScoreVo scoreVo : scoreVos) {
                    if (scoreVo.getRace() == score.getRace() && scoreVo.getTeam() == team) {
                        return R.fail("您已经为该队伍评分");
                    }
                }
                //是否在评分时间段内
                //TODO

                //计算总分
                BigDecimal totalScore = calculateTotalScore(score.getBuild());
                score.setScore(totalScore);
                //日志打印
                log.info("评委:{},比赛:{},队伍:{},总分:{}", id, score.getRace(), team, totalScore);

                return R.ok(insertScore(score));
            }
            return R.fail("您不是评委，无法进行评分");
        }
        return R.fail("请先登录");
    }

    private BigDecimal calculateTotalScore(String build) {
        String[] parts = build.split(",");
        BigDecimal totalScore = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;

        Pattern pattern = Pattern.compile("(.+?):(\\d+\\.?\\d*)%\\*(\\d+\\.?\\d*)");
        MathContext mc = new MathContext(6, RoundingMode.HALF_UP); // 6位精度

        for (String part : parts) {
            Matcher matcher = pattern.matcher(part.trim());
            if (!matcher.matches()) {
                throw new IllegalArgumentException("格式错误: " + part);
            }

            try {
                String name = matcher.group(1);

                // 解析权重（带%的数值）
                BigDecimal weight = new BigDecimal(matcher.group(2))
                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP); // 保留4位小数

                // 解析得分
                BigDecimal score = new BigDecimal(matcher.group(3));

                // 计算加权得分
                BigDecimal weightedScore = weight.multiply(score, mc);
                totalScore = totalScore.add(weightedScore, mc);
                totalWeight = totalWeight.add(weight, mc);

            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("数值解析失败: " + part, e);
            }
        }

        // 验证权重总和（允许0.0001的误差）
        BigDecimal expectedTotalWeight = new BigDecimal("1.0000");
        if (totalWeight.setScale(4, RoundingMode.HALF_UP).compareTo(expectedTotalWeight) != 0) {
            throw new IllegalArgumentException("权重总和非100%，当前总和: "
                    + totalWeight.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP) + "%");
        }

        // 最终结果保留两位小数
        return totalScore.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 查询评分管理列表
     *
     * @param score 评分管理
     * @return 评分管理
     */
    @Override
    public List<ScoreVo> selectScoreList(Score score) {
        return scoreMapper.selectScoreList(score);
    }

    /**
     * 新增评分管理
     *
     * @param score 评分管理
     * @return 结果
     */
    @Override
    public int insertScore(Score score) {
        score.setCreateTime(DateUtils.getNowDate());
        return scoreMapper.insertScore(score);
    }

    /**
     * 修改评分管理
     *
     * @param score 评分管理
     * @return 结果
     */
    @Override
    public int updateScore(Score score) {
        //获取当前登录用户
        UserInfo loginUser = userInfoService.getLoginUser();
        if (loginUser.getId() != score.getTeacher()) {
            return 0;
        }
        //重新计算总分
        BigDecimal totalScore = calculateTotalScore(score.getBuild());
        score.setScore(totalScore);
        score.setUpdateTime(DateUtils.getNowDate());
        return scoreMapper.updateScore(score);
    }

    /**
     * 批量删除评分管理
     *
     * @param ids 需要删除的评分管理主键
     * @return 结果
     */
    @Override
    public int deleteScoreByIds(Long[] ids) {
        //获取当前登录用户
        UserInfo loginUser = userInfoService.getLoginUser();

        for (Long id : ids) {
            //获取评分信息
            Score score = scoreMapper.selectScoreById(id);
            if (loginUser.getId()!= score.getTeacher()) {
                return 0;
            }
        }
        return scoreMapper.deleteScoreByIds(ids);
    }

    /**
     * 删除评分管理信息
     *
     * @param id 评分管理主键
     * @return 结果
     */
    @Override
    public int deleteScoreById(Long id) {
        //获取当前登录用户
        UserInfo loginUser = userInfoService.getLoginUser();
        //获取评分信息
        Score score = scoreMapper.selectScoreById(id);

        if (loginUser.getId()!= score.getTeacher()) {
            return 0;
        }
        return scoreMapper.deleteScoreById(id);
    }

    /**
     * 查询队伍排名
     * @return
     */

    @Override
    public List<Score> selectTeamsRanking() {
        return scoreMapper.selectTeamsRanking();
    }

}
