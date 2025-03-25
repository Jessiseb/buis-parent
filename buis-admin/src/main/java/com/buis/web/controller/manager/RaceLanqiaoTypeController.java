package com.buis.web.controller.manager;

import com.buis.common.annotation.Log;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.enums.BusinessType;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.domain.RaceLanqiaoType;
import com.buis.service.IRaceLanqiaoTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 蓝桥杯赛道类型Controller
 *
 * @author buis
 * @date 2024-10-23
 */
@Api(tags = "蓝桥杯赛道类型Controller")
@RestController
@RequestMapping("/buis/lanQiaoType")
public class RaceLanqiaoTypeController extends BaseController {
    @Autowired
    private IRaceLanqiaoTypeService raceLanqiaoTypeService;

    /**
     * 查询蓝桥杯赛道类型列表
     */
    @ApiOperation("查询蓝桥杯赛道类型列表")
    @PreAuthorize("@ss.hasPermi('buis:lanQiaoType:list')")
    @GetMapping("/list")
    public AjaxResult list(RaceLanqiaoType raceLanqiaoType) {
        List<RaceLanqiaoType> list = raceLanqiaoTypeService.selectRaceLanqiaoTypeList(raceLanqiaoType);
        return success(list);
    }

    /**
     * 导出蓝桥杯赛道类型列表
     */
    @ApiOperation("导出蓝桥杯赛道类型列表")
    @PreAuthorize("@ss.hasPermi('buis:lanQiaoType:export')")
    @Log(title = "蓝桥杯赛道类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RaceLanqiaoType raceLanqiaoType) {
        List<RaceLanqiaoType> list = raceLanqiaoTypeService.selectRaceLanqiaoTypeList(raceLanqiaoType);
        ExcelUtil<RaceLanqiaoType> util = new ExcelUtil<RaceLanqiaoType>(RaceLanqiaoType.class);
        util.exportExcel(response, list, "蓝桥杯赛道类型数据");
    }

    /**
     * 获取蓝桥杯赛道类型详细信息
     */
    @ApiOperation("获取蓝桥杯赛道类型详细信息")
    @PreAuthorize("@ss.hasPermi('buis:lanQiaoType:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(raceLanqiaoTypeService.selectRaceLanqiaoTypeById(id));
    }

    /**
     * 新增蓝桥杯赛道类型
     */
    @ApiOperation("新增蓝桥杯赛道类型")
    @PreAuthorize("@ss.hasPermi('buis:lanQiaoType:add')")
    @Log(title = "蓝桥杯赛道类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RaceLanqiaoType raceLanqiaoType)
    {
        return toAjax(raceLanqiaoTypeService.insertRaceLanqiaoType(raceLanqiaoType));
    }

    /**
     * 修改蓝桥杯赛道类型
     */
    @ApiOperation("修改蓝桥杯赛道类型")
    @PreAuthorize("@ss.hasPermi('buis:lanQiaoType:edit')")
    @Log(title = "蓝桥杯赛道类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RaceLanqiaoType raceLanqiaoType)
    {
        return toAjax(raceLanqiaoTypeService.updateRaceLanqiaoType(raceLanqiaoType));
    }

    /**
     * 删除蓝桥杯赛道类型
     */
    @ApiOperation("删除蓝桥杯赛道类型")
    @PreAuthorize("@ss.hasPermi('buis:lanQiaoType:remove')")
    @Log(title = "蓝桥杯赛道类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(raceLanqiaoTypeService.deleteRaceLanqiaoTypeByIds(ids));
    }


}
