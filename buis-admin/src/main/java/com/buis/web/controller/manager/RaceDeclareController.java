package com.buis.web.controller.manager;

import com.buis.common.annotation.Log;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.enums.BusinessType;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.domain.RaceDeclare;
import com.buis.service.IRaceDeclareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 第二课堂活动申报表Controller
 *
 * @author buis
 * @date 2025-04-03
 */
@Api(tags = "第二课堂活动申报表Controller")
@RestController
@RequestMapping("/buis/declare")
public class RaceDeclareController extends BaseController {
    @Autowired
    private IRaceDeclareService raceDeclareService;

    /**
     * 查询第二课堂活动申报表列表
     */
    @ApiOperation("查询第二课堂活动申报表列表")
    @PreAuthorize("@ss.hasPermi('buis:declare:list')")
    @GetMapping("/list")
    public TableDataInfo list(RaceDeclare raceDeclare) {
        startPage();
        List<RaceDeclare> list = raceDeclareService.selectRaceDeclareList(raceDeclare);
        return getDataTable(list);
    }

    /**
     * 导出第二课堂活动申报表列表
     */
    @ApiOperation("导出第二课堂活动申报表列表")
    @PreAuthorize("@ss.hasPermi('buis:declare:export')")
    @Log(title = "第二课堂活动申报表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RaceDeclare raceDeclare)
    {
        List<RaceDeclare> list = raceDeclareService.selectRaceDeclareList(raceDeclare);
        ExcelUtil<RaceDeclare> util = new ExcelUtil<RaceDeclare>(RaceDeclare.class);
        util.exportExcel(response, list, "第二课堂活动申报表数据");
    }

    /**
     * 获取第二课堂活动申报表详细信息
     */
    @ApiOperation("获取第二课堂活动申报表详细信息")
    @PreAuthorize("@ss.hasPermi('buis:declare:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(raceDeclareService.selectRaceDeclareById(id));
    }

    /**
     * 新增第二课堂活动申报表
     */
    @ApiOperation("新增第二课堂活动申报表")
    @PreAuthorize("@ss.hasPermi('buis:declare:add')")
    @Log(title = "第二课堂活动申报表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RaceDeclare raceDeclare)
    {
        return toAjax(raceDeclareService.insertRaceDeclare(raceDeclare));
    }

    /**
     * 修改第二课堂活动申报表
     */
    @ApiOperation("修改第二课堂活动申报表")
    @PreAuthorize("@ss.hasPermi('buis:declare:edit')")
    @Log(title = "第二课堂活动申报表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RaceDeclare raceDeclare)
    {
        return toAjax(raceDeclareService.updateRaceDeclare(raceDeclare));
    }

    /**
     * 删除第二课堂活动申报表
     */
    @ApiOperation("删除第二课堂活动申报表")
    @PreAuthorize("@ss.hasPermi('buis:declare:remove')")
    @Log(title = "第二课堂活动申报表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(raceDeclareService.deleteRaceDeclareByIds(ids));
    }
}
