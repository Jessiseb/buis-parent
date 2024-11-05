package com.buis.web.controller.api;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.R;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.SecurityUtils;
import com.buis.domain.TeamInfo;
import com.buis.domain.TeamMemberStudent;
import com.buis.domain.TeamMemberTeacher;
import com.buis.domain.UserInfo;
import com.buis.dto.TeamMemberSelectDto;
import com.buis.service.ITeamMemberStudentService;
import com.buis.service.ITeamMemberTeacherService;
import com.buis.service.IUserInfoService;
import com.buis.service.impl.TeamInfoServiceImpl;
import com.buis.vo.TeamInfoVo;
import com.buis.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 队伍管理
 */
@Api(tags = "队伍管理Api")
@RestController
@RequestMapping("/api/team")
@Slf4j
public class TeamApiController extends BaseController{
    @Autowired
    private ITeamMemberStudentService teamMemberStudentService;
    @Autowired
    private ITeamMemberTeacherService teamMemberTeacherService;
    @Autowired
    private TeamInfoServiceImpl teamInfoService;

    @Autowired
    private IUserInfoService userInfoService;
    @ApiOperation("学生与老师建立互选邀请")
    @PostMapping("/mutualSelection")
    public R mutualSelection(@RequestBody TeamMemberSelectDto teamMemberSelectDto){
        //获取当前登录用户
        UserInfo loginUser = userInfoService.getLoginUser();
        Long loginUserId = loginUser.getId();
        if (loginUser.getTitle() != null && loginUser != null){
            //老师选学生
            TeamMemberStudent teamMemberStudent = new TeamMemberStudent();
            //将dto的比赛、队伍id和选择原因拷贝到TeamMemberTeacher
            BeanUtil.copyProperties(teamMemberSelectDto, teamMemberStudent);
            teamMemberStudent.setDecider(loginUserId);
            //设置互选状态，默认为0，待选
            teamMemberStudent.setIsChoosed(0L);
            // 检查 inviteeId 是否为空
            Long inviteeId = teamMemberSelectDto.getInviteeId();
            if (inviteeId == null) {
                return R.fail("被邀请人ID不能为空");
            }
            //写入被邀请人的id
            teamMemberStudent.setStudent(teamMemberSelectDto.getInviteeId());
            int result = teamMemberStudentService.insertTeamMemberStudent(teamMemberStudent);
            return result==1?R.ok("新增互选成功") : R.fail("新增互选失败");
        }else {
            //学生选老师
            TeamMemberTeacher teamMemberTeacher = new TeamMemberTeacher();
            //将dto的比赛、队伍id和选择原因拷贝到TeamMemberTeacher
            BeanUtil.copyProperties(teamMemberSelectDto, teamMemberTeacher);
            teamMemberTeacher.setDecider(loginUserId);
            //设置互选状态，默认为0，待选
            teamMemberTeacher.setIsChoosed(0L);
            // 检查 inviteeId 是否为空
            Long inviteeId = teamMemberSelectDto.getInviteeId();
            if (inviteeId == null) {
                return R.fail("被邀请人ID不能为空");
            }
            //写入被邀请人的id
            teamMemberTeacher.setTeacher(inviteeId);
            int result = teamMemberTeacherService.insertTeamMemberTeacher(teamMemberTeacher);
            return result==1?R.ok("新增互选成功") : R.fail("新增互选失败");
        }
    }
    @ApiOperation("学生与老师互选，取消邀请")
    @DeleteMapping("/cancelSelection/{id}")
    public R cancelSelection(@PathVariable(value = "id") Long id){
        //获取当前登录用户
        UserInfo loginUser = userInfoService.getLoginUser();
        Long loginUserId = loginUser.getId();
        if (loginUser.getTitle() != null){
            // 老师取消邀请
            TeamMemberStudent teamMemberStudent = teamMemberStudentService.selectTeamMemberStudentById(id);
            if (teamMemberStudent != null && teamMemberStudent.getDecider().equals(loginUserId)) {
                teamMemberStudentService.deleteTeamMemberStudentById(id);
                return R.ok("取消邀请成功");
            } else {
                throw new ServiceException("无权限取消此邀请");
            }
        } else {
            // 学生取消邀请
            TeamMemberTeacher teamMemberTeacher = teamMemberTeacherService.selectTeamMemberTeacherById(id);
            if (teamMemberTeacher != null && teamMemberTeacher.getDecider().equals(loginUserId)) {
                teamMemberTeacherService.deleteTeamMemberTeacherById(id);
                return R.ok("取消邀请成功");
            } else {
                throw new ServiceException("无权限取消此邀请");
            }
        }
    }

