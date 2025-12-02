package com.buis.web.controller.manager;

import com.buis.common.annotation.Log;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.domain.R;
import com.buis.common.core.page.MyPageVo;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.enums.BusinessType;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.model.domain.Assistant;
import com.buis.model.dto.UserSearchRequest;
import com.buis.model.vo.AssistantVo;
import com.buis.model.vo.UserInfoVo;
import com.buis.service.IAssistantService;
import com.buis.service.IUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * 学生助理Controller
 *
 * @author buis
 * @date 2024-10-11
 */
@Api(tags = "学生助理Controller")
@RestController
@RequestMapping("/buis/assistant")
public class AssistantController extends BaseController {
    @Autowired
    private IAssistantService assistantService;
    @Resource
    private IUserInfoService userInfoService;

    /**
     * 查询学生助理--列表
     */
    @ApiOperation("查询学生助理列表")
    @PreAuthorize("@ss.hasPermi('buis:assistant:list')")
    @GetMapping("/list")
    public TableDataInfo list(Assistant assistant) {
        startPage();
        List<AssistantVo> list = assistantService.selectAssistantList(assistant);
        return getDataTable(list);
    }

    /**
     * 分页查询学生助理列表
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return 当前页学生助理列表
     */
    @ApiOperation("分页查询所有学生助理")
    @PreAuthorize("@ss.hasPermi('buis:assistant:list')")
    @PostMapping("/getAssistantsByPage")
    public MyPageVo<List<UserInfoVo>> getAssistants(
            @RequestBody UserSearchRequest userSearchRequest,
            @ApiParam(value = "页码", required = true, example = "1") @RequestParam Integer pageNum,
            @ApiParam(value = "每页条数", required = true, example = "10") @RequestParam Integer pageSize) {
        startPage();
        List<UserInfoVo> assistants = userInfoService.queryAssistantsList(userSearchRequest);
        if (assistants.isEmpty()) {
            return new MyPageVo(Collections.emptyList());
        }
        return new MyPageVo(assistants);
    }

    /**
     * 设置学生助理
     * @param userId      用户ID
     * @param isAssistant 目标状态（0-否 1-是）
     * @return 操作结果
     */
    @PostMapping("/setAssistant")
    @ApiOperation("设置学生助理")
    @PreAuthorize("@ss.hasPermi('buis:assistant:edit')")
    public R setAssistantStatus(
            @ApiParam(value = "User_id", required = true) @RequestParam Long userId,
            @ApiParam(value = "是否为助理（0-否 1-是）", required = true, example = "1") @RequestParam Integer isAssistant) {

        return userInfoService.updateAssistantStatus(userId, isAssistant);

    }

    /**
     * 联表模糊查询学生助理列表
     */
    @ApiOperation("联表模糊查询学生助理列表")
    @PreAuthorize("@ss.hasPermi('buis:assistant:fuzzy')")
    @GetMapping("/fuzzy")
    public TableDataInfo fuzzyQuery(String name) {
        startPage();
        List<AssistantVo> list = assistantService.fuzzyQuery(name);
        return getDataTable(list);
    }

    /**
     * 导出学生助理列表
     */
    @ApiOperation("导出学生助理列表")
    @PreAuthorize("@ss.hasPermi('buis:assistant:export')")
    @Log(title = "学生助理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Assistant assistant) {
        List<AssistantVo> list = assistantService.selectAssistantList(assistant);
        ExcelUtil<AssistantVo> util = new ExcelUtil<AssistantVo>(AssistantVo.class);
        util.exportExcel(response, list, "学生助理数据");
    }

    /**
     * 获取学生助理详细信息
     */
    @ApiOperation("获取学生助理详细信息")
    @PreAuthorize("@ss.hasPermi('buis:assistant:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(assistantService.selectAssistantById(id));
    }

    /**
     * 新增学生助理
     */
    @ApiOperation("新增学生助理")
    @PreAuthorize("@ss.hasPermi('buis:assistant:add')")
    @Log(title = "学生助理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Assistant assistant) {
        return toAjax(assistantService.insertAssistant(assistant));
    }

    /**
     * 修改学生助理
     */
    @ApiOperation("修改学生助理")
    @PreAuthorize("@ss.hasPermi('buis:assistant:edit')")
    @Log(title = "学生助理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Assistant assistant) {
        return toAjax(assistantService.updateAssistant(assistant));
    }

    /**
     * 删除学生助理
     */
    @ApiOperation("删除学生助理")
    @PreAuthorize("@ss.hasPermi('buis:assistant:remove')")
    @Log(title = "学生助理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(assistantService.deleteAssistantByIds(ids));
    }
}
