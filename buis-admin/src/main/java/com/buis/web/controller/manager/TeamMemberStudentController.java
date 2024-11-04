package com.buis.web.controller.manager;

import com.buis.common.annotation.Log;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.enums.BusinessType;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.domain.TeamMemberStudent;
import com.buis.service.ITeamMemberStudentService;
import com.buis.vo.TeamMemberStudentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 队伍成员管理Controller
 *
 * @author buis
 * @date 2024-10-11
 */
@Api(tags = "队伍成员管理Controller")
@RestController
@RequestMapping("/buis/teamStudent")
public class TeamMemberStudentController extends BaseController {
    @Autowired
    private ITeamMemberStudentService teamMemberStudentService;


    /**
     * 查询队伍成员管理列表
     */
    @ApiOperation("查询队伍成员管理列表")
    @PreAuthorize("@ss.hasPermi('buis:teamStudent:list')")
    @GetMapping("/list")
    public TableDataInfo list(TeamMemberStudentVo teamMemberStudent) {
        startPage();
        List<TeamMemberStudentVo> list = teamMemberStudentService.selectTeamMemberStudentList(teamMemberStudent);
        return getDataTable(list);
    }


    /**
     * 导出队伍成员管理列表
     */
    @ApiOperation("导出队伍成员管理列表")
    @PreAuthorize("@ss.hasPermi('buis:teamStudent:export')")
    @Log(title = "队伍成员管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TeamMemberStudentVo teamMemberStudent) {
        List<TeamMemberStudentVo> list = teamMemberStudentService.selectTeamMemberStudentList(teamMemberStudent);
        ExcelUtil<TeamMemberStudentVo> util = new ExcelUtil<TeamMemberStudentVo>(TeamMemberStudentVo.class);
        util.exportExcel(response, list, "队伍成员管理数据");
    }

    /**
     * 获取队伍成员管理详细信息
     */
    @ApiOperation("获取队伍成员管理详细信息")
    @PreAuthorize("@ss.hasPermi('buis:teamStudent:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(teamMemberStudentService.selectTeamMemberStudentById(id));
    }

    /**
     * 新增队伍成员管理
     */
    @ApiOperation("新增队伍成员管理")
    @PreAuthorize("@ss.hasPermi('buis:teamStudent:add')")
    @Log(title = "队伍成员管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TeamMemberStudent teamMemberStudent) {
        return toAjax(teamMemberStudentService.insertTeamMemberStudent(teamMemberStudent));
    }

    /**
     * 修改队伍成员管理
     */
    @ApiOperation("修改队伍成员管理")
    @PreAuthorize("@ss.hasPermi('buis:teamStudent:edit')")
    @Log(title = "队伍成员管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TeamMemberStudent teamMemberStudent) {
        return toAjax(teamMemberStudentService.updateTeamMemberStudent(teamMemberStudent));
    }

    /**
     * 删除队伍成员管理
     */
    @ApiOperation("删除队伍成员管理")
    @PreAuthorize("@ss.hasPermi('buis:teamStudent:remove')")
    @Log(title = "队伍成员管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(teamMemberStudentService.deleteTeamMemberStudentByIds(ids));
    }
}
