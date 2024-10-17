package com.buis.web.controller.manager;

import com.buis.common.annotation.Log;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.enums.BusinessType;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.domain.PreSignStudent;
import com.buis.service.IPreSignStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 学生预报名Controller
 *
 * @author buis
 * @date 2024-10-11
 */
@Api(tags = "学生预报名Controller")
@RestController
@RequestMapping("/buis/preStudent")
public class PreSignStudentController extends BaseController {
    @Autowired
    private IPreSignStudentService preSignStudentService;

    /**
     * 查询学生预报名列表
     */
    @ApiOperation("查询学生预报名列表")
    @PreAuthorize("@ss.hasPermi('buis:preStudent:list')")
    @GetMapping("/list")
    public TableDataInfo list(PreSignStudent preSignStudent) {
        startPage();
        List<PreSignStudent> list = preSignStudentService.selectPreSignStudentList(preSignStudent);
        return getDataTable(list);
    }

    /**
     * 导出学生预报名列表
     */
    @ApiOperation("导出学生预报名列表")
    @PreAuthorize("@ss.hasPermi('buis:preStudent:export')")
    @Log(title = "学生预报名", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PreSignStudent preSignStudent)
    {
        List<PreSignStudent> list = preSignStudentService.selectPreSignStudentList(preSignStudent);
        ExcelUtil<PreSignStudent> util = new ExcelUtil<PreSignStudent>(PreSignStudent.class);
        util.exportExcel(response, list, "学生预报名数据");
    }

    /**
     * 获取学生预报名详细信息
     */
    @ApiOperation("获取学生预报名详细信息")
    @PreAuthorize("@ss.hasPermi('buis:preStudent:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(preSignStudentService.selectPreSignStudentById(id));
    }

    /**
     * 新增学生预报名
     */
    @ApiOperation("新增学生预报名")
    @PreAuthorize("@ss.hasPermi('buis:preStudent:add')")
    @Log(title = "学生预报名", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PreSignStudent preSignStudent)
    {
        return toAjax(preSignStudentService.insertPreSignStudent(preSignStudent));
    }

    /**
     * 修改学生预报名
     */
    @ApiOperation("修改学生预报名")
    @PreAuthorize("@ss.hasPermi('buis:preStudent:edit')")
    @Log(title = "学生预报名", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PreSignStudent preSignStudent)
    {
        return toAjax(preSignStudentService.updatePreSignStudent(preSignStudent));
    }

    /**
     * 删除学生预报名
     */
    @ApiOperation("删除学生预报名")
    @PreAuthorize("@ss.hasPermi('buis:preStudent:remove')")
    @Log(title = "学生预报名", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(preSignStudentService.deletePreSignStudentByIds(ids));
    }
}
