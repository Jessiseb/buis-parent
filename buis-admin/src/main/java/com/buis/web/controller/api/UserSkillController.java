package com.buis.web.controller.api;


import cn.dev33.satoken.stp.StpUtil;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.StringUtils;
import com.buis.model.domain.UserSkill;
import com.buis.model.dto.SkillExtractRequest;
import com.buis.model.dto.SkillExtractResult;
import com.buis.model.vo.UserSkillVo;
import com.buis.service.IUserSkillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户技能管理控制器
 *
 *
 */
@Api(tags = "用户技能管理")
@RestController
@RequestMapping("/api/userSkill")
public class UserSkillController extends BaseController {

    @Autowired
    private IUserSkillService userSkillService;

    /**
     * 查询当前登录用户的技能列表
     */
    @GetMapping("/mySkills")
    @ApiOperation("查询当前登录用户的技能列表")
    public AjaxResult getMySkills() {
        try {
            Long userId = Long.parseLong(StpUtil.getLoginId().toString());
            List<UserSkillVo> list = userSkillService.selectUserSkillsByUserId(userId);
            return AjaxResult.success(list);
        } catch (Exception e) {
            logger.error("查询用户技能列表失败", e);
            return error("查询失败");
        }
    }

    /**
     * 查询指定用户的技能列表
     */
    @GetMapping("/listByUser/{userId}")
    @ApiOperation("查询指定用户的技能列表")
    public AjaxResult listByUser(@PathVariable("userId") Long userId) {
        try {
            List<UserSkillVo> list = userSkillService.selectUserSkillsByUserId(userId);
            return AjaxResult.success(list);
        } catch (Exception e) {
            logger.error("查询用户技能列表失败", e);
            return error("查询失败");
        }
    }

    /**
     * 新增用户技能关联
     */
    @PostMapping
    @ApiOperation("新增用户技能关联")
    public AjaxResult add(@Validated @RequestBody UserSkill userSkill) {
        try {
            Long userId = Long.parseLong(StpUtil.getLoginId().toString());
            userSkill.setUserId(userId);

            int rows = userSkillService.insertUserSkill(userSkill);
            return toAjax(rows);
        } catch (ServiceException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("新增用户技能失败", e);
            return error("新增失败，系统异常");
        }
    }

    /**
     * 批量新增用户技能关联
     */
    @PostMapping("/batch")
    @ApiOperation("批量新增用户技能关联")
    public AjaxResult batchAdd(@Validated @RequestBody List<UserSkill> userSkillList) {
        try {
            Long userId = Long.parseLong(StpUtil.getLoginId().toString());
            // 设置当前用户ID
            for (UserSkill userSkill : userSkillList) {
                userSkill.setUserId(userId);
            }

            int rows = userSkillService.batchInsertUserSkill(userSkillList);
            return toAjax(rows);
        } catch (ServiceException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("批量新增用户技能失败", e);
            return error("批量新增失败，系统异常");
        }
    }

    /**
     * 修改用户技能关联
     */
    @PutMapping
    @ApiOperation("修改用户技能关联")
    public AjaxResult edit(@Validated @RequestBody UserSkill userSkill) {
        try {
            int rows = userSkillService.updateUserSkill(userSkill);
            return toAjax(rows);
        } catch (ServiceException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("修改用户技能失败", e);
            return error("修改失败，系统异常");
        }
    }

    /**
     * 删除用户技能关联
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除用户技能关联")
    public AjaxResult remove(@PathVariable("id") Long id) {
        try {
            int rows = userSkillService.deleteUserSkillById(id);
            return toAjax(rows);
        } catch (Exception e) {
            logger.error("删除用户技能失败", e);
            return error("删除失败，系统异常");
        }
    }

    /**
     * 删除当前用户的某个技能
     */
    @DeleteMapping("/removeSkill/{skillTagId}")
    @ApiOperation("删除当前用户的某个技能")
    public AjaxResult removeSkill(@PathVariable("skillTagId") Long skillTagId) {
        try {
            Long userId = Long.parseLong(StpUtil.getLoginId().toString());
            int rows = userSkillService.deleteUserSkillByUserIdAndSkillTagId(userId, skillTagId);
            return toAjax(rows);
        } catch (Exception e) {
            logger.error("删除用户技能失败", e);
            return error("删除失败，系统异常");
        }
    }

    /**
     * 清空当前用户的所有技能
     */
    @DeleteMapping("/clearMySkills")
    @ApiOperation("清空当前用户的所有技能")
    public AjaxResult clearMySkills() {
        try {
            Long userId = Long.parseLong(StpUtil.getLoginId().toString());
            int rows = userSkillService.deleteUserSkillsByUserId(userId);
            return toAjax(rows);
        } catch (Exception e) {
            logger.error("清空用户技能失败", e);
            return error("清空失败，系统异常");
        }
    }

    /**
     * 更新当前用户的技能列表（全量更新）
     */
    @PutMapping("/updateMySkills")
    @ApiOperation("更新当前用户的技能列表")
    public AjaxResult updateMySkills(@RequestBody List<Long> skillTagIds) {
        try {
            Long userId = Long.parseLong(StpUtil.getLoginId().toString());
            int rows = userSkillService.updateUserSkills(userId, skillTagIds);
            return toAjax(rows);
        } catch (ServiceException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("更新用户技能失败", e);
            return error("更新失败，系统异常");
        }
    }

    /**
     * AI提取并添加技能
     */
    @PostMapping("/extractSkills")
    @ApiOperation("AI提取并添加技能")
    public AjaxResult extractSkills(@Validated @RequestBody SkillExtractRequest request) {
        try {
            Long userId = Long.parseLong(StpUtil.getLoginId().toString());

            // 参数校验
            if (StringUtils.isEmpty(request.getText())) {
                return error("文本内容不能为空");
            }
            if (request.getType() == null) {
                request.setType("user");
            }

            SkillExtractResult result = userSkillService.extractAndAddUserSkills(userId, request);
            return AjaxResult.success("AI提取完成", result);
        } catch (ServiceException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("AI提取技能失败", e);
            return error("AI提取失败，系统异常");
        }
    }

    /**
     * 检查当前用户是否拥有某个技能
     */
    @GetMapping("/hasSkill/{skillTagId}")
    @ApiOperation("检查当前用户是否拥有某个技能")
    public AjaxResult hasSkill(@PathVariable("skillTagId") Long skillTagId) {
        try {
            Long userId = Long.parseLong(StpUtil.getLoginId().toString());
            boolean hasSkill = userSkillService.hasSkill(userId, skillTagId);
            return AjaxResult.success(hasSkill);
        } catch (Exception e) {
            logger.error("检查用户技能失败", e);
            return error("检查失败");
        }
    }
}
