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
import com.buis.domain.CollegeMajor;
import com.buis.service.ICollegeMajorService;
import com.buis.common.utils.poi.ExcelUtil;

/**
 * 专业信息Controller
 *
 * @author buis
 * @date 2024-10-10
 */
@Api(tags = "专业信息Controller")
@RestController
@RequestMapping("/buis/major")
public class CollegeMajorController extends BaseController
{
    @Autowired
    private ICollegeMajorService collegeMajorService;

/**
 * 查询专业信息列表
 */
@ApiOperation("查询专业信息列表")
@PreAuthorize("@ss.hasPermi('buis:major:list')")
@GetMapping("/list")
        public AjaxResult list(CollegeMajor collegeMajor)
        {
            List<CollegeMajor> list = collegeMajorService.selectCollegeMajorList(collegeMajor);
            return success(list);
        }

    /**
     * 导出专业信息列表
     */
    @ApiOperation("导出专业信息列表")
    @PreAuthorize("@ss.hasPermi('buis:major:export')")
    @Log(title = "专业信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CollegeMajor collegeMajor)
    {
        List<CollegeMajor> list = collegeMajorService.selectCollegeMajorList(collegeMajor);
        ExcelUtil<CollegeMajor> util = new ExcelUtil<CollegeMajor>(CollegeMajor.class);
        util.exportExcel(response, list, "专业信息数据");
    }

    /**
     * 获取专业信息详细信息
     */
    @ApiOperation("获取专业信息详细信息")
    @PreAuthorize("@ss.hasPermi('buis:major:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(collegeMajorService.selectCollegeMajorById(id));
    }

    /**
     * 新增专业信息
     */
    @ApiOperation("新增专业信息")
    @PreAuthorize("@ss.hasPermi('buis:major:add')")
    @Log(title = "专业信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CollegeMajor collegeMajor)
    {
        return toAjax(collegeMajorService.insertCollegeMajor(collegeMajor));
    }

    /**
     * 修改专业信息
     */
    @ApiOperation("修改专业信息")
    @PreAuthorize("@ss.hasPermi('buis:major:edit')")
    @Log(title = "专业信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CollegeMajor collegeMajor)
    {
        return toAjax(collegeMajorService.updateCollegeMajor(collegeMajor));
    }

    /**
     * 删除专业信息
     */
    @ApiOperation("删除专业信息")
    @PreAuthorize("@ss.hasPermi('buis:major:remove')")
    @Log(title = "专业信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(collegeMajorService.deleteCollegeMajorByIds(ids));
    }
}
