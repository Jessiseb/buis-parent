package com.buis.web.controller.manager;

import com.buis.common.annotation.Log;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.enums.BusinessType;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.domain.Assistant;
import com.buis.service.IAssistantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 学生助理Controller
 *
 * @author buis
 * @date 2024-10-11
 */
@Api(tags = "学生助理Controller")
@RestController
@RequestMapping("/buis/assistant")
public class AssistantController extends BaseController {
    @Autowired
    private IAssistantService assistantService;

    /**
     * 查询学生助理列表
     */
    @ApiOperation("查询学生助理列表")
    @PreAuthorize("@ss.hasPermi('buis:assistant:list')")
    @GetMapping("/list")
    public TableDataInfo list(Assistant assistant) {
        startPage();
        List<Assistant> list = assistantService.selectAssistantList(assistant);
        return getDataTable(list);
    }

    /**
     * 导出学生助理列表
     */
    @ApiOperation("导出学生助理列表")
    @PreAuthorize("@ss.hasPermi('buis:assistant:export')")
    @Log(title = "学生助理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Assistant assistant) {
        List<Assistant> list = assistantService.selectAssistantList(assistant);
        ExcelUtil<Assistant> util = new ExcelUtil<Assistant>(Assistant.class);
        util.exportExcel(response, list, "学生助理数据");
    }

    /**
     * 获取学生助理详细信息
     */
    @ApiOperation("获取学生助理详细信息")
    @PreAuthorize("@ss.hasPermi('buis:assistant:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(assistantService.selectAssistantById(id));
    }

    /**
     * 新增学生助理
     */
    @ApiOperation("新增学生助理")
    @PreAuthorize("@ss.hasPermi('buis:assistant:add')")
    @Log(title = "学生助理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Assistant assistant) {
        return toAjax(assistantService.insertAssistant(assistant));
    }

    /**
     * 修改学生助理
     */
    @ApiOperation("修改学生助理")
    @PreAuthorize("@ss.hasPermi('buis:assistant:edit')")
    @Log(title = "学生助理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Assistant assistant) {
        return toAjax(assistantService.updateAssistant(assistant));
    }

    /**
     * 删除学生助理
     */
    @ApiOperation("删除学生助理")
    @PreAuthorize("@ss.hasPermi('buis:assistant:remove')")
    @Log(title = "学生助理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(assistantService.deleteAssistantByIds(ids));
    }
}
