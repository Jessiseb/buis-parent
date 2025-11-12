package com.buis.web.controller.manager;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.buis.model.dto.TeamMemberTeacherDto;
import com.buis.model.vo.TeamMemberTeacherVo;
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
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.enums.BusinessType;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.model.domain.TeamMemberTeacher;
import com.buis.service.ITeamMemberTeacherService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 队伍老师管理Controller
 *
 * @author buis
 * @date 2024-10-11
 */
@Api(tags = "队伍老师管理Controller")
@RestController
@RequestMapping("/buis/teamTeacher")
public class TeamMemberTeacherController extends BaseController {
    @Autowired
    private ITeamMemberTeacherService teamMemberTeacherService;

    /**
     * 查询队伍老师管理列表
     */
    @ApiOperation("查询队伍老师管理列表")
    @PreAuthorize("@ss.hasPermi('buis:teamTeacher:list')")
    @GetMapping("/list")
    public TableDataInfo list(TeamMemberTeacher teamMemberTeacher) {
        startPage();
        List<TeamMemberTeacherVo> list = teamMemberTeacherService.selectTeamMemberTeacherList(teamMemberTeacher);
        return getDataTable(list);
    }

    /**
     * 导出队伍老师管理列表
     */
    @ApiOperation("导出队伍老师管理列表")
    @PreAuthorize("@ss.hasPermi('buis:teamTeacher:export')")
    @Log(title = "队伍老师管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TeamMemberTeacherVo teamMemberTeacher) {
        List<TeamMemberTeacherVo> list = teamMemberTeacherService.selectTeamMemberTeacherList(teamMemberTeacher);
        ExcelUtil<TeamMemberTeacherVo> util = new ExcelUtil<TeamMemberTeacherVo>(TeamMemberTeacherVo.class);
        util.exportExcel(response, list, "队伍老师管理数据");
    }
    @ApiOperation("导出队伍老师管理列表模板")
    @Log(title = "队伍老师管理", businessType = BusinessType.EXPORT)
    @PostMapping("/exportTemplate")
    @PreAuthorize("@ss.hasPermi('buis:teamTeacher:export')")
    public void exportHeader(HttpServletResponse response) throws IOException {
        ExcelUtil<TeamMemberTeacherVo> util = new ExcelUtil<>(TeamMemberTeacherVo.class);
        util.exportExcelHeaderOnly(response, "队伍老师管理数据");
    }
    /**
     * 导入队伍老师管理列表
     */
     @ApiOperation("导入队伍老师管理列表")
     @PreAuthorize("@ss.hasPermi('buis:teamTeacher:add')")
     @Log(title = "队伍老师管理", businessType = BusinessType.IMPORT)
     @PostMapping("/import")
     public AjaxResult excelImport(MultipartFile file) throws Exception {
         ExcelUtil<TeamMemberTeacherVo> util = new ExcelUtil<TeamMemberTeacherVo>(TeamMemberTeacherVo.class);
         List<TeamMemberTeacherVo> TeamMemberTeacherList = util.importExcel(file.getInputStream());
         return toAjax(teamMemberTeacherService.insertTeamMemberTeacherBatch(TeamMemberTeacherList));
     }
    /**
     * 获取队伍老师管理详细信息
     */
    @ApiOperation("获取队伍老师管理详细信息")
    @PreAuthorize("@ss.hasPermi('buis:teamTeacher:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(teamMemberTeacherService.selectTeamMemberTeacherById(id));
    }

    /**
     * 新增队伍老师管理
     */
    @ApiOperation("新增队伍老师管理")
    @PreAuthorize("@ss.hasPermi('buis:teamTeacher:add')")
    @Log(title = "队伍老师管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TeamMemberTeacher teamMemberTeacher) {
        return toAjax(teamMemberTeacherService.insertTeamMemberTeacher(teamMemberTeacher));
    }

    /**
     * 批量新增/删除队伍老师管理
     */
    @ApiOperation("批量新增/删除队伍老师管理")
    @PreAuthorize("@ss.hasPermi('buis:teamTeacher:Add')")
    @Log(title = "队伍老师管理", businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    public AjaxResult batchAdd(@RequestBody TeamMemberTeacherDto dto) {
        return toAjax(teamMemberTeacherService.batchAddTeamMemberTeacher(dto));
    }

    /**
     * 修改队伍老师管理
     */
    @ApiOperation("修改队伍老师管理")
    @PreAuthorize("@ss.hasPermi('buis:teamTeacher:edit')")
    @Log(title = "队伍老师管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TeamMemberTeacher teamMemberTeacher) {
        return toAjax(teamMemberTeacherService.updateTeamMemberTeacher(teamMemberTeacher));
    }

    /**
     * 删除队伍老师管理
     */
    @ApiOperation("删除队伍老师管理")
    @PreAuthorize("@ss.hasPermi('buis:teamTeacher:remove')")
    @Log(title = "队伍老师管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(teamMemberTeacherService.deleteTeamMemberTeacherByIds(ids));
    }
}
