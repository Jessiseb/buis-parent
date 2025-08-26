package com.buis.web.controller.manager;

import com.buis.common.annotation.Log;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.enums.BusinessType;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.domain.Second;
import com.buis.service.ISecondService;
import com.buis.vo.RaceSecondVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 关联第二课堂表Controller
 *
 * @author buis
 * @date 2025-04-04
 */
@Api(tags = "关联第二课堂表Controller")
@RestController
@RequestMapping("/buis/second")
public class SecondController extends BaseController {
    @Autowired
    private ISecondService SecondService;

    /**
     * 查询关联第二课堂表列表
     */
    @ApiOperation("查询关联第二课堂表列表")
    @PreAuthorize("@ss.hasPermi('buis:second:list')")
    @GetMapping("/list")
    public TableDataInfo list(Second second) {
        startPage();
        List<Second> list = SecondService.selectSecondList(second);
        return getDataTable(list);
    }

    /**
     * 导出关联第二课堂表列表
     */
    @ApiOperation("导出关联第二课堂表列表")
    @PreAuthorize("@ss.hasPermi('buis:second:export')")
    @Log(title = "关联第二课堂表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Second second)
    {
        List<Second> list = SecondService.selectSecondList(second);
        ExcelUtil<Second> util = new ExcelUtil<Second>(Second.class);
        util.exportExcel(response, list, "关联第二课堂表数据");
    }

    /**
     * 获取关联第二课堂表详细信息
     */
    @ApiOperation("获取关联第二课堂表详细信息")
    @PreAuthorize("@ss.hasPermi('buis:second:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(SecondService.selectSecondById(id));
    }

    /**
     * 新增关联第二课堂表
     */
    @ApiOperation("新增关联第二课堂表")  // swagger相关注解
    @PreAuthorize("@ss.hasPermi('buis:second:add')")
    @Log(title = "关联第二课堂表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Second second)
    {
        return toAjax(SecondService.insertSecond(second));
    }

    /**
     * 修改关联第二课堂表
     */
    @ApiOperation("修改关联第二课堂表")
    @PreAuthorize("@ss.hasPermi('buis:second:edit')")
    @Log(title = "关联第二课堂表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Second second)
    {
        return toAjax(SecondService.updateSecond(second));
    }

    /**
     * 删除关联第二课堂表
     */
    @ApiOperation("删除关联第二课堂表")
    @PreAuthorize("@ss.hasPermi('buis:second:remove')")
    @Log(title = "关联第二课堂表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(SecondService.deleteSecondByIds(ids));
    }
}
