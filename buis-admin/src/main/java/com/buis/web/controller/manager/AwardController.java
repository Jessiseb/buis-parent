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
import com.buis.domain.Award;
import com.buis.service.IAwardService;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.common.core.page.TableDataInfo;

/**
 * 奖状信息Controller
 *
 * @author zjx
 * @date 2025-03-16
 */
@Api(tags = "奖状信息Controller")
@RestController
@RequestMapping("/buis/award")
public class AwardController extends BaseController
{
    @Autowired
    private IAwardService awardService;

/**
 * 查询奖状信息列表
 */
@ApiOperation("查询奖状信息列表")
@PreAuthorize("@ss.hasPermi('buis:award:list')")
@GetMapping("/list")
    public TableDataInfo list(Award award)
    {
        startPage();
        List<Award> list = awardService.selectAwardList(award);
        return getDataTable(list);
    }

    /**
     * 导出奖状信息列表
     */
    @ApiOperation("导出奖状信息列表")
    @PreAuthorize("@ss.hasPermi('buis:award:export')")
    @Log(title = "奖状信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Award award)
    {
        List<Award> list = awardService.selectAwardList(award);
        ExcelUtil<Award> util = new ExcelUtil<Award>(Award.class);
        util.exportExcel(response, list, "奖状信息数据");
    }

    /**
     * 获取奖状信息详细信息
     */
    @ApiOperation("获取奖状信息详细信息")
    @PreAuthorize("@ss.hasPermi('buis:award:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(awardService.selectAwardById(id));
    }

    /**
     * 新增奖状信息
     */
    @ApiOperation("新增奖状信息")
    @PreAuthorize("@ss.hasPermi('buis:award:add')")
    @Log(title = "奖状信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Award award)
    {
        return toAjax(awardService.insertAward(award));
    }

    /**
     * 修改奖状信息
     */
    @ApiOperation("修改奖状信息")
    @PreAuthorize("@ss.hasPermi('buis:award:edit')")
    @Log(title = "奖状信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Award award)
    {
        return toAjax(awardService.updateAward(award));
    }

    /**
     * 删除奖状信息
     */
    @ApiOperation("删除奖状信息")
    @PreAuthorize("@ss.hasPermi('buis:award:remove')")
    @Log(title = "奖状信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(awardService.deleteAwardByIds(ids));
    }
}
