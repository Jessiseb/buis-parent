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
import com.buis.domain.RacePublish;
import com.buis.service.IRacePublishService;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.common.core.page.TableDataInfo;

/**
 * 第二课堂活动发布表Controller
 *
 * @author buis
 * @date 2025-04-04
 */
@Api(tags = "第二课堂活动发布表Controller")
@RestController
@RequestMapping("/buis/publish")
public class RacePublishController extends BaseController
{
    @Autowired
    private IRacePublishService racePublishService;

    /**
     * 查询第二课堂活动发布表列表
     */
    @ApiOperation("查询第二课堂活动发布表列表")
    @PreAuthorize("@ss.hasPermi('buis:publish:list')")
    @GetMapping("/list")
    public TableDataInfo list(RacePublish racePublish)
    {
        startPage();
        List<RacePublish> list = racePublishService.selectRacePublishList(racePublish);
        return getDataTable(list);
    }

    /**
     * 导出第二课堂活动发布表列表
     */
    @ApiOperation("导出第二课堂活动发布表列表")
    @PreAuthorize("@ss.hasPermi('buis:publish:export')")
    @Log(title = "第二课堂活动发布表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RacePublish racePublish)
    {
        List<RacePublish> list = racePublishService.selectRacePublishList(racePublish);
        ExcelUtil<RacePublish> util = new ExcelUtil<RacePublish>(RacePublish.class);
        util.exportExcel(response, list, "第二课堂活动发布表数据");
    }

    /**
     * 获取第二课堂活动发布表详细信息
     */
    @ApiOperation("获取第二课堂活动发布表详细信息")
    @PreAuthorize("@ss.hasPermi('buis:publish:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(racePublishService.selectRacePublishById(id));
    }

    /**
     * 新增第二课堂活动发布表
     */
    @ApiOperation("新增第二课堂活动发布表")
    @PreAuthorize("@ss.hasPermi('buis:publish:add')")
    @Log(title = "第二课堂活动发布表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RacePublish racePublish)
    {
        return toAjax(racePublishService.insertRacePublish(racePublish));
    }

    /**
     * 修改第二课堂活动发布表
     */
    @ApiOperation("修改第二课堂活动发布表")
    @PreAuthorize("@ss.hasPermi('buis:publish:edit')")
    @Log(title = "第二课堂活动发布表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RacePublish racePublish)
    {
        return toAjax(racePublishService.updateRacePublish(racePublish));
    }

    /**
     * 删除第二课堂活动发布表
     */
    @ApiOperation("删除第二课堂活动发布表")
    @PreAuthorize("@ss.hasPermi('buis:publish:remove')")
    @Log(title = "第二课堂活动发布表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(racePublishService.deleteRacePublishByIds(ids));
    }
}
