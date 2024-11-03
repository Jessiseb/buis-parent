package com.buis.web.controller.api;

import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.R;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.SecurityUtils;
import com.buis.domain.RaceLanqiaoStudent;
import com.buis.domain.RaceLanqiaoTeacher;
import com.buis.domain.RaceLanqiaoType;
import com.buis.service.IRaceLanqiaoStudentService;
import com.buis.service.IRaceLanqiaoTeacherService;
import com.buis.service.IRaceLanqiaoTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "蓝桥杯Api")
@RequestMapping("/api/lanqiao")


public class LanQiaoApiController extends BaseController {
    @Autowired
    private IRaceLanqiaoStudentService raceLanqiaoStudentService;
    @Autowired
    private IRaceLanqiaoTypeService raceLanqiaoTypeService;
    @Autowired
    private IRaceLanqiaoTeacherService raceLanqiaoTeacherService;

    /**
     * 删除蓝桥杯学生报名信息
     */
    @ApiOperation("删除蓝桥杯报名信息")
    @DeleteMapping("/{flag}")
    public R remove(@ApiParam(name = "flag", value = "标识（0-学生，1-老师）", required = true) @PathVariable Integer flag) {
        Long userId = SecurityUtils.getFrontLoginUserId();
        Integer res = 0;
        if (flag == 0) {
            res = raceLanqiaoStudentService.deleteRaceLanqiaoStudentByUserId(userId);
        } else if (flag == 1) {
            res = raceLanqiaoTeacherService.deleteRaceLanqiaoTeacherByUserId(userId);
        }
        if (res > 0) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    /**
     * 获取蓝桥杯报名信息
     */
    @ApiOperation("获取蓝桥杯报名信息")
    @GetMapping("/{flag}")
    public R getInfo(@ApiParam(name = "flag", value = "标识（0-学生，1-老师）", required = true) @PathVariable Integer flag) {
        Long userId = SecurityUtils.getFrontLoginUserId();
        Object result = null;
        if (flag == 0) {
            result = raceLanqiaoStudentService.selectRaceLanqiaoStudentByUserId(userId);
        } else if (flag == 1) {
            result = raceLanqiaoTeacherService.selectRaceLanqiaoTeacherByUserId(userId);
        }
        return R.ok(result);
    }


    /**
     * 新增蓝桥杯学生报名信息
     */
    @ApiOperation("新增蓝桥杯学生报名信息")
    @PostMapping("/student")
    public R add(@ApiParam(name = "raceLanqiaoStudent", value = "学生报名信息表", required = true) @RequestBody RaceLanqiaoStudent raceLanqiaoStudent) {
        Long userId = SecurityUtils.getFrontLoginUserId();
        raceLanqiaoStudent.setUserId(userId);
        return R.ok(raceLanqiaoStudentService.insertRaceLanqiaoStudent(raceLanqiaoStudent));
    }

    /**
     * 新增蓝桥杯老师报名信息
     */
    @ApiOperation("新增蓝桥杯老师报名信息")
    @PostMapping("/teacher")
    public R add(@ApiParam(name = "raceLanqiaoTeacher", value = "老师报名信息表", required = true) @RequestBody RaceLanqiaoTeacher raceLanqiaoTeacher) {
        Long userId = SecurityUtils.getFrontLoginUserId();
        raceLanqiaoTeacher.setUserId(userId);
        return R.ok(raceLanqiaoTeacherService.insertRaceLanqiaoTeacher(raceLanqiaoTeacher));
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
    public R typeList(@ApiParam(name = "raceLanqiaoType", value = "赛道类型条件过滤", required = false) @RequestBody RaceLanqiaoType raceLanqiaoType) {
        List<RaceLanqiaoType> list = raceLanqiaoTypeService.selectRaceLanqiaoTypeList(raceLanqiaoType);
        return R.ok(list);
    }

}
