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
import com.buis.domain.TeamMemberTeacher;
import com.buis.service.ITeamMemberTeacherService;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.common.core.page.TableDataInfo;

/**
 * 队伍老师管理Controller
 *
 * @author buis
 * @date 2024-10-11
 */
@Api(tags = "队伍老师管理Controller")
@RestController
@RequestMapping("/buis/teamTeacher")
public class TeamMemberTeacherController extends BaseController
{
    @Autowired
    private ITeamMemberTeacherService teamMemberTeacherService;

    /**
     * 查询队伍老师管理列表
     */
    @ApiOperation("查询队伍老师管理列表")
    @PreAuthorize("@ss.hasPermi('buis:teamTeacher:list')")
    @GetMapping("/list")
        public TableDataInfo list(TeamMemberTeacher teamMemberTeacher)
        {
            //controller ok

            List<TeamMemberTeacher> list = teamMemberTeacherService.selectTeamMemberTeacherList(teamMemberTeacher);
            return getDataTable(list);
        }









    /**
     * 导出队伍老师管理列表
     */
    @ApiOperation("导出队伍老师管理列表")
    @PreAuthorize("@ss.hasPermi('buis:teamTeacher:export')")
    @Log(title = "队伍老师管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TeamMemberTeacher teamMemberTeacher)
    {
        List<TeamMemberTeacher> list = teamMemberTeacherService.selectTeamMemberTeacherList(teamMemberTeacher);
        ExcelUtil<TeamMemberTeacher> util = new ExcelUtil<TeamMemberTeacher>(TeamMemberTeacher.class);
        util.exportExcel(response, list, "队伍老师管理数据");
    }

    /**
     * 获取队伍老师管理详细信息
     */
    @ApiOperation("获取队伍老师管理详细信息")
    @PreAuthorize("@ss.hasPermi('buis:teamTeacher:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(teamMemberTeacherService.selectTeamMemberTeacherById(id));
    }

    /**
     * 新增队伍老师管理
     */
    @ApiOperation("新增队伍老师管理")
    @PreAuthorize("@ss.hasPermi('buis:teamTeacher:add')")
    @Log(title = "队伍老师管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TeamMemberTeacher teamMemberTeacher)
    {
        return toAjax(teamMemberTeacherService.insertTeamMemberTeacher(teamMemberTeacher));
    }

    /**
     * 修改队伍老师管理
     */
    @ApiOperation("修改队伍老师管理")
    @PreAuthorize("@ss.hasPermi('buis:teamTeacher:edit')")
    @Log(title = "队伍老师管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TeamMemberTeacher teamMemberTeacher)
    {

        return toAjax(teamMemberTeacherService.updateTeamMemberTeacher(teamMemberTeacher));
    }

    /**
     * 删除队伍老师管理
     */
    @ApiOperation("删除队伍老师管理")
    @PreAuthorize("@ss.hasPermi('buis:teamTeacher:remove')")
    @Log(title = "队伍老师管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        //controller ok
        return toAjax(teamMemberTeacherService.deleteTeamMemberTeacherByIds(ids));
    }
}
