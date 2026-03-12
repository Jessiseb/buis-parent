package com.buis.web.controller.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.buis.common.annotation.Log;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.domain.R;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.enums.BusinessType;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.file.zipUtils;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.model.domain.TeamInfo;
import com.buis.model.vo.TeamInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.buis.service.ITeamInfoService;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

/**
 * 队伍信息Controller
 *
 * @author buis
 * @date 2024-10-11
 */
@Slf4j
@Api(tags = "队伍信息Controller")
@RestController
@RequestMapping("/buis/teamInfo")
public class TeamInfoController extends BaseController {
    @Autowired
    private ITeamInfoService teamInfoService;

    /**
     * 查询队伍信息列表
     */
    @ApiOperation("查询队伍信息列表")
    @PreAuthorize("@ss.hasPermi('buis:teamInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(TeamInfo teamInfo) {
        startPage();
        List<TeamInfoVo> list = teamInfoService.selectTeamInfoList(teamInfo);
        return getDataTable(list);
    }

    /**
     * 导出队伍信息列表
     */
    @ApiOperation("导出队伍信息列表")
    @PreAuthorize("@ss.hasPermi('buis:teamInfo:export')")
    @Log(title = "队伍信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TeamInfo teamInfo) {
        List<TeamInfoVo> list = teamInfoService.selectTeamInfoList(teamInfo);
        ExcelUtil<TeamInfoVo> util = new ExcelUtil<TeamInfoVo>(TeamInfoVo.class);
        util.exportExcel(response, list, "项目信息数据");
    }




    /**
     * 导出申报书
     */
    @ApiOperation("导出申报书")
    @PreAuthorize("@ss.hasPermi('buis:teamInfo:exportFile')")
    @Log(title = "队伍信息", businessType = BusinessType.EXPORT)
    @GetMapping("/exportFile")
    public void zipDownload(TeamInfo teamInfo, HttpServletResponse response) throws IOException {
        List<TeamInfoVo> teamInfoList = teamInfoService.selectTeamInfoList(teamInfo);
        //参数校验
        if (CollectionUtil.isEmpty(teamInfoList)) {
            throw new ServiceException("请选择要导出的团队申报书~");
        }
        //获取所有队伍的申报书url
        List<String> declarationUrlList = teamInfoList.stream().map(TeamInfo::getDeclarationUrl)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        //判空
        if (declarationUrlList.isEmpty()) {
            throw new ServiceException("未找到可下载文件!");
        }

        // 设置响应头（关键步骤）
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"申报书_"+System.currentTimeMillis()+".zip\"");

        String zipFile = "申报书_"+System.currentTimeMillis()+".zip";
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (String file : declarationUrlList) {
                zipUtils.addToZipFile(file, zos);
            }
        }

    }

    /**
     * 获取队伍信息详细信息
     */
    @ApiOperation("获取队伍信息详细信息")
    @PreAuthorize("@ss.hasPermi('buis:teamInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(teamInfoService.selectTeamInfoById(id));
    }

    /**
     * 新增队伍信息
     */
    @ApiOperation("新增队伍信息")
    @PreAuthorize("@ss.hasPermi('buis:teamInfo:add')")
    @Log(title = "队伍信息", businessType = BusinessType.INSERT)
    @PostMapping
    public R<TeamInfo> add(@RequestBody TeamInfo teamInfo) {
        return R.ok(teamInfoService.insertTeamInfo(teamInfo));
    }

    /**
     * 修改队伍信息
     */
    @ApiOperation("修改队伍信息")
    @PreAuthorize("@ss.hasPermi('buis:teamInfo:edit')")
    @Log(title = "队伍信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TeamInfo teamInfo) {
        return toAjax(teamInfoService.updateTeamInfoBackend(teamInfo));
    }

    /**
     * 删除队伍信息
     */
    @ApiOperation("删除队伍信息")
    @PreAuthorize("@ss.hasPermi('buis:teamInfo:remove')")
    @Log(title = "队伍信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(teamInfoService.deleteTeamInfoByIds(ids));
    }


}
