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
import com.buis.domain.TeamInfo;
import com.buis.service.ITeamInfoService;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.common.core.page.TableDataInfo;

/**
 * 队伍信息Controller
 *
 * @author buis
 * @date 2024-10-11
 */
@Api(tags = "队伍信息Controller")
@RestController
@RequestMapping("/buis/teamInfo")
public class TeamInfoController extends BaseController
{
    @Autowired
    private ITeamInfoService teamInfoService;

/**
 * 查询队伍信息列表
 */
@ApiOperation("查询队伍信息列表")
@PreAuthorize("@ss.hasPermi('buis:teamInfo:list')")
@GetMapping("/list")
    public TableDataInfo list(TeamInfo teamInfo)
    {
        startPage();
        List<TeamInfo> list = teamInfoService.selectTeamInfoList(teamInfo);
        return getDataTable(list);
    }

    /**
     * 导出队伍信息列表
     */
    @ApiOperation("导出队伍信息列表")
    @PreAuthorize("@ss.hasPermi('buis:teamInfo:export')")
    @Log(title = "队伍信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TeamInfo teamInfo)
    {
        List<TeamInfo> list = teamInfoService.selectTeamInfoList(teamInfo);
        ExcelUtil<TeamInfo> util = new ExcelUtil<TeamInfo>(TeamInfo.class);
        util.exportExcel(response, list, "队伍信息数据");
    }

    /**
     * 获取队伍信息详细信息
     */
    @ApiOperation("获取队伍信息详细信息")
    @PreAuthorize("@ss.hasPermi('buis:teamInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(teamInfoService.selectTeamInfoById(id));
    }

    /**
     * 新增队伍信息
     */
    @ApiOperation("新增队伍信息")
    @PreAuthorize("@ss.hasPermi('buis:teamInfo:add')")
    @Log(title = "队伍信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TeamInfo teamInfo)
    {
        return toAjax(teamInfoService.insertTeamInfo(teamInfo));
    }

    /**
     * 修改队伍信息
     */
    @ApiOperation("修改队伍信息")
    @PreAuthorize("@ss.hasPermi('buis:teamInfo:edit')")
    @Log(title = "队伍信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TeamInfo teamInfo)
    {
        return toAjax(teamInfoService.updateTeamInfo(teamInfo));
    }

    /**
     * 删除队伍信息
     */
    @ApiOperation("删除队伍信息")
    @PreAuthorize("@ss.hasPermi('buis:teamInfo:remove')")
    @Log(title = "队伍信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(teamInfoService.deleteTeamInfoByIds(ids));
    }
}
