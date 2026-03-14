package com.buis.web.controller.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.buis.common.annotation.Log;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.domain.R;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.enums.BusinessType;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.file.zipUtils;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.model.domain.RaceInfo;
import com.buis.model.dto.StudentExportToDiDto;
import com.buis.model.dto.TeacherExportToDaDto;
import com.buis.model.dto.TeacherExportToDiDto;
import com.buis.model.vo.RaceInfoVo;
import com.buis.model.vo.RaceNumsVo;
import com.buis.model.vo.RaceStatCardVo;
import com.buis.service.IRaceInfoService;
import com.buis.service.ITeamInfoService;
import com.buis.service.IUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

/**
 * 比赛信息Controller
 *
 * @author kaven
 * @date 2024-10-23
 */
@Api(tags = "比赛信息Controller")
@RestController
@RequestMapping("/buis/raceInfo")
public class
RaceInfoController extends BaseController {
    @Value("${spring.url}")
    private String serviceUrl;
    @Autowired
    private IRaceInfoService raceInfoService;
    @Autowired
    private ITeamInfoService teamInfoService;
    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 审核教师申报的第二课堂活动
     *
     * @param id
     * @return
     */
//    @ApiOperation("审核第二课堂活动")
//    @PutMapping("/updateApproval/{id}")
////    @PreAuthorize("@ss.hasPermi('buis:raceInfo:updateApproval')")
//    public AjaxResult updateApproval(@ApiParam(name = "id",value = "第二课堂活动id",required = true)
//                                     @PathVariable("id") Long id) {
//        if (id == null){
//            return error("id不能为空");
//        }
//        return success(raceInfoService.updateApproval(id));
//    }

    /**
     * 查询比赛信息列表
     */
    @ApiOperation("查询活动信息列表")
    @PreAuthorize("@ss.hasPermi('buis:raceInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(RaceInfo raceInfo) {
        startPage();
        List<RaceInfoVo> list = raceInfoService.selectRaceInfoList(raceInfo);
        return getDataTable(list);
    }

    /**
     * 导出活动申报材料
     */
    @ApiOperation("导出活动申报材料")
    @PreAuthorize("@ss.hasPermi('buis:raceInfo:declarationDownload')")
    @PostMapping("/exportDeclare")
    public void declarationDownload(@RequestBody Long[] raceIdList, HttpServletResponse response) throws IOException {
        // 接收比赛id列表
        List<RaceInfoVo> raceInfoList = new ArrayList<>();
        for (Long raceId : raceIdList) {
            RaceInfo raceInfo = new RaceInfo();
            raceInfo.setId(raceId);
            raceInfoList.add(raceInfoService.selectRaceInfoById(raceId)); // 获得活动信息
        }

        if (CollectionUtil.isEmpty(raceInfoList)) {
            throw new ServiceException("请选择要导出申报书的活动~");
        }

        List<String> declareUrlList = raceInfoList.stream()
                .map(RaceInfo::getDeclareUrl)// 获得活动申报材料url
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());

        if (declareUrlList.isEmpty()) {
            throw new ServiceException("未找到可下载文件!");
        }

        // 设置响应头（解决中文编码问题）
        String fileName = "申报书_" + System.currentTimeMillis() + ".zip";
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);

        // 生成活动申报材料压缩包
        try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
            for (String relativePath : declareUrlList) {
                // 将相对路径转换为完整URL
                String fullUrl = serviceUrl + relativePath.replace("\\", "/");
                zipUtils.addToZipFileFromUrl(fullUrl, zos);
            }
        } catch (Exception e) {
            throw new ServiceException("压缩文件创建失败: " + e.getMessage());
        }
    }

    /**
     * 导出活动信息列表
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
        return toAjax(raceInfoService.updateBackRaceInfo(raceInfo));
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
    public void exportTeacherDaProjectCount(@RequestParam(value = "college", required = true) int college,
                                            @RequestParam(value = "grade", required = true) int grade,
                                            HttpServletResponse response) {
        List<TeacherExportToDaDto> teacherExporToDektDtoList = new LinkedList<>(); //要导出的类型
        List<TeacherExportToDaDto> list = raceInfoService.exportTeacherDaProjectCount(college, grade, teacherExporToDektDtoList, "大创立项", response);
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
    public void exportTeacherDiProjectCount(@RequestParam(value = "college", required = true) int college,
                                            @RequestParam(value = "grade", required = true) int grade,
                                            HttpServletResponse response) {
        List<TeacherExportToDiDto> teacherExporToDektDtoList = new LinkedList<>(); //要导出的类型
        List<TeacherExportToDiDto> list = raceInfoService.exportTeacherDiProjectCount(college, grade, teacherExporToDektDtoList, "第二课堂", response);
        ExcelUtil<TeacherExportToDiDto> util = new ExcelUtil<TeacherExportToDiDto>(TeacherExportToDiDto.class);
        util.exportExcel(response, list, "老师所指导的第二课堂项目数量数据");
    }

    /**
     * 统计学生参与的第二课堂比赛数量
     */
    @ApiOperation("导出学生参与的第二课堂比赛数量")
    @PreAuthorize("@ss.hasPermi('buis:raceInfo:export')")
    @PostMapping("/exportStudentDiProjectCount")
    public void studentRaceCount(@RequestParam(value = "college", required = true) int college,
                                 @RequestParam(value = "grade", required = true) int grade,
                                 HttpServletResponse response) {
        List<StudentExportToDiDto> studentExportToDiDtoList = new LinkedList<>(); //要导出的类型
        List<StudentExportToDiDto> list = raceInfoService.exportStudentDiProjectCount(college, grade, studentExportToDiDtoList, "第二课堂", response);
        ExcelUtil<StudentExportToDiDto> util = new ExcelUtil<StudentExportToDiDto>(StudentExportToDiDto.class);
        util.exportExcel(response, list, "导出学生参与的第二课堂比赛数量");
    }

    /*
     * 查询总比赛数量
     * */
    @ApiOperation("查询总比赛数量")
    @PreAuthorize("@ss.hasPermi('buis:raceInfo:count')")
    @PostMapping("/count")
    public AjaxResult count() {
        //is_delete = 0才返回
        // 查询数量
        long count = raceInfoService.selectRaceInfoCount();
        return AjaxResult.success(count);
    }

    /*
    * 热门赛事报名排行
    * */
    @ApiOperation("热门赛事报名排行")
    @PostMapping("/getHotRace")
    @PreAuthorize("@ss.hasPermi('buis:raceInfo:hotRace')")
    public AjaxResult getHotRace() {
        List<RaceNumsVo> list = raceInfoService.selectHotRaceWithSignCount();
        return AjaxResult.success(list);
    }

    /*
    * 查询总比赛数量
    * 竞赛总参与人次
    * 竞赛获奖率
    * 新增竞赛项目数
    * */
    @ApiOperation("查询小卡")
    @PostMapping("/getCard")
    @PreAuthorize("@ss.hasPermi('buis:raceInfo:getCard')")
    public AjaxResult getCard() {
        List<RaceStatCardVo> list = raceInfoService.selectCard();
        return AjaxResult.success(list);
    }


}
