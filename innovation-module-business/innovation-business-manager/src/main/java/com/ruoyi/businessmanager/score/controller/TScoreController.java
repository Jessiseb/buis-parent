package com.ruoyi.businessmanager.score.controller;

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
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.businessmanager.score.domain.entities.TScore;
import com.ruoyi.businessmanager.score.service.ITScoreService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 评委评分功能Controller
 * 
 * @author zjx
 * @date 2024-10-01
 */
@Api(tags = "评委评分功能")
@RestController
@RequestMapping("/businessmanager/score")
public class TScoreController extends BaseController
{
    @Autowired
    private ITScoreService tScoreService;

    /**
     * 查询评委评分功能列表
     */
    @ApiOperation("查询评委评分功能列表")
    @PreAuthorize("@ss.hasPermi('businessmanager:score:list')")
    @GetMapping("/list")
    public TableDataInfo list(TScore tScore)
    {
        startPage();
        List<TScore> list = tScoreService.selectTScoreList(tScore);
        return getDataTable(list);
    }

    /**
     * 导出评委评分功能列表
     */
    @ApiOperation("导出评委评分功能列表")
    @PreAuthorize("@ss.hasPermi('businessmanager:score:export')")
    @Log(title = "评委评分功能", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TScore tScore)
    {
        List<TScore> list = tScoreService.selectTScoreList(tScore);
        ExcelUtil<TScore> util = new ExcelUtil<TScore>(TScore.class);
        util.exportExcel(response, list, "评委评分功能数据");
    }

    /**
     * 获取评委评分功能详细信息
     */
    @ApiOperation("获取评委评分功能详细信息")
    @PreAuthorize("@ss.hasPermi('businessmanager:score:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tScoreService.selectTScoreById(id));
    }

    /**
     * 新增评委评分功能
     */
    @ApiOperation("新增评委评分功能")
    @PreAuthorize("@ss.hasPermi('businessmanager:score:add')")
    @Log(title = "评委评分功能", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TScore tScore)
    {
        return toAjax(tScoreService.insertTScore(tScore));
    }

    /**
     * 修改评委评分功能
     */
    @ApiOperation("修改评委评分功能")
    @PreAuthorize("@ss.hasPermi('businessmanager:score:edit')")
    @Log(title = "评委评分功能", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TScore tScore)
    {
        return toAjax(tScoreService.updateTScore(tScore));
    }

    /**
     * 删除评委评分功能
     */
    @ApiOperation("删除评委评分功能")
    @PreAuthorize("@ss.hasPermi('businessmanager:score:remove')")
    @Log(title = "评委评分功能", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tScoreService.deleteTScoreByIds(ids));
    }
}
