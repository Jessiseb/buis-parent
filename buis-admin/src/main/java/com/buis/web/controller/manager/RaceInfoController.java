package com.buis.web.controller.manager;

import com.buis.common.annotation.Log;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.enums.BusinessType;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.domain.RaceInfo;
import com.buis.service.IRaceInfoService;
import com.buis.vo.RaceInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 比赛信息Controller
 *
 * @author kaven
 * @date 2024-10-23
 */
@Api(tags = "比赛信息Controller")
@RestController
@RequestMapping("/buis/raceInfo")
public class RaceInfoController extends BaseController {
    @Autowired
    private IRaceInfoService raceInfoService;

    /**
     * 查询比赛信息列表
     */
    @ApiOperation("查询比赛信息列表")
    @PreAuthorize("@ss.hasPermi('buis:raceInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(RaceInfo raceInfo) {
        startPage();
        List<RaceInfoVo> list = raceInfoService.selectRaceInfoList(raceInfo);
        return getDataTable(list);
    }

    /**
     * 导出比赛信息列表
     */
    @ApiOperation("导出比赛信息列表")
    @PreAuthorize("@ss.hasPermi('buis:raceInfo:export')")
    @Log(title = "比赛信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RaceInfo raceInfo) {
        List<RaceInfoVo> list = raceInfoService.selectRaceInfoList(raceInfo);
        ExcelUtil<RaceInfoVo> util = new ExcelUtil<RaceInfoVo>(RaceInfoVo.class);
        util.exportExcel(response, list, "比赛信息数据");
    }

    /**
     * 获取比赛信息详细信息
     */
    @ApiOperation("获取比赛信息详细信息")
    @PreAuthorize("@ss.hasPermi('buis:raceInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(raceInfoService.selectRaceInfoById(id));
    }

    /**
     * 新增比赛信息
     */
    @ApiOperation("新增比赛信息")
    @PreAuthorize("@ss.hasPermi('buis:raceInfo:add')")
    @Log(title = "比赛信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RaceInfo raceInfo) {
        return toAjax(raceInfoService.insertRaceInfo(raceInfo));
    }

    /**
     * 修改比赛信息
     */
    @ApiOperation("修改比赛信息")
    @PreAuthorize("@ss.hasPermi('buis:raceInfo:edit')")
    @Log(title = "比赛信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RaceInfo raceInfo) {
        return toAjax(raceInfoService.updateRaceInfo(raceInfo));
    }

    /**
     * 删除比赛信息
     */
    @ApiOperation("删除比赛信息")
    @PreAuthorize("@ss.hasPermi('buis:raceInfo:remove')")
    @Log(title = "比赛信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(raceInfoService.deleteRaceInfoByIds(ids));
    }
}
