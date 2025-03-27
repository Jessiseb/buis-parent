package com.buis.web.controller.manager;

import com.buis.common.annotation.Log;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.enums.BusinessType;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.domain.RaceInfo;
import com.buis.domain.TeamInfo;
import com.buis.dto.StudentExportToDiDto;
import com.buis.dto.TeacherExportToDaDto;
import com.buis.dto.TeacherExportToDiDto;
import com.buis.service.IRaceInfoService;
import com.buis.service.ITeamInfoService;
import com.buis.service.IUserInfoService;
import com.buis.vo.RaceInfoVo;
import com.buis.vo.TeamInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 比赛信息Controller
 *
 * @author kaven
 * @date 2024-10-23
 */
@Api(tags = "比赛信息Controller")
@RestController
@RequestMapping("/buis/raceInfo")
public class RaceInfoController extends BaseController {
    @Autowired
    private IRaceInfoService raceInfoService;
    @Autowired
    private ITeamInfoService teamInfoService;
    @Autowired
    private IUserInfoService userInfoService;


    /**
     * 审核第二课堂活动
     *
     * @param id
     * @return
     */
    @ApiOperation("审核第二课堂活动")
    @PutMapping("/updateApproval/{id}")
//    @PreAuthorize("@ss.hasPermi('buis:raceInfo:updateApproval')")
    public AjaxResult updateApproval(@PathVariable("id") Long id) {
        return success(raceInfoService.updateApproval(id));
    }

    /**
     * 查询比赛信息列表
     */
    @ApiOperation("查询比赛信息列表")
    @PreAuthorize("@ss.hasPermi('buis:raceInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(RaceInfo raceInfo) {
        startPage();
        List<RaceInfoVo> list = raceInfoService.selectRaceInfoList(raceInfo);
        return getDataTable(list);
    }

    /**
     * 导出比赛信息列表
     */
    @ApiOperation("导出比赛信息列表")
    @PreAuthorize("@ss.hasPermi('buis:raceInfo:export')")
    @Log(title = "比赛信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RaceInfo raceInfo) {
        List<RaceInfoVo> list = raceInfoService.selectRaceInfoList(raceInfo);
        ExcelUtil<RaceInfoVo> util = new ExcelUtil<RaceInfoVo>(RaceInfoVo.class);
        util.exportExcel(response, list, "比赛信息数据");
    }

    /**
     * 获取比赛信息详细信息
     */
    @ApiOperation("获取比赛信息详细信息")
    @PreAuthorize("@ss.hasPermi('buis:raceInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(raceInfoService.selectRaceInfoById(id));
    }

    /**
     * 新增比赛信息
     */
    @ApiOperation("新增比赛信息")
    @PreAuthorize("@ss.hasPermi('buis:raceInfo:add')")
    @Log(title = "比赛信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RaceInfo raceInfo) {
        return toAjax(raceInfoService.insertRaceInfo(raceInfo));
    }

    /**
     * 修改比赛信息
     */
    @ApiOperation("修改比赛信息")
    @PreAuthorize("@ss.hasPermi('buis:raceInfo:edit')")
    @Log(title = "比赛信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RaceInfo raceInfo) {
        return toAjax(raceInfoService.updateRaceInfo(raceInfo));
    }

    /**
     * 删除比赛信息
     */
    @ApiOperation("删除比赛信息")
    @PreAuthorize("@ss.hasPermi('buis:raceInfo:remove')")
    @Log(title = "比赛信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(raceInfoService.deleteRaceInfoByIds(ids));
    }

    /**
     * 导出老师所指导的大创立项项目数量
     * (该接口只会导出有指导第大创立项项目的老师的信息)
     */
    @ApiOperation("导出老师所指导的大创立项项目数量")
    @PreAuthorize("@ss.hasPermi('buis:raceInfo:export')")
    @PostMapping("/exportTeacherDaProjectCount")
    public void exportTeacherDaProjectCount(@RequestParam(value = "college", required = false, defaultValue = "1") int college,
                                            @RequestParam(value = "grade", required = false, defaultValue = "2025") int grade,
                                            HttpServletResponse response) {
        List<TeacherExportToDaDto> teacherExporToDektDtoList = new LinkedList<>(); //要导出的类型
        List<TeacherExportToDaDto> list = raceInfoService.exportTeacherDaProjectCount(college,grade,teacherExporToDektDtoList, "大创立项", response);
        ExcelUtil<TeacherExportToDaDto> util = new ExcelUtil<TeacherExportToDaDto>(TeacherExportToDaDto.class);
        util.exportExcel(response, list, "老师所指导的大创项目数量数据");
    }

    /**
     * 导出大数据学院老师所负责的第二课堂项目数量
     * (该接口只会导出有指导第大创立项项目的老师的信息)
     */
    @ApiOperation("导出大数据学院老师所负责的第二课堂项目数量")
    @PreAuthorize("@ss.hasPermi('buis:raceInfo:export')")
    @PostMapping("/exportTeacherDiProjectCount")
    public void exportTeacherDiProjectCount(@RequestParam(value = "college", required = false, defaultValue = "1") int college,
                                            @RequestParam(value = "grade", required = false, defaultValue = "2025") int grade,
                                            HttpServletResponse response) {
        List<TeacherExportToDiDto> teacherExporToDektDtoList = new LinkedList<>(); //要导出的类型
        List<TeacherExportToDiDto> list = raceInfoService.exportTeacherDiProjectCount(college,grade,teacherExporToDektDtoList, "第二课堂", response);
        ExcelUtil<TeacherExportToDiDto> util = new ExcelUtil<TeacherExportToDiDto>(TeacherExportToDiDto.class);
        util.exportExcel(response, list, "老师所指导的第二课堂项目数量数据");
    }

    /**
     * 统计学生参与的第二课堂比赛数量
     */
    @ApiOperation("导出学生参与的第二课堂比赛数量")
    @PreAuthorize("@ss.hasPermi('buis:raceInfo:export')")
    @GetMapping("/exportStudentDiProjectCount")
    public void studentRaceCount(HttpServletResponse response) {
        List<StudentExportToDiDto> studentExportToDiDtoList = new LinkedList<>(); //要导出的类型
        List<StudentExportToDiDto> list = raceInfoService.exportStudentDiProjectCount(studentExportToDiDtoList, "第二课堂", response);
        ExcelUtil<StudentExportToDiDto> util = new ExcelUtil<StudentExportToDiDto>(StudentExportToDiDto.class);
        util.exportExcel(response, list, "导出学生参与的第二课堂比赛数量");
    }
}
