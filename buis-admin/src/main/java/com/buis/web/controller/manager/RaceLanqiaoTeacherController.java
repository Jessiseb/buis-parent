package com.buis.web.controller.manager;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.buis.common.annotation.Log;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.enums.BusinessType;
import com.buis.domain.RaceLanqiaoTeacher;
import com.buis.service.IRaceLanqiaoTeacherService;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.common.core.page.TableDataInfo;

/**
 * 蓝桥杯老师报名信息Controller
 *
 * @author buis
 * @date 2024-10-24
 */
@Api(tags = "蓝桥杯老师报名信息Controller")
@RestController
@RequestMapping("/buis/lanQiaoTeacher")
public class RaceLanqiaoTeacherController extends BaseController
{
    @Autowired
    private IRaceLanqiaoTeacherService raceLanqiaoTeacherService;

/**
 * 查询蓝桥杯老师报名信息列表
 */
@ApiOperation("查询蓝桥杯老师报名信息列表")
@PreAuthorize("@ss.hasPermi('buis:lanQiaoTeacher:list')")
@GetMapping("/list")
    public TableDataInfo list(RaceLanqiaoTeacher raceLanqiaoTeacher)
    {
        startPage();
        List<RaceLanqiaoTeacher> list = raceLanqiaoTeacherService.selectRaceLanqiaoTeacherList(raceLanqiaoTeacher);
        return getDataTable(list);
    }

    /**
     * 导出蓝桥杯老师报名信息列表
     */
    @ApiOperation("导出蓝桥杯老师报名信息列表")
    @PreAuthorize("@ss.hasPermi('buis:lanQiaoTeacher:export')")
    @Log(title = "蓝桥杯老师报名信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RaceLanqiaoTeacher raceLanqiaoTeacher)
    {
        List<RaceLanqiaoTeacher> list = raceLanqiaoTeacherService.selectRaceLanqiaoTeacherList(raceLanqiaoTeacher);
        ExcelUtil<RaceLanqiaoTeacher> util = new ExcelUtil<RaceLanqiaoTeacher>(RaceLanqiaoTeacher.class);
        util.exportExcel(response, list, "蓝桥杯老师报名信息数据");
    }

    /**
     * 获取蓝桥杯老师报名信息详细信息
     */
    @ApiOperation("获取蓝桥杯老师报名信息详细信息")
    @PreAuthorize("@ss.hasPermi('buis:lanQiaoTeacher:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(raceLanqiaoTeacherService.selectRaceLanqiaoTeacherById(id));
    }

    /**
     * 新增蓝桥杯老师报名信息
     */
    @ApiOperation("新增蓝桥杯老师报名信息")
    @PreAuthorize("@ss.hasPermi('buis:lanQiaoTeacher:add')")
    @Log(title = "蓝桥杯老师报名信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RaceLanqiaoTeacher raceLanqiaoTeacher)
    {
        return toAjax(raceLanqiaoTeacherService.insertRaceLanqiaoTeacher(raceLanqiaoTeacher));
    }

    /**
     * 修改蓝桥杯老师报名信息
     */
    @ApiOperation("修改蓝桥杯老师报名信息")
    @PreAuthorize("@ss.hasPermi('buis:lanQiaoTeacher:edit')")
    @Log(title = "蓝桥杯老师报名信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RaceLanqiaoTeacher raceLanqiaoTeacher)
    {
        return toAjax(raceLanqiaoTeacherService.updateRaceLanqiaoTeacher(raceLanqiaoTeacher));
    }

    /**
     * 删除蓝桥杯老师报名信息
     */
    @ApiOperation("删除蓝桥杯老师报名信息")
    @PreAuthorize("@ss.hasPermi('buis:lanQiaoTeacher:remove')")
    @Log(title = "蓝桥杯老师报名信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(raceLanqiaoTeacherService.deleteRaceLanqiaoTeacherByIds(ids));
    }
}
