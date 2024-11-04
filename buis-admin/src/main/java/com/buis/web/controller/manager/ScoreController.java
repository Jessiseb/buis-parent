package com.buis.web.controller.manager;

import com.buis.common.annotation.Log;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.enums.BusinessType;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.domain.Score;
import com.buis.service.IScoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 评分管理Controller
 *
 * @author buis
 * @date 2024-10-11
 */
@Api(tags = "评分管理Controller")
@RestController
@RequestMapping("/buis/score")
public class ScoreController extends BaseController {
    @Autowired
    private IScoreService scoreService;

    /**
     * 查询评分管理列表
     */
    @ApiOperation("查询评分管理列表")
    @PreAuthorize("@ss.hasPermi('buis:score:list')")
    @GetMapping("/list")
    public TableDataInfo list(Score score) {
        startPage();
        List<Score> list = scoreService.selectScoreList(score);
        return getDataTable(list);
    }

    /**
     * 导出评分管理列表
     */
    @ApiOperation("导出评分管理列表")
    @PreAuthorize("@ss.hasPermi('buis:score:export')")
    @Log(title = "评分管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Score score) {
        List<Score> list = scoreService.selectScoreList(score);
        ExcelUtil<Score> util = new ExcelUtil<Score>(Score.class);
        util.exportExcel(response, list, "评分管理数据");
    }

    /**
     * 获取评分管理详细信息
     */
    @ApiOperation("获取评分管理详细信息")
    @PreAuthorize("@ss.hasPermi('buis:score:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(scoreService.selectScoreById(id));
    }

    /**
     * 新增评分管理
     */
    @ApiOperation("新增评分管理")
    @PreAuthorize("@ss.hasPermi('buis:score:add')")
    @Log(title = "评分管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Score score) {
        return toAjax(scoreService.insertScore(score));
    }

    /**
     * 修改评分管理
     */
    @ApiOperation("修改评分管理")
    @PreAuthorize("@ss.hasPermi('buis:score:edit')")
    @Log(title = "评分管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Score score) {
        return toAjax(scoreService.updateScore(score));
    }

    /**
     * 删除评分管理
     */
    @ApiOperation("删除评分管理")
    @PreAuthorize("@ss.hasPermi('buis:score:remove')")
    @Log(title = "评分管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(scoreService.deleteScoreByIds(ids));
    }
}
