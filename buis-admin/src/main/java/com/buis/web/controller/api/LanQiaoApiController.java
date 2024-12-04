package com.buis.web.controller.api;

import cn.dev33.satoken.stp.StpUtil;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.R;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.SecurityUtils;
import com.buis.domain.RaceLanqiaoStudent;
import com.buis.domain.RaceLanqiaoTeacher;
import com.buis.domain.RaceLanqiaoType;
import com.buis.domain.UserInfo;
import com.buis.service.IRaceLanqiaoStudentService;
import com.buis.service.IRaceLanqiaoTeacherService;
import com.buis.service.IRaceLanqiaoTypeService;
import com.buis.service.IUserInfoService;
import com.buis.service.impl.UserInfoServiceImpl;
import com.buis.vo.RaceLanqiaoTeacherVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.jsqlparser.statement.select.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "蓝桥杯Api")
@RestController
@RequestMapping("/api/lanqiao")
public class LanQiaoApiController extends BaseController {
    @Autowired
    private IRaceLanqiaoStudentService raceLanqiaoStudentService;
    @Autowired
    private IRaceLanqiaoTypeService raceLanqiaoTypeService;
    @Autowired
    private IRaceLanqiaoTeacherService raceLanqiaoTeacherService;
    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 根据比赛id查询老师列表
     */
    @ApiOperation("根据比赛id查询指导老师列表")
    @GetMapping("/getTeacherList/{race}")
    public R<List<RaceLanqiaoTeacherVo>> getTeacherList(@ApiParam(name = "race", value = "比赛id", required = true) @PathVariable Long race) {
        RaceLanqiaoTeacher raceLanqiaoTeacher = new RaceLanqiaoTeacher();
        raceLanqiaoTeacher.setRaceId(race);
        List<RaceLanqiaoTeacherVo> raceLanqiaoTeacherVos = raceLanqiaoTeacherService.selectRaceLanqiaoTeacherList(raceLanqiaoTeacher);
        if (raceLanqiaoTeacherVos != null && raceLanqiaoTeacherVos.size() > 0) {
            for (RaceLanqiaoTeacherVo raceLanqiaoTeacherVo : raceLanqiaoTeacherVos) {
                if (raceLanqiaoTeacherVo != null) {
                    raceLanqiaoTeacherVo.setBankNumber(null);//银行卡号
                    raceLanqiaoTeacherVo.setIdentity(null);//身份证
                }
            }
        }
        return R.ok(raceLanqiaoTeacherVos);
    }

    /**
     * 删除蓝桥杯报名信息
     */
    @ApiOperation("删除蓝桥杯报名信息")
    @DeleteMapping("/{race}/{isTeacher}")
    public R remove(@ApiParam(name = "race", value = "比赛id", required = true) @PathVariable Long race, @ApiParam(name = "isTeacher", value = "标识（0-学生，1-老师）", required = true) @PathVariable Integer isTeacher) {
        Long userId = SecurityUtils.getFrontLoginUserId();
        if (isTeacher == 0) {
            raceLanqiaoStudentService.deleteRaceLanqiaoStudentByRaceIdAndUserId(race, userId);
        } else if (isTeacher == 1) {
            raceLanqiaoTeacherService.deleteRaceLanqiaoTeacherByRaceIdAndUserId(race, userId);
        }
        return R.ok();
    }

    /**
     * 获取学生蓝桥杯报名信息
     */
    @ApiOperation("获取学生蓝桥杯报名信息")
    @GetMapping("/student/{race}")
    public R<RaceLanqiaoStudent> getStudent(@ApiParam(name = "race", value = "比赛id", required = true) @PathVariable Long race) {
        Long userId = SecurityUtils.getFrontLoginUserId();
        UserInfo id = userInfoService.selectUserInfoById(userId);
        if (id != null && id.getIsTeacher() != 0 ) {
            throw new ServiceException("您不是学生，无法报名");
        }
        RaceLanqiaoStudent raceLanqiaoStudent = raceLanqiaoStudentService.selectRaceLanqiaoStudentRaceIdAndByUserId(race, userId);
        return R.ok(raceLanqiaoStudent);
    }

