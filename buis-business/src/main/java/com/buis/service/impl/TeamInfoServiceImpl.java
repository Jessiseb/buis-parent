package com.buis.service.impl;

import java.util.*;

import com.buis.common.utils.DateUtils;
import com.buis.dto.TeamQueryDto;
import com.buis.mapper.TeamMemberTeacherMapper;
import com.buis.bo.MemberInfoBO;
import com.buis.vo.TeamInfoVo;
import com.buis.mapper.TeamMemberStudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buis.mapper.TeamInfoMapper;
import com.buis.domain.TeamInfo;
import com.buis.service.ITeamInfoService;

/**
 * 队伍信息Service业务层处理
 * 
 * @author buis
 * @date 2024-10-11
 */
@Service
public class TeamInfoServiceImpl implements ITeamInfoService
{
    @Autowired
    private TeamInfoMapper teamInfoMapper;

    @Autowired
    private TeamMemberStudentMapper teamMemberStudentMapper;

    @Autowired
    private TeamMemberTeacherMapper teamMemberTeacherMapper;
    /**
     * 查询队伍信息
     * 
     * @param id 队伍信息主键
     * @return 队伍信息
     */
    @Override
    public TeamInfo selectTeamInfoById(Long id)
    {
        return teamInfoMapper.selectTeamInfoById(id);
    }

    /**
     * 查询队伍信息列表
     * 
     * @param teamInfo 队伍信息
     * @return 队伍信息
     */
    @Override
    public List<TeamInfo> selectTeamInfoList(TeamInfo teamInfo)
    {
        return teamInfoMapper.selectTeamInfoList(teamInfo);
    }

    /**
     * 新增队伍信息
     * 
     * @param teamInfo 队伍信息
     * @return 结果
     */
    @Override
    public int insertTeamInfo(TeamInfo teamInfo)
    {
        teamInfo.setCreateTime(DateUtils.getNowDate());
        return teamInfoMapper.insertTeamInfo(teamInfo);
    }

    /**
     * 修改队伍信息
     * 
     * @param teamInfo 队伍信息
     * @return 结果
     */
    @Override
    public int updateTeamInfo(TeamInfo teamInfo)
    {
        teamInfo.setUpdateTime(DateUtils.getNowDate());
        return teamInfoMapper.updateTeamInfo(teamInfo);
    }

    /**
     * 批量删除队伍信息
     * 
     * @param ids 需要删除的队伍信息主键
     * @return 结果
     */
    @Override
    public int deleteTeamInfoByIds(Long[] ids)
    {
        return teamInfoMapper.deleteTeamInfoByIds(ids);
    }

    /**
     * 删除队伍信息信息
     * 
     * @param id 队伍信息主键
     * @return 结果
     */
    @Override
    public int deleteTeamInfoById(Long id)
    {
        return teamInfoMapper.deleteTeamInfoById(id);
    }

    @Override
    public List<TeamInfo> listTeams(TeamQueryDto teamQueryDTO) {
        //TODO 需要在查出来的时候判断我是否加入该队伍吗?
        return teamInfoMapper.listTeams(teamQueryDTO);
    }


