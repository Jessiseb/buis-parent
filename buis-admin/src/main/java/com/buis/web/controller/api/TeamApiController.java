package com.buis.web.controller.api;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.R;
import com.buis.common.core.page.MyPageVo;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.SecurityUtils;
import com.buis.domain.*;
import com.buis.domain.InvitationVo;
import com.buis.dto.InvitationQueryDto;
import com.buis.dto.TeamMemberSelectDto;
import com.buis.service.ITeamMemberStudentService;
import com.buis.service.ITeamMemberTeacherService;
import com.buis.service.IUserInfoService;
import com.buis.service.impl.TeamInfoServiceImpl;
import com.buis.vo.TeamInfoVo;
import com.buis.vo.UserInfoVo;
import com.github.pagehelper.PageInfo;
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
    /**
     * 学生与老师互选，建立邀请
     * @param teamMemberSelectDto
     * @return
     */
    @ApiOperation("学生与老师建立互选邀请")
    @PostMapping("/mutualSelection")
    public R mutualSelection(@RequestBody TeamMemberSelectDto teamMemberSelectDto){
        //获取当前登录用户
        UserInfo loginUser = userInfoService.getLoginUser();
        Long loginUserId = loginUser.getId();
        Long race = teamMemberSelectDto.getRace();
        Long team = teamMemberSelectDto.getTeam();
        Long inviteeId = teamMemberSelectDto.getInviteeId();
        System.out.println(inviteeId);
        //判断被邀请人在数据库中是否存在
        UserInfoVo userInfoVo = userInfoService.selectUserInfoById(inviteeId);
        if (userInfoVo.getId()==null || inviteeId == null){
            return R.fail("被邀请人不存在或输入为空");
        }
        //判断比赛信息是否合法
        TeamInfoVo teamInfoVo = teamInfoService.selectTeamInfoById(team);
        if (teamInfoVo.getId()==null || !teamInfoVo.getRace().equals(race)){
            return R.fail("比赛、队伍信息不存在或输入为空");
        }
        if (loginUser.getIsTeacher() == 1 && loginUser != null){
            //老师选学生
            TeamMemberStudent teamMemberStudent = new TeamMemberStudent();
            //将dto的比赛、队伍id和选择原因拷贝到TeamMemberTeacher
            BeanUtil.copyProperties(teamMemberSelectDto, teamMemberStudent);
            teamMemberStudent.setDecider(loginUserId);
            //设置互选状态，默认为0，待选
            teamMemberStudent.setIsChoosed(0L);
            //写入被邀请人的id
            teamMemberStudent.setStudent(teamMemberSelectDto.getInviteeId());
            int result = teamMemberStudentService.insertTeamMemberStudent(teamMemberStudent);
            if (result==1){
                //将数据存入redis
//                stringRedisTemplate.opsForValue().set(KEY_PREFIX+loginUserId, teamMemberStudent.toString());
                return R.ok("新增互选成功");
            }
            return R.fail("新增互选失败");
        }else {
            //学生选老师
            TeamMemberTeacher teamMemberTeacher = new TeamMemberTeacher();
            //将dto的比赛、队伍id和选择原因拷贝到TeamMemberTeacher
            BeanUtil.copyProperties(teamMemberSelectDto, teamMemberTeacher);
            teamMemberTeacher.setDecider(loginUserId);
            //设置互选状态，默认为0，待选
            teamMemberTeacher.setIsChoosed(0L);
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
        if (loginUser.getIsTeacher() == 1){
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

    /**
     * 学生与老师互选，接受或拒绝邀请
     * @param id
     * @param isChoosed 互选状态
     * @return
     */
    @ApiOperation("学生与老师互选，接受或拒绝邀请")
    @PutMapping("/updateSelection/{id}")
    public R updateSelection(@ApiParam(name = "id",value = "要修改的数据id")@PathVariable("id") Long id, @ApiParam(name = "isChoosed",value = "要修改的状态,0待选,1接受,2拒绝")@RequestParam("isChoosed") Long isChoosed) {
        // 获取当前登录用户
        UserInfo loginUser = userInfoService.getLoginUser();
        Long loginUserId = loginUser.getId();
        if (loginUser.getIsTeacher() == 1) {
            // 用户为老师，处理学生选择老师的邀请
            TeamMemberTeacher teamMemberTeacher = teamMemberTeacherService.selectTeamMemberTeacherById(id);
            if (teamMemberTeacher != null && teamMemberTeacher.getTeacher().equals(loginUserId)) {
                // 更新状态
                teamMemberTeacher.setIsChoosed(isChoosed);
                teamMemberTeacherService.updateTeamMemberTeacher(teamMemberTeacher);
                return R.ok("更新邀请状态成功");
            } else {
                throw new ServiceException("无权限处理此邀请");
            }
        } else {
            // 用户为学生，处理老师选择学生的邀请
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

    @ApiOperation("带条件分页查询学生与老师互选邀请")
    @PostMapping("/invitationList")
    public MyPageVo<List<InvitationVo>> invitationList(@RequestBody InvitationQueryDto query) {
        // 获取当前登录用户
        UserInfo loginUser = userInfoService.getLoginUser();
        List<InvitationVo> list = new ArrayList<>();
        startPage();
        if (loginUser.getIsTeacher()==1){
            //用户为老师，查询所有老师对学生的邀请
            list = teamMemberStudentService.selectInvitationList(query);
        }else {
            //用户为学生，查询所有学生对老师的邀请
            list = teamMemberTeacherService.selectInvitationList(query);
        }
        return new MyPageVo<>(list, (int) new PageInfo(list).getTotal());
    }



    @ApiOperation("根据队伍id查询队伍成员列表")
    @GetMapping("/Member/{team}")
    public R<HashMap<String, List<UserInfoVo>>> getMemberStudentByTeamId(
                              @ApiParam(name = "team", value = "队伍id", required = true)@PathVariable("team") Long team) {
        List<UserInfoVo> student = teamMemberStudentService.getMemberStudentByTeamId(team);
        List<UserInfoVo> teacher = teamMemberTeacherService.getTeachersByRaceId(team);
        HashMap<String, List<UserInfoVo>> map = new HashMap<>();
        map.put("student", student);
        map.put("teacher", teacher);
        return R.ok(map);
    }


    /**
     * 查询队伍信息列表
     */
    @ApiOperation("带条件分页查询队伍列表")
    @PostMapping("/teamList")
    public MyPageVo<List<TeamInfoVo>> list(@RequestBody TeamInfo teamInfo) {
        startPage();
        List<TeamInfoVo> list = teamInfoService.selectTeamInfoList(teamInfo);
        return new MyPageVo(list, (int) new PageInfo(list).getTotal());
    }


    /**
     * 根据老师id查询老师指导的队伍列表
     *
     * @return 返回结果
     */
    @ApiOperation("根据老师id查询老师指导的队伍列表")
    @GetMapping("/guide")
    public R<List<TeamInfoVo>> guide() {
        Long userId = SecurityUtils.getFrontLoginUserId();
        List<TeamInfoVo> teamInfoVos = teamMemberTeacherService.selectJoinTeamByTeacherId(userId);
        return R.ok(teamInfoVos);
    }

}
