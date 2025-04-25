package com.buis.web.controller.api;

import cn.dev33.satoken.stp.StpUtil;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.R;
import com.buis.common.core.page.MyPageVo;
import com.buis.common.utils.SecurityUtils;
import com.buis.domain.PreSignStudent;
import com.buis.domain.PreSignTeacher;
import com.buis.domain.UserInfo;
import com.buis.service.IPreSignStudentService;
import com.buis.service.IPreSignTeacherService;
import com.buis.service.IUserInfoService;
import com.buis.vo.PreSignStudentVo;
import com.buis.vo.PreSignTeacherVo;
import com.buis.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 报名信息
 */
@Api(tags = "报名信息Api")
@RestController
@RequestMapping("/api/join")
public class JoinApiController extends BaseController {
    @Autowired
    private IPreSignTeacherService preTeacherService;
    @Autowired
    private IPreSignStudentService preStudentService;
    @Autowired
    private IUserInfoService userInfoService;


    /**
     * 根据比赛id分页查询预报名老师列表
     *
     * @param RaceId
     * @return
     */
    @ApiOperation("根据比赛id分页查询预报名老师列表")
    @PostMapping("/preTeacher/{RaceId}")
    public MyPageVo<List<PreSignTeacherVo>> getPreTeachersByRaceId(@ApiParam(name = "RaceId", value = "比赛id", required = true) @PathVariable("RaceId") Long RaceId) {
        //开始分页
        startPage();
        PreSignTeacher prevTeacher = new PreSignTeacher();
        prevTeacher.setRace(RaceId);
        List<PreSignTeacherVo> preSignTeacherVos = preTeacherService.selectPreSignTeacherList(prevTeacher);
        //返回结果
        return new MyPageVo(preSignTeacherVos);
    }

    /**
     * 新增预报名
     */
    @ApiOperation("新增预报名")
    @PostMapping("/addPre/{isTeacher}/{race}")
    public R addPre(@ApiParam(name = "isTeacher", value = "标识（0-学生，1-老师）", required = true)
                    @PathVariable Integer isTeacher, @ApiParam(name = "race", value = "比赛id", required = true) @PathVariable Long race) {
        Long userId = SecurityUtils.getFrontLoginUserId();
        if (isTeacher == 1) {
            PreSignTeacher preSignTeacher = new PreSignTeacher();
            preSignTeacher.setTeacher(userId);
            preSignTeacher.setRace(race);
            preTeacherService.insertPreSignTeacher(preSignTeacher);
        } else if (isTeacher == 0) {
            UserInfoVo user = userInfoService.selectUserInfoById(userId);
            PreSignStudent preSignStudent = new PreSignStudent();
            preSignStudent.setStudent(userId);
            preSignStudent.setRace(race);
            preSignStudent.setNumber(user.getNumber());
            preSignStudent.setSClass(user.getStuGrade() + user.getMajorName() + user.getStuClass());
            preSignStudent.setPhone(user.getPhone());
            preStudentService.insertPreSignStudent(preSignStudent);
        }
        return R.ok();

    }

    /**
     * 删除预报名
     */
    @ApiOperation("删除老师预报名")
    @DeleteMapping("/removePre/{isTeacher}/{race}")
    public R removePre(@ApiParam(name = "isTeacher", value = "标识（0-学生，1-老师）", required = true) @PathVariable Integer isTeacher, @ApiParam(name = "RaceId", value = "比赛id", required = true) @PathVariable Long race) {
        Long userId = SecurityUtils.getFrontLoginUserId();
        if (isTeacher == 1) {
            preTeacherService.deletePreSignTeacherByRaceIdAndUserId(race, userId);
        } else if (isTeacher == 0) {
            preStudentService.deletePreSignStudentByRaceIdAndUserId(race, userId);
        }
        return R.ok();
    }

    /**
     * 修改老师预报名
     */
    @ApiOperation("修改老师预报名")
    @PutMapping("/preTeacher")
    public R editPreTeacher(@RequestBody PreSignTeacher preSignTeacher) {
        return R.ok(preTeacherService.updatePreSignTeacher(preSignTeacher));
    }

    /**
     * 修改学生预报名
     */
    @ApiOperation("修改学生预报名")
    @PutMapping("/preStudent")
    public R editPreStudent(@RequestBody PreSignStudent preSignStudent) {
        return R.ok(preStudentService.updatePreSignStudent(preSignStudent));
    }

    /**
     * 根据比赛id分页查询预报名学生列表
     *
     * @param pageNum
     * @param pageSize
     * @param RaceId
     * @return
     */
    @ApiOperation("根据比赛id分页查询预报名学生列表")
    @PostMapping("/preStudent/{RaceId}")
    public MyPageVo<List<PreSignStudentVo>> getPreStudentsByRaceId(
            @ApiParam(name = "pageNum", value = "查询页数", required = true) @RequestParam Integer pageNum,
            @ApiParam(name = "pageSize", value = "查询数量", required = true) @RequestParam Integer pageSize,
            @ApiParam(name = "RaceId", value = "比赛id", required = true) @PathVariable("RaceId") Long RaceId) {
        //开始分页
        startPage();
        PreSignStudent preSignStudent = new PreSignStudent();
        preSignStudent.setRace(RaceId);
        List<PreSignStudentVo> preSignStudentVos = preStudentService.selectPreSignStudentList(preSignStudent);
        //返回结果
        return new MyPageVo(preSignStudentVos);
    }

    /**
     * 根据比赛id查询预报名学生信息
     *
     * @param raceId
     * @return
     */
    @ApiOperation("根据比赛id查询预报名学生信息")
    @PostMapping("/preStudentByRace")
    public R<PreSignStudent> getPreStudentsByRaceId(
            @ApiParam(name = "RaceId", value = "比赛id", required = true)
            @RequestParam("raceId") Long raceId) {
        String s = StpUtil.getLoginId() + "";
        Long loginId = Long.parseLong(s);
        return R.ok(preStudentService.selectByUserIdAndRace(loginId,raceId));
    }


}
