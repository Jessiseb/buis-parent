package com.buis.web.controller.manager;

import com.buis.common.annotation.Log;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.enums.BusinessType;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.model.domain.RaceType;
import com.buis.service.IRaceTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 比赛类型Controller
 *
 * @author buis
 * @date 2024-10-23
 */
@Api(tags = "比赛类型Controller")
@RestController
@RequestMapping("/buis/raceType")
public class RaceTypeController extends BaseController {
    @Autowired
    private IRaceTypeService raceTypeService;

    /**
     * 查询比赛类型列表
     */
    @ApiOperation("查询比赛类型列表")
    @PreAuthorize("@ss.hasPermi('buis:raceType:list')")
    @GetMapping("/list")
    public AjaxResult list(RaceType raceType) {
        List<RaceType> list = raceTypeService.selectRaceTypeList(raceType);
        return success(list);
    }


    /**
     * 导出比赛类型列表
     */
    @ApiOperation("导出比赛类型列表")
    @PreAuthorize("@ss.hasPermi('buis:raceType:export')")
    @Log(title = "比赛类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RaceType raceType) {
        List<RaceType> list = raceTypeService.selectRaceTypeList(raceType);
        ExcelUtil<RaceType> util = new ExcelUtil<RaceType>(RaceType.class);
        util.exportExcel(response, list, "比赛类型数据");
    }

    /**
     * 获取比赛类型详细信息
     */
    @ApiOperation("获取比赛类型详细信息")
    @PreAuthorize("@ss.hasPermi('buis:raceType:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(raceTypeService.selectRaceTypeById(id));
    }

    /**
     * 新增比赛类型
     */
    @ApiOperation("新增比赛类型")
    @PreAuthorize("@ss.hasPermi('buis:raceType:add')")
    @Log(title = "比赛类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RaceType raceType) {
        return toAjax(raceTypeService.insertRaceType(raceType));
    }

    /**
     * 修改比赛类型
     */
    @ApiOperation("修改比赛类型")
    @PreAuthorize("@ss.hasPermi('buis:raceType:edit')")
    @Log(title = "比赛类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RaceType raceType) {
        return toAjax(raceTypeService.updateRaceType(raceType));
    }

    /**
     * 删除比赛类型
     */
    @ApiOperation("删除比赛类型")
    @PreAuthorize("@ss.hasPermi('buis:raceType:remove')")
    @Log(title = "比赛类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(raceTypeService.deleteRaceTypeByIds(ids));
    }
}
