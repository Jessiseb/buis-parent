package com.buis.web.controller.api;

import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.StringUtils;
import com.buis.model.domain.TeamSkill;
import com.buis.model.dto.SkillExtractRequest;
import com.buis.model.dto.SkillExtractResult;
import com.buis.model.vo.TeamSkillVo;
import com.buis.service.ITeamSkillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 团队技能需求管理控制器
 *
 * @author buis
 */
@Api(tags = "团队技能需求管理")
@RestController
@RequestMapping("/api/teamSkill")
public class TeamSkillController extends BaseController {

    @Autowired
    private ITeamSkillService teamSkillService;

    /**
     * 查询团队技能需求列表
     */
    @GetMapping("/listByTeam/{teamId}")
    @ApiOperation("查询团队技能需求列表")
    public AjaxResult listByTeam(@PathVariable("teamId") Long teamId) {
        try {
            List<TeamSkillVo> list = teamSkillService.selectTeamSkillsByTeamId(teamId);
            return AjaxResult.success(list);
        } catch (Exception e) {
            logger.error("查询团队技能需求列表失败", e);
            return error("查询失败");
        }
    }

    /**
     * 新增团队技能需求
     */
    @PostMapping
    @ApiOperation("新增团队技能需求")
    public AjaxResult add(@Validated @RequestBody TeamSkill teamSkill) {
        try {
            int rows = teamSkillService.insertTeamSkill(teamSkill);
            return toAjax(rows);
        } catch (ServiceException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("新增团队技能需求失败", e);
            return error("新增失败，系统异常");
        }
    }

    /**
     * 批量新增团队技能需求
     */
    @PostMapping("/batch")
    @ApiOperation("批量新增团队技能需求")
    public AjaxResult batchAdd(@Validated @RequestBody List<TeamSkill> teamSkillList) {
        try {
            int rows = teamSkillService.batchInsertTeamSkill(teamSkillList);
            return toAjax(rows);
        } catch (ServiceException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("批量新增团队技能需求失败", e);
            return error("批量新增失败，系统异常");
        }
    }

    /**
     * 修改团队技能需求
     */
    @PutMapping
    @ApiOperation("修改团队技能需求")
    public AjaxResult edit(@Validated @RequestBody TeamSkill teamSkill) {
        try {
            int rows = teamSkillService.updateTeamSkill(teamSkill);
            return toAjax(rows);
        } catch (ServiceException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("修改团队技能需求失败", e);
            return error("修改失败，系统异常");
        }
    }

    /**
     * 删除团队技能需求
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除团队技能需求")
    public AjaxResult remove(@PathVariable("id") Long id) {
        try {
            int rows = teamSkillService.deleteTeamSkillById(id);
            return toAjax(rows);
        } catch (Exception e) {
            logger.error("删除团队技能需求失败", e);
            return error("删除失败，系统异常");
        }
    }

    /**
     * 删除团队的某个技能需求
     */
    @DeleteMapping("/removeSkill/{teamId}/{skillTagId}")
    @ApiOperation("删除团队的某个技能需求")
    public AjaxResult removeSkill(@PathVariable("teamId") Long teamId,
                                  @PathVariable("skillTagId") Long skillTagId) {
        try {
            int rows = teamSkillService.deleteTeamSkillByTeamIdAndSkillTagId(teamId, skillTagId);
            return toAjax(rows);
        } catch (Exception e) {
            logger.error("删除团队技能需求失败", e);
            return error("删除失败，系统异常");
        }
    }

    /**
     * 清空团队的所有技能需求
     */
    @DeleteMapping("/clearTeamSkills/{teamId}")
    @ApiOperation("清空团队的所有技能需求")
    public AjaxResult clearTeamSkills(@PathVariable("teamId") Long teamId) {
        try {
            int rows = teamSkillService.deleteTeamSkillsByTeamId(teamId);
            return toAjax(rows);
        } catch (Exception e) {
            logger.error("清空团队技能需求失败", e);
            return error("清空失败，系统异常");
        }
    }

    /**
     * 更新团队的技能需求（全量更新）
     */
    @PutMapping("/updateTeamSkills/{teamId}")
    @ApiOperation("更新团队的技能需求")
    public AjaxResult updateTeamSkills(@PathVariable("teamId") Long teamId,
                                       @RequestBody List<TeamSkill> teamSkills) {
        try {
            int rows = teamSkillService.updateTeamSkills(teamId, teamSkills);
            return toAjax(rows);
        } catch (ServiceException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("更新团队技能需求失败", e);
            return error("更新失败，系统异常");
        }
    }

    /**
     * AI提取并添加团队技能需求
     */
    @PostMapping("/extractSkills/{teamId}")
    @ApiOperation("AI提取并添加团队技能需求")
    public AjaxResult extractSkills(@PathVariable("teamId") Long teamId,
                                    @Validated @RequestBody SkillExtractRequest request) {
        try {
            // 参数校验
            if (StringUtils.isEmpty(request.getText())) {
                return error("文本内容不能为空");
            }
            if (request.getType() == null) {
                request.setType("team");
            }

            SkillExtractResult result = teamSkillService.extractAndAddTeamSkills(teamId, request);
            return AjaxResult.success("AI提取完成", result);
        } catch (ServiceException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("AI提取团队技能失败", e);
            return error("AI提取失败，系统异常");
        }
    }

    /**
     * 检查团队是否需要某个技能
     */
    @GetMapping("/needsSkill/{teamId}/{skillTagId}")
    @ApiOperation("检查团队是否需要某个技能")
    public AjaxResult needsSkill(@PathVariable("teamId") Long teamId,
                                 @PathVariable("skillTagId") Long skillTagId) {
        try {
            boolean needsSkill = teamSkillService.needsSkill(teamId, skillTagId);
            return AjaxResult.success(needsSkill);
        } catch (Exception e) {
            logger.error("检查团队技能需求失败", e);
            return error("检查失败");
        }
    }
}
