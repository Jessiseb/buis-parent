package com.buis.controller;

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
import com.buis.domain.AwardRecords;
import com.buis.service.IAwardRecordsService;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.common.core.page.TableDataInfo;

/**
 * 奖状汇总Controller
 *
 * @author zjx
 * @date 2025-03-17
 */
@Api(tags = "奖状汇总Controller")
@RestController
@RequestMapping("/buis/records")
public class AwardRecordsController extends BaseController
{
    @Autowired
    private IAwardRecordsService awardRecordsService;

/**
 * 查询奖状汇总列表
 */
@ApiOperation("查询奖状汇总列表")
@PreAuthorize("@ss.hasPermi('buis:records:list')")
@GetMapping("/list")
    public TableDataInfo list(AwardRecords awardRecords)
    {
        startPage();
        List<AwardRecords> list = awardRecordsService.selectAwardRecordsList(awardRecords);
        return getDataTable(list);
    }

    /**
     * 导出奖状汇总列表
     */
    @ApiOperation("导出奖状汇总列表")
    @PreAuthorize("@ss.hasPermi('buis:records:export')")
    @Log(title = "奖状汇总", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AwardRecords awardRecords)
    {
        List<AwardRecords> list = awardRecordsService.selectAwardRecordsList(awardRecords);
        ExcelUtil<AwardRecords> util = new ExcelUtil<AwardRecords>(AwardRecords.class);
        util.exportExcel(response, list, "奖状汇总数据");
    }

    /**
     * 获取奖状汇总详细信息
     */
    @ApiOperation("获取奖状汇总详细信息")
    @PreAuthorize("@ss.hasPermi('buis:records:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(awardRecordsService.selectAwardRecordsById(id));
    }

    /**
     * 新增奖状汇总
     */
    @ApiOperation("新增奖状汇总")
    @PreAuthorize("@ss.hasPermi('buis:records:add')")
    @Log(title = "奖状汇总", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AwardRecords awardRecords)
    {
        return toAjax(awardRecordsService.insertAwardRecords(awardRecords));
    }

    /**
     * 修改奖状汇总
     */
    @ApiOperation("修改奖状汇总")
    @PreAuthorize("@ss.hasPermi('buis:records:edit')")
    @Log(title = "奖状汇总", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AwardRecords awardRecords)
    {
        return toAjax(awardRecordsService.updateAwardRecords(awardRecords));
    }

    /**
     * 删除奖状汇总
     */
    @ApiOperation("删除奖状汇总")
    @PreAuthorize("@ss.hasPermi('buis:records:remove')")
    @Log(title = "奖状汇总", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(awardRecordsService.deleteAwardRecordsByIds(ids));
    }
}