    @ApiOperation("学生与老师互选，接受或拒绝邀请")
    @PutMapping("/updateSelection/{id}")
    public R updateSelection(@PathVariable("id") Long id, @RequestParam("isChoosed") Long isChoosed) {
        // 获取当前登录用户
        UserInfo loginUser = userInfoService.getLoginUser();
        Long loginUserId = loginUser.getId();
        System.out.println(isChoosed);
        if (loginUser.getTitle() != null) {
            // 老师处理邀请
            TeamMemberTeacher teamMemberTeacher = teamMemberTeacherService.selectTeamMemberTeacherById(id);
            System.out.println(teamMemberTeacher);
            if (teamMemberTeacher != null && teamMemberTeacher.getTeacher().equals(loginUserId)) {
                // 更新状态
                teamMemberTeacher.setIsChoosed(isChoosed);
                teamMemberTeacherService.updateTeamMemberTeacher(teamMemberTeacher);
                return R.ok("更新邀请状态成功");
            } else {
                throw new ServiceException("无权限处理此邀请");
            }
        } else {
            // 学生处理邀请
            TeamMemberStudent teamMemberStudent = teamMemberStudentService.selectTeamMemberStudentById(id);
            if (teamMemberStudent != null && teamMemberStudent.getStudent().equals(loginUserId)) {
                // 更新状态
                teamMemberStudent.setIsChoosed(isChoosed);
                teamMemberStudentService.updateTeamMemberStudent(teamMemberStudent);
                return R.ok("更新邀请状态成功");
            } else {
                throw new ServiceException("无权限处理此邀请");
            }

        }
    }

   /* @ApiOperation("查询我的邀请列表")
    @GetMapping("/myInvitations")
    public R<List<TeamMemberSelectDto>> getMyInvitations(@RequestParam(value = "type", required = false) String type) {
        // 获取当前登录用户
        UserInfo loginUser = userInfoService.getLoginUser();
        Long loginUserId = loginUser.getId();

        List<TeamMemberSelectDto> invitations = new ArrayList<>();

        if (loginUser.getTitle() != null) {
            // 老师查询邀请
            List<TeamMemberStudent> teamMemberStudents = teamMemberStudentService.list(Wrappers.<TeamMemberStudent>lambdaQuery()
                    .eq(TeamMemberStudent::getDecider, loginUserId)
                    .or()
                    .eq(TeamMemberStudent::getTargetUser, loginUserId));

            for (TeamMemberStudent teamMemberStudent : teamMemberStudents) {
                if (type == null || (type.equals("sent") && teamMemberStudent.getDecider().equals(loginUserId)) ||
                        (type.equals("received") && teamMemberStudent.getTargetUser().equals(loginUserId))) {
                    TeamMemberSelectDto dto = new TeamMemberSelectDto();
                    BeanUtil.copyProperties(teamMemberStudent, dto);
                    invitations.add(dto);
                }
            }
        } else {
            // 学生查询邀请
            List<TeamMemberTeacher> teamMemberTeachers = teamMemberTeacherService.list(Wrappers.<TeamMemberTeacher>lambdaQuery()
                    .eq(TeamMemberTeacher::getDecider, loginUserId)
                    .or()
                    .eq(TeamMemberTeacher::getTargetUser, loginUserId));

            for (TeamMemberTeacher teamMemberTeacher : teamMemberTeachers) {
                if (type == null || (type.equals("sent") && teamMemberTeacher.getDecider().equals(loginUserId)) ||
                        (type.equals("received") && teamMemberTeacher.getTargetUser().equals(loginUserId))) {
                    TeamMemberSelectDto dto = new TeamMemberSelectDto();
                    BeanUtil.copyProperties(teamMemberTeacher, dto);
                    invitations.add(dto);
                }
            }
        }
        return R.ok(invitations);
    }*/


    @ApiOperation("根据队伍id查询队伍成员列表")
    @GetMapping("/Member/{team}")
    public R getMemberStudentByTeamId(
                              @ApiParam(name = "team", value = "队伍id", required = true)@PathVariable("team") Long team) {
        List<UserInfoVo> student = teamMemberStudentService.getMemberStudentByTeamId(team);
        List<UserInfoVo> teacher = teamMemberTeacherService.getTeachersByRaceId(team);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("student", student);
        map.put("teacher", teacher);
        return R.ok(map);
    }


    /**
     * 查询队伍信息列表
     */
    @ApiOperation("带条件分页查询队伍列表")
    @PostMapping("/teamList")
    public TableDataInfo list(@RequestBody TeamInfo teamInfo) {
        startPage();
        List<TeamInfoVo> list = teamInfoService.selectTeamInfoList(teamInfo);
        return getDataTable(list);
    }


    /**
     * 根据老师id查询老师指导的队伍列表
     *
     * @return 返回结果
     */
    @ApiOperation("根据老师id查询老师指导的队伍列表")
    @GetMapping("/guide")
    public R guide() {
        Long userId = SecurityUtils.getFrontLoginUserId();
        List<TeamInfoVo> teamInfoVos = teamMemberTeacherService.selectJoinTeamByTeacherId(userId);
        return R.ok(teamInfoVos);
    }

}
