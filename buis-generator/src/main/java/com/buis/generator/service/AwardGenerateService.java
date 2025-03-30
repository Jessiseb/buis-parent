package com.buis.generator.service;

import cn.hutool.core.date.DateUtil;
import com.buis.common.core.domain.R;
import com.buis.domain.Award;
import com.buis.generator.util.AwardUtil;
import com.buis.service.*;
import com.buis.vo.UserInfoVo;

import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Service
public class AwardGenerateService {
    /**
     * 生成 Word 文档内容
     */
    @Resource
    private IAwardService awardService;
    @Resource
    private ITeamMemberStudentService teamMemberStudentService;
    @Resource
    private IRaceInfoService raceInfoService;

    public R<String> generateAwardContent(String name,Long raceId, HttpServletResponse response) {

        AwardUtil awardUtil = new AwardUtil();

//        //通过teamId查出队伍全部学生的名字
//        List<String> names = new ArrayList<>();
//        List<UserInfoVo> student = teamMemberStudentService.getMemberStudentByTeamId(teamId);
//        for (UserInfoVo user : student) {
//            names.add(user.getName());
//        }
//        String studentNames = String.join("、", names);
//
        Award awards = awardService.selectAwardByNameAndRace(name, raceId);

        //比赛名字
        String race = raceInfoService.selectRaceInfoById(raceId).getName();

        //转换成汉字
        Map<String, String> levelMapping = new HashMap<>();
        levelMapping.put("1", "一等");
        levelMapping.put("2", "二等");
        levelMapping.put("3", "三等");
        levelMapping.put("4", "优秀");

        String level = levelMapping.getOrDefault(awards.getAwardLevel(), " ");

        // 2. 准备数据（将实体类转换为 Velocity 需要的 Map 格式）
        Map<String, String> templateParam = new HashMap<>();
        templateParam.put("name", awards.getName());
        templateParam.put("year", awards.getYear());
        templateParam.put("race", race);
        templateParam.put("level", level);
        if (awards.getTeachers() != null) {
            templateParam.put("teacher", awards.getTeachers()); // 允许空值
        } else {
            templateParam.put("teacher", " ");
        }
        templateParam.put("date", DateUtil.format(awards.getAwardDate(), "yyyy年MM月dd日")); // 格式化日期

        return awardUtil.generate(response, "/templates", "award.xml", templateParam, "奖状");
    }

//    public R<String> generateAwardContents(List<Long> teamIds, HttpServletResponse response){
//        AwardUtil awardUtil = new AwardUtil();
//        List<Map<String, String>> allParams = new ArrayList<>();
//        for (Long teamId : teamIds) {
//
//            Award awards = awardService.selectAwardByTeam(teamId);
//
//            //通过teamId查出队伍全部学生的名字
//            List<String> names = new ArrayList<>();
//            List<UserInfoVo> student = teamMemberStudentService.getMemberStudentByTeamId(teamId);
//            for (UserInfoVo user : student) {
//                names.add(user.getName());
//            }
//            String studentNames = String.join("、", names);
//
//            //比赛名字
//            String race = raceInfoService.selectRaceInfoById(awards.getRaceId()).getName();
//
//            //转换成汉字
//            Map<String, String> levelMapping = new HashMap<>();
//            levelMapping.put("1", "一");
//            levelMapping.put("2", "二");
//            levelMapping.put("3", "三");
//
//            String level = levelMapping.getOrDefault(awards.getAwardLevel(), " ");
//
//            // 2. 准备数据（将实体类转换为 Velocity 需要的 Map 格式）
//            Map<String, String> templateParam = new HashMap<>();
//            templateParam.put("name", studentNames);
//            templateParam.put("year", awards.getYear());
//            templateParam.put("race", race);
//            templateParam.put("level", level);
//            if (awards.getTeachers() != null) {
//                templateParam.put("teacher", awards.getTeachers()); // 允许空值
//            } else {
//                templateParam.put("teacher", " ");
//            }
//            templateParam.put("date", DateUtil.format(awards.getAwardDate(), "yyyy年MM月dd日")); // 格式化日期
//
//            // 将数据添加到列表
//            allParams.add(templateParam);
//        }
//        awardUtil.batchGenerate(response, "/templates", "award.xml", allParams, "奖状");
//        return R.ok("生成成功");
//    }
}




