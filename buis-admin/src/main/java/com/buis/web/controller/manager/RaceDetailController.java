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
import com.buis.domain.RaceDetail;
import com.buis.service.IRaceDetailService;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.common.core.page.TableDataInfo;

/**
 * 比赛详情Controller
 *
 * @author buis
 * @date 2024-10-11
 */
@Api(tags = "比赛详情Controller")
@RestController
@RequestMapping("/buis/raceDetail")
public class RaceDetailController extends BaseController
{
    @Autowired
    private IRaceDetailService raceDetailService;

/**
 * 查询比赛详情列表
 */
@ApiOperation("查询比赛详情列表")
@PreAuthorize("@ss.hasPermi('buis:raceDetail:list')")
@GetMapping("/list")
    public TableDataInfo list(RaceDetail raceDetail)
    {
        startPage();
        List<RaceDetail> list = raceDetailService.selectRaceDetailList(raceDetail);
        return getDataTable(list);
    }

    /**
     * 导出比赛详情列表
     */
    @ApiOperation("导出比赛详情列表")
    @PreAuthorize("@ss.hasPermi('buis:raceDetail:export')")
    @Log(title = "比赛详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RaceDetail raceDetail)
    {
        List<RaceDetail> list = raceDetailService.selectRaceDetailList(raceDetail);
        ExcelUtil<RaceDetail> util = new ExcelUtil<RaceDetail>(RaceDetail.class);
        util.exportExcel(response, list, "比赛详情数据");
    }

    /**
     * 获取比赛详情详细信息
     */
    @ApiOperation("获取比赛详情详细信息")
    @PreAuthorize("@ss.hasPermi('buis:raceDetail:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(raceDetailService.selectRaceDetailById(id));
    }

    /**
     * 新增比赛详情
     */
    @ApiOperation("新增比赛详情")
    @PreAuthorize("@ss.hasPermi('buis:raceDetail:add')")
    @Log(title = "比赛详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RaceDetail raceDetail)
    {
        return toAjax(raceDetailService.insertRaceDetail(raceDetail));
    }

    /**
     * 修改比赛详情
     */
    @ApiOperation("修改比赛详情")
    @PreAuthorize("@ss.hasPermi('buis:raceDetail:edit')")
    @Log(title = "比赛详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RaceDetail raceDetail)
    {
        return toAjax(raceDetailService.updateRaceDetail(raceDetail));
    }

    /**
     * 删除比赛详情
     */
    @ApiOperation("删除比赛详情")
    @PreAuthorize("@ss.hasPermi('buis:raceDetail:remove')")
    @Log(title = "比赛详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(raceDetailService.deleteRaceDetailByIds(ids));
    }
}
