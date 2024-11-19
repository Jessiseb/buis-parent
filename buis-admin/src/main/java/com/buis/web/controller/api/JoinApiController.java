package com.buis.web.controller.api;

import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.domain.R;
import com.buis.common.core.page.MyPageVo;
import com.buis.common.core.page.TableDataInfo;
import com.buis.domain.PreSignStudent;
import com.buis.domain.PreSignTeacher;
import com.buis.service.IPreSignStudentService;
import com.buis.service.IPreSignTeacherService;
import com.buis.vo.PreSignStudentVo;
import com.buis.vo.PreSignTeacherVo;
import com.github.pagehelper.PageInfo;
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
     * 获取老师预报名详细信息
     */
    @ApiOperation("获取老师预报名详细信息")
    @GetMapping(value = "/preTeacher/{id}")
    public R<PreSignTeacherVo> getTeacherInfo(@PathVariable("id") Long id) {
        return R.ok(preTeacherService.selectPreSignTeacherById(id));
    }
    /**
     * 新增老师预报名
     */
    @ApiOperation("新增老师预报名")
    @PostMapping("/preTeacher")
    public R addPreTeacher(@RequestBody PreSignTeacher preSignTeacher)
    {
        return R.ok(preTeacherService.insertPreSignTeacher(preSignTeacher));

    }

    /**
     * 修改老师预报名
     */
    @ApiOperation("修改老师预报名")
    @PutMapping("/preTeacher")
    public R editPreTeacher(@RequestBody PreSignTeacher preSignTeacher)
    {
        return R.ok(preTeacherService.updatePreSignTeacher(preSignTeacher));
    }

    /**
     * 删除老师预报名
     */
    @ApiOperation("删除老师预报名")
    @DeleteMapping("/preTeacher/{id}")
    public R removePreTeacher(@PathVariable Long id)
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
    @GetMapping(value = "/preStudent/{id}")
    public R<PreSignStudentVo> getStudentInfo(@PathVariable("id") Long id) {
        return R.ok(preStudentService.selectPreSignStudentById(id));
    }

    /**
     * 新增学生预报名
     */
    @ApiOperation("新增学生预报名")
    @PostMapping("/preStudent")
    public R addPreStudent(@RequestBody PreSignStudent preSignStudent) {
        return R.ok(preStudentService.insertPreSignStudent(preSignStudent));
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
     * 删除学生预报名
     */
    @ApiOperation("删除学生预报名")
    @DeleteMapping("/preStudent/{ids}")
    public R removePreStudent(@PathVariable Long[] ids) {
        return R.ok(preStudentService.deletePreSignStudentByIds(ids));
    }

}
