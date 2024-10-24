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
import com.buis.domain.RaceLanqiaoStudent;
import com.buis.service.IRaceLanqiaoStudentService;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.common.core.page.TableDataInfo;

/**
 * 蓝桥杯学生报名信息Controller
 *
 * @author buis
 * @date 2024-10-24
 */
@Api(tags = "蓝桥杯学生报名信息Controller")
@RestController
@RequestMapping("/buis/lanQiaoStudent")
public class RaceLanqiaoStudentController extends BaseController
{
    @Autowired
    private IRaceLanqiaoStudentService raceLanqiaoStudentService;

/**
 * 查询蓝桥杯学生报名信息列表
 */
@ApiOperation("查询蓝桥杯学生报名信息列表")
@PreAuthorize("@ss.hasPermi('buis:lanQiaoStudent:list')")
@GetMapping("/list")
    public TableDataInfo list(RaceLanqiaoStudent raceLanqiaoStudent)
    {
        startPage();
        List<RaceLanqiaoStudent> list = raceLanqiaoStudentService.selectRaceLanqiaoStudentList(raceLanqiaoStudent);
        return getDataTable(list);
    }

    /**
     * 导出蓝桥杯学生报名信息列表
     */
    @ApiOperation("导出蓝桥杯学生报名信息列表")
    @PreAuthorize("@ss.hasPermi('buis:lanQiaoStudent:export')")
    @Log(title = "蓝桥杯学生报名信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RaceLanqiaoStudent raceLanqiaoStudent)
    {
        List<RaceLanqiaoStudent> list = raceLanqiaoStudentService.selectRaceLanqiaoStudentList(raceLanqiaoStudent);
        ExcelUtil<RaceLanqiaoStudent> util = new ExcelUtil<RaceLanqiaoStudent>(RaceLanqiaoStudent.class);
        util.exportExcel(response, list, "蓝桥杯学生报名信息数据");
    }

    /**
     * 获取蓝桥杯学生报名信息详细信息
     */
    @ApiOperation("获取蓝桥杯学生报名信息详细信息")
    @PreAuthorize("@ss.hasPermi('buis:lanQiaoStudent:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(raceLanqiaoStudentService.selectRaceLanqiaoStudentById(id));
    }

    /**
     * 新增蓝桥杯学生报名信息
     */
    @ApiOperation("新增蓝桥杯学生报名信息")
    @PreAuthorize("@ss.hasPermi('buis:lanQiaoStudent:add')")
    @Log(title = "蓝桥杯学生报名信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RaceLanqiaoStudent raceLanqiaoStudent)
    {
        return toAjax(raceLanqiaoStudentService.insertRaceLanqiaoStudent(raceLanqiaoStudent));
    }

    /**
     * 修改蓝桥杯学生报名信息
     */
    @ApiOperation("修改蓝桥杯学生报名信息")
    @PreAuthorize("@ss.hasPermi('buis:lanQiaoStudent:edit')")
    @Log(title = "蓝桥杯学生报名信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RaceLanqiaoStudent raceLanqiaoStudent)
    {
        return toAjax(raceLanqiaoStudentService.updateRaceLanqiaoStudent(raceLanqiaoStudent));
    }

    /**
     * 删除蓝桥杯学生报名信息
     */
    @ApiOperation("删除蓝桥杯学生报名信息")
    @PreAuthorize("@ss.hasPermi('buis:lanQiaoStudent:remove')")
    @Log(title = "蓝桥杯学生报名信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(raceLanqiaoStudentService.deleteRaceLanqiaoStudentByIds(ids));
    }
}