    /**
     * 获取老师蓝桥杯报名信息
     */
    @ApiOperation("获取老师蓝桥杯报名信息")
    @GetMapping("/teacher/{race}")
    public R<RaceLanqiaoTeacher> getTeacher(@ApiParam(name = "race", value = "比赛id", required = true) @PathVariable Long race) {
        Long userId = SecurityUtils.getFrontLoginUserId();
        UserInfo id = userInfoService.selectUserInfoById(userId);
        if (id != null && id.getIsTeacher() != 1 ) {
            throw new ServiceException("您不是老师，无法报名");
        }
        RaceLanqiaoTeacher raceLanqiaoTeacher = raceLanqiaoTeacherService.selectRaceLanqiaoTeacherRaceIdAndByUserId(race, userId);
        return R.ok(raceLanqiaoTeacher);
    }

    /**
     * 新增蓝桥杯学生报名信息
     */
    @ApiOperation("新增蓝桥杯学生报名信息")
    @PostMapping("/student/{race}")
    public R add(@ApiParam(name = "race", value = "比赛id", required = true) @PathVariable Long race, @ApiParam(name = "raceLanqiaoStudent", value = "学生报名信息表", required = true) @RequestBody RaceLanqiaoStudent raceLanqiaoStudent) {
        Long userId = SecurityUtils.getFrontLoginUserId();
        raceLanqiaoStudent.setUserId(userId);
        raceLanqiaoStudent.setRaceId(race);
        int res = raceLanqiaoStudentService.insertRaceLanqiaoStudent(raceLanqiaoStudent);
        return R.ok(res);
    }

    /**
     * 新增蓝桥杯老师报名信息
     */
    @ApiOperation("新增蓝桥杯老师报名信息")
    @PostMapping("/teacher/{race}")
    public R add(@ApiParam(name = "race", value = "比赛id", required = true) @PathVariable Long race,
                 @ApiParam(name = "raceLanqiaoTeacher", value = "老师报名信息表", required = true)
                 @RequestBody RaceLanqiaoTeacher raceLanqiaoTeacher) {
        Long userId = SecurityUtils.getFrontLoginUserId();

        raceLanqiaoTeacher.setUserId(userId);
        raceLanqiaoTeacher.setRaceId(race);
        int res = raceLanqiaoTeacherService.insertRaceLanqiaoTeacher(raceLanqiaoTeacher);
        return R.ok(res);
    }

    /**
     * 修改蓝桥杯学生报名信息
     */
    @ApiOperation("修改蓝桥杯学生报名信息")
    @PutMapping("/student")
    public R edit(@ApiParam(name = "raceLanqiaoStudent", value = "学生报名信息表", required = true) @RequestBody RaceLanqiaoStudent raceLanqiaoStudent) {
        Long userId = SecurityUtils.getFrontLoginUserId();
        if (userId != null && userId.equals(raceLanqiaoStudent.getUserId())) {
            return R.ok(raceLanqiaoStudentService.updateRaceLanqiaoStudent(raceLanqiaoStudent));
        } else {
            throw new ServiceException("非本人修改");
        }
    }

    /**
     * 修改蓝桥杯老师报名信息
     */
    @ApiOperation("修改蓝桥杯老师报名信息")
    @PutMapping("/teacher")
    public R edit(@ApiParam(name = "raceLanqiaoTeacher", value = "老师报名信息表", required = true) @RequestBody RaceLanqiaoTeacher raceLanqiaoTeacher) {
        Long userId = SecurityUtils.getFrontLoginUserId();
        if (userId != null && userId.equals(raceLanqiaoTeacher.getUserId())) {
            return R.ok(raceLanqiaoTeacherService.updateRaceLanqiaoTeacher(raceLanqiaoTeacher));
        } else {
            throw new ServiceException("非本人修改");
        }
    }

    /**
     * 查询蓝桥杯赛道类型列表
     */
    @ApiOperation("查询蓝桥杯赛道类型列表")
    @PostMapping("/typeList")
    public R<List<RaceLanqiaoType>> typeList(@ApiParam(name = "raceLanqiaoType", value = "赛道类型条件过滤", required = false) @RequestBody RaceLanqiaoType raceLanqiaoType) {
        List<RaceLanqiaoType> list = raceLanqiaoTypeService.selectRaceLanqiaoTypeList(raceLanqiaoType);
        return R.ok(list);
    }

}
