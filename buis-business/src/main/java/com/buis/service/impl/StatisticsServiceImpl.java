package com.buis.service.impl;

import com.buis.mapper.PreSignStudentMapper;
import com.buis.mapper.StatisticsMapper;
import com.buis.model.bo.MemberInfoBO;
import com.buis.model.domain.PreSignStudent;
import com.buis.model.vo.ClassAwardCountVo;
import com.buis.model.vo.PreSignStudentVo;
import com.buis.model.vo.StudentCompetitionStatus;
import com.buis.model.vo.TeamInfoVo;
import com.buis.service.IStatisticsService;
import com.buis.service.ITeamMemberTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsServiceImpl implements IStatisticsService {
    @Autowired
    private ITeamMemberTeacherService teamMemberTeacherService;
    @Autowired
    private PreSignStudentMapper preSignStudentMapper;
    @Autowired
    private StatisticsMapper statisticsMapper;

    /**
     * 根据老师ID获取所带学生竞赛状态
     * @param teacherId
     * @return
     */
    @Override
    public List<StudentCompetitionStatus> getStudentCompetitionStatus(Long teacherId) {
        // 根据老师id获取所带团队
        List<TeamInfoVo> teamList = teamMemberTeacherService.selectJoinTeamByTeacherId(teacherId, null, null);
        // 遍历团队获取老师所带的学生id列表（需去重优化）
        List<Long> stuIdList = new ArrayList<>();
        for (TeamInfoVo teamInfoVo : teamList) {
            List<MemberInfoBO> stuList = teamInfoVo.getMemberInfoBOList();
            for (MemberInfoBO stu : stuList) {
                stuIdList.add(stu.getId());
            }
        }
        // 根据学生id列表获取所带学生报名的比赛
        List<StudentCompetitionStatus> studentCompetitionStatusesList = new ArrayList<>();
        for (Long stuId : stuIdList) {
            PreSignStudent preSignStudent = new PreSignStudent();
            preSignStudent.setStudent(stuId);
            List<PreSignStudentVo> preSignStudentVos = preSignStudentMapper.selectPreSignStudentList(preSignStudent);
            StudentCompetitionStatus aStu = new StudentCompetitionStatus();
            aStu.setStuName(preSignStudentVos.get(0).getStudentInfo().getName());
            ArrayList<String> raceNameList = new ArrayList<>();
            for (PreSignStudentVo preSignStudentVo : preSignStudentVos) {
                raceNameList.add(preSignStudentVo.getRaceName());
            }
            aStu.setRaceName(raceNameList);
            studentCompetitionStatusesList.add(aStu);
        }

        return studentCompetitionStatusesList;
    }

    /**
     * 获取老师负责学生的班级获奖情况
     * @param teacherId 老师ID
     * @param major 专业
     * @param sClass 班级
     * @param projectType 项目类型
     * @param year 学年
     * @return 班级获奖数量列表
     */
    @Override
    public List<ClassAwardCountVo> getClassAwardCount(Long teacherId, Integer major, String sClass, Integer projectType, String year) {
        return statisticsMapper.selectClassAwardCount(teacherId, major, sClass, projectType, year);
    }
}