    /**
     * 查询队伍信息列表 Vo
     * @param teamInfo
     * @return
     */
    @Override
    public List<TeamInfoVo> selectTeamInfoVoList(TeamInfo teamInfo) {
        List<TeamInfoVo> teamInfoVo=teamInfoMapper.selectTeamInfoVo(teamInfo);
        if (teamInfoVo == null || teamInfoVo.isEmpty()) {
            return Collections.emptyList(); // 返回空列表
        }
        //根据返回的title判断队长是否为老师
        for (TeamInfoVo infoVo : teamInfoVo) {
            // 获取老师队伍人数及队伍已有老师名称
            Map<String, Object> teacherResultMap = teamMemberTeacherMapper.getCountAndTeachers(infoVo.getId());
            // 获取学生队伍人数及已有成员名称
            Map<String, Object> studentResultMap = teamMemberStudentMapper.getCountAndStudents(infoVo.getId());

            Long teamNumbers = 0L;
            List<MemberInfoBO> teamMembers = new ArrayList<>();

            if (teacherResultMap != null) {
                Long teacherNumbers = (Long) teacherResultMap.get("teamNumbers");
                String teacherMembersNameWithIdsStr = (String) teacherResultMap.get("teamMembersNameWithIds");
                teamNumbers += teacherNumbers;
                if (teacherMembersNameWithIdsStr != null) {
                    teamMembers.addAll(parseTeamMembersNameWithIds(teacherMembersNameWithIdsStr));
                }
            }

            if (studentResultMap != null) {
                Long studentNumbers = (Long) studentResultMap.get("teamNumbers");
                String studentMembersNameWithIdsStr = (String) studentResultMap.get("teamMembersNameWithIds");
                teamNumbers += studentNumbers;
                if (studentMembersNameWithIdsStr != null) {
                    teamMembers.addAll(parseTeamMembersNameWithIds(studentMembersNameWithIdsStr));
                }
            }

            infoVo.setTeamNumbers(teamNumbers);
            infoVo.setTeamMemberList(teamMembers);
        }
        return teamInfoVo;
    }

    /**
     * 查询单个队伍信息 vo
     * @param id
     * @return
     */
    @Override
    public TeamInfoVo selectTeamInfoVoById(Long id) {
        TeamInfoVo teamInfoVo = teamInfoMapper.selectTeamInfoVoById(id);
        // 获取老师队伍人数及队伍已有老师名称
        Map<String, Object> teacherResultMap = teamMemberTeacherMapper.getCountAndTeachers(teamInfoVo.getId());
        // 获取学生队伍人数及已有成员名称
        Map<String, Object> studentResultMap = teamMemberStudentMapper.getCountAndStudents(teamInfoVo.getId());

        Long teamNumbers = 0L;
        List<MemberInfoBO> teamMembers = new ArrayList<>();

        if (teacherResultMap != null) {
            Long teacherNumbers = (Long) teacherResultMap.get("teamNumbers");
            String teacherMembersNameWithIdsStr = (String) teacherResultMap.get("teamMembersNameWithIds");
            teamNumbers += teacherNumbers;
            if (teacherMembersNameWithIdsStr != null) {
                // parseTeamMembersNameWithIds方法解析并添加到列表
                teamMembers.addAll(parseTeamMembersNameWithIds(teacherMembersNameWithIdsStr));
            }
        }

        if (studentResultMap != null) {
            Long studentNumbers = (Long) studentResultMap.get("teamNumbers");
            String studentMembersNameWithIdsStr = (String) studentResultMap.get("teamMembersNameWithIds");
            teamNumbers += studentNumbers;
            if (studentMembersNameWithIdsStr != null) {
                teamMembers.addAll(parseTeamMembersNameWithIds(studentMembersNameWithIdsStr));
            }
        }

        teamInfoVo.setTeamNumbers(teamNumbers);
        teamInfoVo.setTeamMemberList(teamMembers);
        return teamInfoVo;
    }



    private List<MemberInfoBO> parseTeamMembersNameWithIds(String teamMembersNameWithIdsStr) {
        List<MemberInfoBO> teamMembers = new ArrayList<>();
        // 将输入字符串按逗号分割，得到每个成员的ID和名字对
        String[] pairs = teamMembersNameWithIdsStr.split(",");
        // 遍历每个成员的ID和名字对
        for (String pair : pairs) {
            // 将每个成员的ID和名字对按冒号分割，得到ID和名字
            String[] parts = pair.split(":");
            // 检查分割后的数组长度是否为2，确保每个成员的ID和名字都存在
            if (parts.length == 2) {
                MemberInfoBO member = new MemberInfoBO();
                member.setId(Long.parseLong(parts[0]));
                member.setName(parts[1]);
                teamMembers.add(member);
            }
        }
        return teamMembers;
    }

}
