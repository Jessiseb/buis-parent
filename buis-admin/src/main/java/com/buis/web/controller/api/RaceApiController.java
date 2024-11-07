package com.buis.web.controller.api;

import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.domain.R;
import com.buis.common.core.page.TableDataInfo;
import com.buis.domain.PreSignStudent;
import com.buis.domain.PreSignTeacher;
import com.buis.domain.RaceInfo;
import com.buis.domain.RaceType;
import com.buis.service.*;
import com.buis.vo.PreSignStudentVo;
import com.buis.vo.PreSignTeacherVo;
import com.buis.vo.RaceInfoVo;
import com.buis.vo.UserInfoVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 比赛信息
 */
@Api(tags = "比赛信息Api")
@RestController
@RequestMapping("/api/race")
public class RaceApiController extends BaseController {
    @Autowired
    private IRaceInfoService raceInfoService;
    @Autowired
    private IRaceTypeService raceTypeService;
    @Autowired
    private IPreSignTeacherService preTeacherService;
    @Autowired
    private IPreSignStudentService preStudentService;
    @ApiOperation("带条件分页查询比赛列表")
    @GetMapping("/raceList")
    public TableDataInfo list(@ApiParam(name = "raceInfo", value = "比赛信息", required = false) RaceInfo raceInfo) {
        startPage();
        List<RaceInfoVo> list = raceInfoService.selectRaceInfoList(raceInfo);
        return new TableDataInfo(list, (int) new PageInfo(list).getTotal());
    }
    /**
     * 查询比赛类型列表
     */
    @ApiOperation("查询比赛类型列表")
    @GetMapping("/typeList")
    public R<List<RaceType>> list(@ApiParam(name = "raceType", value = "比赛类型信息", required = false)RaceType raceType) {
        List<RaceType> list = raceTypeService.selectRaceTypeList(raceType);
        return R.ok(list);
    }
    @ApiOperation("根据比赛id分页查询预报名老师列表")
    @PostMapping("/teacher/{RaceId}")
    public TableDataInfo getTeachersByRaceId(@ApiParam(name = "RaceId", value = "比赛id", required = true) @PathVariable("RaceId") Long RaceId) {
        //开始分页
        startPage();
        PreSignTeacher prevTeacher =  new PreSignTeacher();
        prevTeacher.setRace(RaceId);
        List<PreSignTeacherVo> preSignTeacherVos = preTeacherService.selectPreSignTeacherList(prevTeacher);
        //返回结果
        return new TableDataInfo(preSignTeacherVos, (int) new PageInfo(preSignTeacherVos).getTotal());
    }
    @ApiOperation("根据比赛id分页查询预报名学生列表")
    @PostMapping("/student/{RaceId}")
    public TableDataInfo getStudentsByRaceId(@ApiParam(name = "RaceId", value = "比赛id", required = true) @PathVariable("RaceId") Long RaceId) {
        //开始分页
        startPage();
        PreSignStudent preSignStudent = new PreSignStudent();
        preSignStudent.setRace(RaceId);
        List<PreSignStudentVo> preSignStudentVos = preStudentService.selectPreSignStudentList(preSignStudent);
        //返回结果
        return new TableDataInfo(preSignStudentVos, (int) new PageInfo(preSignStudentVos).getTotal());
    }
}
