package com.buis.web.controller.api;

import com.buis.common.annotation.Log;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.domain.R;
import com.buis.common.core.page.MyPageVo;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.enums.BusinessType;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.domain.PreSignStudent;
import com.buis.domain.PreSignTeacher;
import com.buis.domain.RaceInfo;
import com.buis.domain.RaceType;
import com.buis.service.IPreSignStudentService;
import com.buis.service.IPreSignTeacherService;
import com.buis.service.IRaceInfoService;
import com.buis.service.IRaceTypeService;
import com.buis.vo.PreSignStudentVo;
import com.buis.vo.PreSignTeacherVo;
import com.buis.vo.RaceInfoVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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


    /**
     * 根据比赛id分页查询预报名老师列表
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
        return new MyPageVo(preSignTeacherVos, new PageInfo(preSignTeacherVos).getTotal());
    }

    /**
     * 查询老师预报名列表
     */
    @ApiOperation("查询老师预报名列表")
    @GetMapping("/list")
    public TableDataInfo list(PreSignTeacher preSignTeacher) {
        startPage();
        List<PreSignTeacherVo> list = preTeacherService.selectPreSignTeacherList(preSignTeacher);
        return getDataTable(list);
    }


    /**
     * 获取老师预报名详细信息
     */
    @ApiOperation("获取老师预报名详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getTeacherInfo(@PathVariable("id") Long id) {
        return success(preTeacherService.selectPreSignTeacherById(id));
    }
    /**
     * 新增老师预报名
     */
    @ApiOperation("新增老师预报名")
    @PostMapping
    public R add(@RequestBody PreSignTeacher preSignTeacher)
    {
        return R.ok(preTeacherService.insertPreSignTeacher(preSignTeacher));

    }

    /**
     * 修改老师预报名
     */
    @ApiOperation("修改老师预报名")
    @PutMapping
    public R edit(@RequestBody PreSignTeacher preSignTeacher)
    {
        return R.ok(preTeacherService.updatePreSignTeacher(preSignTeacher));
    }

    /**
     * 删除老师预报名
     */
    @ApiOperation("删除老师预报名")
    @DeleteMapping("/{id}")
    public R removeTeacher(@PathVariable Long id)
    {
        return R.ok(preTeacherService.deletePreSignTeacherById(id));
    }
    /**
     * 根据比赛id分页查询预报名学生列表
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
        return new MyPageVo(preSignStudentVos, new PageInfo(preSignStudentVos).getTotal());
    }

    /**
     * 获取学生预报名详细信息
     */
    @ApiOperation("获取学生预报名详细信息")
    @GetMapping(value = "/{id}")
    public R getStudentInfo(@PathVariable("id") Long id) {
        return R.ok(preStudentService.selectPreSignStudentById(id));
    }

    /**
     * 新增学生预报名
     */
    @ApiOperation("新增学生预报名")
    @PostMapping
    public R add(@RequestBody PreSignStudent preSignStudent) {
        return R.ok(preStudentService.insertPreSignStudent(preSignStudent));
    }

    /**
     * 修改学生预报名
     */
    @ApiOperation("修改学生预报名")
    @PutMapping
    public R edit(@RequestBody PreSignStudent preSignStudent) {
        return R.ok(preStudentService.updatePreSignStudent(preSignStudent));
    }

    /**
     * 删除学生预报名
     */
    @ApiOperation("删除学生预报名")
    @DeleteMapping("/{ids}")
    public R removeStudent(@PathVariable Long[] ids) {
        return R.ok(preStudentService.deletePreSignStudentByIds(ids));
    }

}
