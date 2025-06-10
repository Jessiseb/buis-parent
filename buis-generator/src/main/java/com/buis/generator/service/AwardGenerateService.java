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

    public R<String> generateAwardContent(String name, Long raceId, HttpServletResponse response) {

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
        if (awards == null) {
            return R.fail("在" + race + "中未找到" + name + "对应的奖项");
        }

        //转换成汉字
        Map<String, String> levelMapping = new HashMap<>();
        levelMapping.put("1", "一等奖");
        levelMapping.put("2", "二等奖");
        levelMapping.put("3", "三等奖");
        levelMapping.put("4", "优秀奖");

        String level = levelMapping.getOrDefault(awards.getAwardLevel(), " ");

        // 2. 准备数据（将实体类转换为 Velocity 需要的 Map 格式）
        Map<String, String> templateParam = new HashMap<>();
        templateParam.put("name", awards.getName());
        templateParam.put("year", awards.getYear());
        templateParam.put("race", race);
        templateParam.put("level", level);
        if (awards.getTeachers() != null) {
            templateParam.put("project", "作品《"+awards.getAwardProject()+"》");
        } else {
            templateParam.put("project", "");
        }
        if (awards.getTeachers() != null) {
            templateParam.put("teacher", "指导老师："+awards.getTeachers()); // 允许空值
        } else {
            templateParam.put("teacher", " ");
        }
        templateParam.put("date", DateUtil.format(awards.getAwardDate(), "yyyy 年 MM 月 dd 日")); // 格式化日期

        return awardUtil.generate(response, "/templates", "award.xml", templateParam, "奖状");
    }

    public R<String> generateAwardContents(List<String> names, List<Long> raceIds, HttpServletResponse response) throws IOException {
        AwardUtil awardUtil = new AwardUtil();
        List<Map<String, String>> templateParams = new ArrayList<>();

        if (names.size() != raceIds.size()) {
            throw new RuntimeException("传入的姓名和比赛不一致");
        }
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            Long raceId = raceIds.get(i);

            Award awards = awardService.selectAwardByNameAndRace(name, raceId);
            //比赛名字
            String race = raceInfoService.selectRaceInfoById(raceId).getName();
            if (awards == null) {
                return R.fail("在" + race + "中未找到" + name + "对应的奖项");
            }

//            //通过teamId查出队伍全部学生的名字
//            List<String> names = new ArrayList<>();
//            List<UserInfoVo> student = teamMemberStudentService.getMemberStudentByTeamId(teamId);
//            for (UserInfoVo user : student) {
//                names.add(user.getName());
//            }
//            String studentNames = String.join("、", names);

            //转换成汉字
            Map<String, String> levelMapping = new HashMap<>();
            levelMapping.put("1", "一等奖");
            levelMapping.put("2", "二等奖");
            levelMapping.put("3", "三等奖");
            levelMapping.put("4", "优秀奖");

            String level = levelMapping.getOrDefault(awards.getAwardLevel(), " ");

            // 2. 准备数据（将实体类转换为 Velocity 需要的 Map 格式）
            Map<String, String> templateParam = new HashMap<>();
            templateParam.put("name", awards.getName());
            templateParam.put("year", awards.getYear());
            templateParam.put("race", race);
            templateParam.put("level", level);
            if (awards.getTeachers() != null) {
                templateParam.put("project", "作品《"+awards.getAwardProject()+"》");
            } else {
                templateParam.put("project", "");
            }
            if (awards.getTeachers() != null) {
                templateParam.put("teacher", "指导老师："+awards.getTeachers()); // 允许空值
            } else {
                templateParam.put("teacher", " ");
            }
            templateParam.put("date", DateUtil.format(awards.getAwardDate(), "yyyy 年 MM 月 dd日"));

            // 将数据添加到列表
            templateParams.add(templateParam);
        }
        return awardUtil.batchGenerate(response, "/templates", "award.xml", templateParams, "奖状");
    }
}




