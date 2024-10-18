package com.buis.web.controller.manager;

import com.buis.common.annotation.Log;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.enums.BusinessType;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.domain.Judge;
import com.buis.service.IJudgeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 评委管理Controller
 *
 * @author buis
 * @date 2024-10-10
 */
@Api(tags = "评委管理Controller")
@RestController
@RequestMapping("/buis/judge")
public class JudgeController extends BaseController {
    @Autowired
    private IJudgeService judgeService;

    /**
     * 查询评委管理列表
     */
    @ApiOperation("查询评委管理列表")
    @PreAuthorize("@ss.hasPermi('buis:judge:list')")
    @GetMapping("/list")
    public TableDataInfo list(Judge judge) {
        startPage();
        List<Judge> list = judgeService.selectJudgeList(judge);
        return getDataTable(list);
    }

    /**
     * 导出评委管理列表
     */
    @ApiOperation("导出评委管理列表")
    @PreAuthorize("@ss.hasPermi('buis:judge:export')")
    @Log(title = "评委管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Judge judge)
    {
        List<Judge> list = judgeService.selectJudgeList(judge);
        ExcelUtil<Judge> util = new ExcelUtil<Judge>(Judge.class);
        util.exportExcel(response, list, "评委管理数据");
    }

    /**
     * 获取评委管理详细信息
     */
    @ApiOperation("获取评委管理详细信息")
    @PreAuthorize("@ss.hasPermi('buis:judge:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(judgeService.selectJudgeById(id));
    }

    /**
     * 新增评委管理
     */
    @ApiOperation("新增评委管理")
    @PreAuthorize("@ss.hasPermi('buis:judge:add')")
    @Log(title = "评委管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Judge judge)
    {
        return toAjax(judgeService.insertJudge(judge));
    }

    /**
     * 修改评委管理
     */
    @ApiOperation("修改评委管理")
    @PreAuthorize("@ss.hasPermi('buis:judge:edit')")
    @Log(title = "评委管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Judge judge)
    {
        return toAjax(judgeService.updateJudge(judge));
    }

    /**
     * 删除评委管理
     */
    @ApiOperation("删除评委管理")
    @PreAuthorize("@ss.hasPermi('buis:judge:remove')")
    @Log(title = "评委管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(judgeService.deleteJudgeByIds(ids));
    }
}
