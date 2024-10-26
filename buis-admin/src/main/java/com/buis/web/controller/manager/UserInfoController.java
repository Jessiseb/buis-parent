package com.buis.web.controller.manager;

import com.buis.common.annotation.Log;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.enums.BusinessType;
import com.buis.common.utils.poi.ExcelUtil;
import com.buis.domain.UserInfo;
import com.buis.service.IUserInfoService;
import com.buis.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户信息Controller
 *
 * @author buis
 * @date 2024-10-11
 */
@Api(tags = "用户信息Controller")
@RestController
@RequestMapping("/buis/userInfo")
public class UserInfoController extends BaseController {
    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 查询用户信息列表
     */
    @ApiOperation("查询用户信息列表")
    @PreAuthorize("@ss.hasPermi('buis:userInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserInfo userInfo) {
        startPage();
        List<UserInfoVo> list = userInfoService.selectUserInfoList(userInfo);
        return getDataTable(list);
    }

    /**
     * 导出用户信息列表
     */
    @ApiOperation("导出用户信息列表")
    @PreAuthorize("@ss.hasPermi('buis:userInfo:export')")
    @Log(title = "用户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserInfo userInfo) {
        List<UserInfoVo> list = userInfoService.selectUserInfoList(userInfo);
        ExcelUtil<UserInfoVo> util = new ExcelUtil<>(UserInfoVo.class);
        util.exportExcel(response, list, "用户信息数据");
    }

    /**
     * 获取用户信息详细信息
     */
    @ApiOperation("获取用户信息详细信息")
    @PreAuthorize("@ss.hasPermi('buis:userInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(userInfoService.selectUserInfoById(id));
    }

    /**
     * 新增用户信息
     */
    @ApiOperation("新增用户信息")
    @PreAuthorize("@ss.hasPermi('buis:userInfo:add')")
    @Log(title = "用户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserInfo userInfo) {
        return toAjax(userInfoService.insertUserInfo(userInfo));
    }

    /**
     * 修改用户信息
     */
    @ApiOperation("修改用户信息")
    @PreAuthorize("@ss.hasPermi('buis:userInfo:edit')")
    @Log(title = "用户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserInfo userInfo) {
        return toAjax(userInfoService.updateUserInfo(userInfo));
    }

    /**
     * 删除用户信息
     */
    @ApiOperation("删除用户信息")
    @PreAuthorize("@ss.hasPermi('buis:userInfo:remove')")
    @Log(title = "用户信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(userInfoService.deleteUserInfoByIds(ids));
    }
}
