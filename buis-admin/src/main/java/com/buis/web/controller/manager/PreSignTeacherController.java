package com.buis.web.controller.manager;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.buis.domain.vo.PreSignTeacherVo;
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
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.enums.BusinessType;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.domain.PreSignTeacher;
import com.buis.service.IPreSignTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 老师预报名Controller
 *
 * @author buis
 * @date 2024-10-11
 */
@Api(tags = "老师预报名Controller")
@RestController
@RequestMapping("/buis/preTeacher")
public class PreSignTeacherController extends BaseController {
    @Autowired
    private IPreSignTeacherService preSignTeacherService;

    /**
     * 查询老师预报名列表
     */
    @ApiOperation("查询老师预报名列表")
    @PreAuthorize("@ss.hasPermi('buis:preTeacher:list')")
    @GetMapping("/list")
    public TableDataInfo list(PreSignTeacher preSignTeacher) {
        startPage();
//        List<PreSignTeacher> list = preSignTeacherService.selectPreSignTeacherList(preSignTeacher);
       List<PreSignTeacherVo> listVo= preSignTeacherService.selectPreSignTeacherVoList(preSignTeacher);
        return getDataTable(listVo);
    }

    /**
     * 导出老师预报名列表
     */
    @ApiOperation("导出老师预报名列表")
    @PreAuthorize("@ss.hasPermi('buis:preTeacher:export')")
    @Log(title = "老师预报名", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PreSignTeacher preSignTeacher) {
        List<PreSignTeacher> list = preSignTeacherService.selectPreSignTeacherList(preSignTeacher);
        ExcelUtil<PreSignTeacher> util = new ExcelUtil<PreSignTeacher>(PreSignTeacher.class);
        util.exportExcel(response, list, "老师预报名数据");
    }

    /**
     * 获取老师预报名详细信息
     */
    @ApiOperation("获取老师预报名详细信息")
    @PreAuthorize("@ss.hasPermi('buis:preTeacher:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
//        return success(preSignTeacherService.selectPreSignTeacherById(id));
        return success(preSignTeacherService.selectPreSignTeacherVoById(id));
    }
    /**
     * 新增老师预报名
     */
    @ApiOperation("新增老师预报名")
    @PreAuthorize("@ss.hasPermi('buis:preTeacher:add')")
    @Log(title = "老师预报名", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PreSignTeacher preSignTeacher)
    {
        return toAjax(preSignTeacherService.insertPreSignTeacher(preSignTeacher));

    }

    /**
     * 修改老师预报名
     */
    @ApiOperation("修改老师预报名")
    @PreAuthorize("@ss.hasPermi('buis:preTeacher:edit')")
    @Log(title = "老师预报名", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PreSignTeacher preSignTeacher)
    {
        return toAjax(preSignTeacherService.updatePreSignTeacher(preSignTeacher));
    }

    /**
     * 删除老师预报名
     */
    @ApiOperation("删除老师预报名")
    @PreAuthorize("@ss.hasPermi('buis:preTeacher:remove')")
    @Log(title = "老师预报名", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(preSignTeacherService.deletePreSignTeacherByIds(ids));
    }
}
