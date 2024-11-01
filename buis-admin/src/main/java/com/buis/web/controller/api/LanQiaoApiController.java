package com.buis.web.controller.api;

import cn.dev33.satoken.stp.StpUtil;
import com.buis.common.annotation.Log;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.enums.BusinessType;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.SecurityUtils;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.domain.RaceLanqiaoStudent;
import com.buis.domain.RaceLanqiaoTeacher;
import com.buis.domain.RaceLanqiaoType;
import com.buis.service.IRaceLanqiaoApiService;
import com.buis.service.IRaceLanqiaoStudentService;
import com.buis.service.IRaceLanqiaoTeacherService;
import com.buis.service.IRaceLanqiaoTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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
    @Autowired
    private IRaceLanqiaoApiService raceLanqiaoApiService;
    /**
     * 删除蓝桥杯学生报名信息
     */
    @ApiOperation("删除蓝桥杯报名信息")
    @DeleteMapping("/{flag}")
    public AjaxResult remove(@PathVariable Integer flag)
    {
        Long userId = SecurityUtils.getFrontLoginUserId();
        return toAjax(raceLanqiaoApiService.deleteLanqiaoStudentOrTeacherByUserId(userId));
    }
    /**
     * 获取蓝桥杯学生报名信息详细信息
     */
    @ApiOperation("获取蓝桥杯报名信息详细信息")
    @GetMapping("/{flag}")
    public AjaxResult getInfo()
    {
        Long userId = SecurityUtils.getFrontLoginUserId();
        return success(raceLanqiaoStudentService.selectRaceLanqiaoStudentByUserId(userId));
    }

    /**
     * 获取蓝桥杯老师报名信息详细信息
     */
    @ApiOperation("获取蓝桥杯老师报名信息详细信息")
    @GetMapping("teacher")
    public AjaxResult getTeacherInfo()
    {
        Long userId = SecurityUtils.getFrontLoginUserId();
        return success(raceLanqiaoTeacherService.selectRaceLanqiaoTeacherByUserId(userId));
    }

    /**
     * 新增蓝桥杯学生报名信息
     */
    @ApiOperation("新增蓝桥杯学生报名信息")
    @PostMapping("/{flag}")
    public AjaxResult add(@RequestBody RaceLanqiaoStudent raceLanqiaoStudent)
    {

        Long userId = SecurityUtils.getFrontLoginUserId();
        //判断userId是否新增过老师报名
        raceLanqiaoStudent.setUserId(userId);
        return toAjax(raceLanqiaoStudentService.insertRaceLanqiaoStudent(raceLanqiaoStudent));
    }
    /**
     * 新增蓝桥杯老师报名信息
     */
    @ApiOperation("新增蓝桥杯老师报名信息")
    @PostMapping("/{flag}")
    public AjaxResult add(@RequestBody RaceLanqiaoTeacher raceLanqiaoTeacher)
    {        Long userId = SecurityUtils.getFrontLoginUserId();

        raceLanqiaoTeacher.setUserId(userId);
        return toAjax(raceLanqiaoTeacherService.insertRaceLanqiaoTeacher(raceLanqiaoTeacher));
    }
    /**
     * 修改蓝桥杯学生报名信息
     */
    @ApiOperation("修改蓝桥杯学生报名信息")
    @PutMapping("/{flag}")
    public AjaxResult edit(@RequestBody RaceLanqiaoStudent raceLanqiaoStudent)
    {
        Long userId = SecurityUtils.getFrontLoginUserId();
        if (userId != null && userId.equals(raceLanqiaoStudent.getUserId())) {
            return toAjax(raceLanqiaoStudentService.updateRaceLanqiaoStudent(raceLanqiaoStudent));

        }
        throw new ServiceException();
    }
    /**
     * 修改蓝桥杯老师报名信息
     */
    @ApiOperation("修改蓝桥杯老师报名信息")
    @PutMapping("/{flag}")
    public AjaxResult edit(@RequestBody RaceLanqiaoTeacher raceLanqiaoTeacher)
    {
        Long userId = SecurityUtils.getFrontLoginUserId();

        return toAjax(raceLanqiaoTeacherService.updateRaceLanqiaoTeacher(raceLanqiaoTeacher));
    }

    /**
     * 查询蓝桥杯赛道类型列表
     */
    @ApiOperation("查询蓝桥杯赛道类型列表")
    @GetMapping("/list")
    public AjaxResult list(RaceLanqiaoType raceLanqiaoType)
    {
        List<RaceLanqiaoType> list = raceLanqiaoTypeService.selectRaceLanqiaoTypeList(raceLanqiaoType);
        return success(list);
    }

}
