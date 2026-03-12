package com.buis.web.controller.manager;


import com.buis.common.annotation.Log;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.enums.BusinessType;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.model.domain.RaceSecond;
import com.buis.service.IRaceSecondService;
import com.buis.model.vo.RaceSecondVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 第二课堂和race_info中间表Controller
 *
 * @author buis
 * @date 2025-04-07
 */
@Api(tags = "第二课堂和race_info中间表Controller")
@RestController
@RequestMapping("/buis/RaceSecond")
public class RaceSecondController extends BaseController {
    @Autowired
    private IRaceSecondService raceSecondService;

    /**
     * 查询第二课堂和race_info中间表列表
     */
    @ApiOperation("查询第二课堂和race_info中间表列表")
    @PreAuthorize("@ss.hasPermi('buis:RaceSecond:list')")
    @GetMapping("/list")
    public TableDataInfo list(RaceSecondVo raceSecondVo) {
        startPage();
        List<RaceSecondVo> list = raceSecondService.selectRaceSecondList(raceSecondVo);
        return getDataTable(list);
    }

    /**
     * 导出第二课堂和race_info中间表列表
     */
    @ApiOperation("导出第二课堂和race_info中间表列表")
    @PreAuthorize("@ss.hasPermi('buis:RaceSecond:export')")
    @Log(title = "第二课堂和race_info中间表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RaceSecondVo raceSecondVo) {
        List<RaceSecondVo> list = raceSecondService.selectRaceSecondList(raceSecondVo);
        ExcelUtil<RaceSecondVo> util = new ExcelUtil<RaceSecondVo>(RaceSecondVo.class);
        util.exportExcel(response, list, "第二课堂和race_info中间表数据");
    }

    /**
     * 获取第二课堂和race_info中间表详细信息
     */
    @ApiOperation("获取第二课堂和race_info中间表详细信息")
    @PreAuthorize("@ss.hasPermi('buis:RaceSecond:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(raceSecondService.selectRaceSecondById(id));
    }

    /**
     * 新增第二课堂和race_info中间表
     */
    @ApiOperation("新增第二课堂和race_info中间表")
    @PreAuthorize("@ss.hasPermi('buis:RaceSecond:add')")
    @Log(title = "第二课堂和race_info中间表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RaceSecond raceSecond) {
        return toAjax(raceSecondService.insertRaceSecond(raceSecond));
    }

    /**
     * 修改第二课堂和race_info中间表
     */
    @ApiOperation("修改第二课堂和race_info中间表")
    @PreAuthorize("@ss.hasPermi('buis:RaceSecond:edit')")
    @Log(title = "第二课堂和race_info中间表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RaceSecond raceSecond) {
        return toAjax(raceSecondService.updateRaceSecond(raceSecond));
    }

    /**
     * 删除第二课堂和race_info中间表
     */
    @ApiOperation("删除第二课堂和race_info中间表")
    @PreAuthorize("@ss.hasPermi('buis:RaceSecond:remove')")
    @Log(title = "第二课堂和race_info中间表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(raceSecondService.deleteRaceSecondByIds(ids));
    }
}
