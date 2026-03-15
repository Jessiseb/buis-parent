package com.buis.web.controller.manager;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.buis.model.domain.SkillTag;
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
import com.buis.service.ISkillTagService;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.common.core.page.TableDataInfo;

/**
 * 技能标签主Controller
 *
 * @author buis
 * @date 2026-03-11
 */
@Api(tags = "技能标签主Controller")
@RestController
@RequestMapping("/buis/skillTag")
public class SkillTagController extends BaseController {
    @Autowired
    private ISkillTagService skillTagService;

    /**
     * 查询技能标签主列表
     */
    @ApiOperation("查询技能标签主列表")
    @PreAuthorize("@ss.hasPermi('buis:skillTag:list')")
    @GetMapping("/list")
    public TableDataInfo list(SkillTag skillTag) {
        startPage();
        List<SkillTag> list = skillTagService.selectSkillTagList(skillTag);
        return getDataTable(list);
    }

    /**
     * 导出技能标签主列表
     */
    @ApiOperation("导出技能标签主列表")
    @PreAuthorize("@ss.hasPermi('buis:skillTag:export')")
    @Log(title = "技能标签主", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SkillTag skillTag) {
        List<SkillTag> list = skillTagService.selectSkillTagList(skillTag);
        ExcelUtil<SkillTag> util = new ExcelUtil<SkillTag>(SkillTag.class);
        util.exportExcel(response, list, "技能标签主数据");
    }

    /**
     * 获取技能标签主详细信息
     */
    @ApiOperation("获取技能标签主详细信息")
    @PreAuthorize("@ss.hasPermi('buis:skillTag:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(skillTagService.selectSkillTagById(id));
    }

    /**
     * 新增技能标签主
     */
    @ApiOperation("新增技能标签主")
    @PreAuthorize("@ss.hasPermi('buis:skillTag:add')")
    @Log(title = "技能标签主", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SkillTag skillTag) {
        return toAjax(skillTagService.insertSkillTag(skillTag));
    }

    /**
     * 修改技能标签主
     */
    @ApiOperation("修改技能标签主")
    @PreAuthorize("@ss.hasPermi('buis:skillTag:edit')")
    @Log(title = "技能标签主", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SkillTag skillTag) {
        return toAjax(skillTagService.updateSkillTag(skillTag));
    }

    /**
     * 删除技能标签主
     */
    @ApiOperation("删除技能标签主")
    @PreAuthorize("@ss.hasPermi('buis:skillTag:remove')")
    @Log(title = "技能标签主", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(skillTagService.deleteSkillTagByIds(ids));
    }


    /*
    * 获取学生掌握的技能
    * */
    @ApiOperation("获取学生掌握的技能")
    @PostMapping("/getSkillTagByAllUser")
    @PreAuthorize("@ss.hasPermi('buis:skillTag:getSkillTagByAllUser')")
    public AjaxResult getSkillTagByAllUser() {
        return success(skillTagService.getSkillTagByAllUser());
    }

}
