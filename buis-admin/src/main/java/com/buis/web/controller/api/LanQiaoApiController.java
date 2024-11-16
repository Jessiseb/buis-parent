package com.buis.web.controller.api;

import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.R;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.SecurityUtils;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.domain.RaceLanqiaoStudent;
import com.buis.domain.RaceLanqiaoTeacher;
import com.buis.domain.RaceLanqiaoType;
import com.buis.service.IRaceDetailService;
import com.buis.service.IRaceLanqiaoStudentService;
import com.buis.service.IRaceLanqiaoTeacherService;
import com.buis.service.IRaceLanqiaoTypeService;
import com.buis.vo.RaceLanqiaoStudentVo;
import com.buis.vo.RaceLanqiaoTeacherVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
    private IRaceDetailService raceDetailService;

    /**
     * 删除蓝桥杯报名信息
     */
    @ApiOperation("删除蓝桥杯报名信息")
    @DeleteMapping("/{race}/{flag}")
    public R remove(@ApiParam(name = "race", value = "比赛id", required = true) @PathVariable Long race, @ApiParam(name = "flag", value = "标识（0-学生，1-老师）", required = true) @PathVariable Integer flag) {
        Long userId = SecurityUtils.getFrontLoginUserId();
        Integer res = 0;
        if (flag == 0) {
            res = raceLanqiaoStudentService.deleteRaceLanqiaoStudentByUserIdAndRaceId(race, userId);
        } else if (flag == 1) {
            res = raceLanqiaoTeacherService.deleteRaceLanqiaoTeacherByUserIdAndRaceId(race, userId);
        }
        if (res > 0) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    /**
     * 获取学生蓝桥杯报名信息
     */
    @ApiOperation("获取学生蓝桥杯报名信息")
    @GetMapping("/student/{race}")
    public R<RaceLanqiaoStudent> getStudent(@ApiParam(name = "race", value = "比赛id", required = true) @PathVariable Long race) {
        Long userId = SecurityUtils.getFrontLoginUserId();
        RaceLanqiaoStudent raceLanqiaoStudent = raceLanqiaoStudentService.selectRaceLanqiaoStudentByUserIdAndRaceId(race, userId);
        return R.ok(raceLanqiaoStudent);
    }

    /**
     * 获取老师蓝桥杯报名信息
     */
    @ApiOperation("获取老师蓝桥杯报名信息")
    @GetMapping("/teacher/{race}")
    public R<RaceLanqiaoTeacher> getTeacher(@ApiParam(name = "race", value = "比赛id", required = true) @PathVariable Long race) {
        Long userId = SecurityUtils.getFrontLoginUserId();
        RaceLanqiaoTeacher raceLanqiaoTeacher = raceLanqiaoTeacherService.selectRaceLanqiaoTeacherByUserIdAndRaceId(race, userId);
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
        return R.ok(raceLanqiaoStudentService.insertRaceLanqiaoStudent(raceLanqiaoStudent));
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

    /**
     * 导出蓝桥杯指导老师信息
     */
    @ApiOperation("导出蓝桥杯指导老师信息")
    @PostMapping("/export")
    public void exportTeacher(@ApiParam(name = "raceLanqiaoTeacher", value = "蓝桥杯指导老师条件过滤", required = false) @RequestBody RaceLanqiaoTeacher raceLanqiaoTeacher,
                              HttpServletResponse response) {
        List<RaceLanqiaoTeacherVo> list = raceLanqiaoTeacherService.selectRaceLanqiaoTeacherList(raceLanqiaoTeacher);
        ExcelUtil<RaceLanqiaoTeacherVo> util = new ExcelUtil<>(RaceLanqiaoTeacherVo.class);
        util.exportExcel(response, list, "蓝桥杯指导老师数据");
    }

    /**
     * 导出蓝桥杯报名学生信息
     */
    @ApiOperation("导出蓝桥杯报名学生信息")
    @PostMapping("/exportStudent")
    public void exportStudent(@ApiParam(name = "raceLanqiaoStudent", value = "蓝桥杯报名学生条件过滤", required = false) @RequestBody RaceLanqiaoStudent raceLanqiaoStudent,
                              HttpServletResponse response) {
        List<RaceLanqiaoStudentVo> list = raceLanqiaoStudentService.selectRaceLanqiaoStudentList(raceLanqiaoStudent);
        ExcelUtil<RaceLanqiaoStudentVo> util = new ExcelUtil<>(RaceLanqiaoStudentVo.class);
        util.exportExcel(response, list, "蓝桥杯报名学生数据");
    }


}
