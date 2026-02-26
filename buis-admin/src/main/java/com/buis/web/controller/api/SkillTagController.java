package com.buis.web.controller.api;



import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.exception.ServiceException;
import com.buis.model.domain.SkillTag;
import com.buis.model.vo.SkillTagVo;
import com.buis.service.ISkillTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 技能标签管理控制器
 *
 *
 */
@Api(tags = "技能标签管理")
@RestController
@RequestMapping("/api/skillTag")
public class SkillTagController extends BaseController {

    @Autowired
    private ISkillTagService skillTagService;

    /**
     * 查询技能标签列表
     */
    @GetMapping("/list")
    @ApiOperation("查询技能标签列表")
    public TableDataInfo list(SkillTag skillTag,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        startPage();
        List<SkillTagVo> list = skillTagService.selectSkillTagList(skillTag);
        return getDataTable(list);
    }

    /**
     * 获取技能标签详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取技能标签详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        SkillTag skillTag = skillTagService.selectSkillTagById(id);
        return AjaxResult.success(skillTag);
    }

    /**
     * 查询所有启用的技能标签
     */
    @GetMapping("/listAll")
    @ApiOperation("查询所有启用的技能标签")
    public AjaxResult listAll() {
        List<SkillTag> list = skillTagService.selectAllActiveTags();
        return AjaxResult.success(list);
    }

    /**
     * 根据分类查询技能标签
     */
    @GetMapping("/listByCategory/{category}")
    @ApiOperation("根据分类查询技能标签")
    public AjaxResult listByCategory(@PathVariable("category") String category) {
        List<SkillTag> list = skillTagService.selectSkillTagsByCategory(category);
        return AjaxResult.success(list);
    }

    /**
     * 搜索技能标签
     */
    @GetMapping("/search")
    @ApiOperation("搜索技能标签")
    public AjaxResult search(@RequestParam("keyword") String keyword) {
        List<SkillTag> list = skillTagService.searchSkillTags(keyword);
        return AjaxResult.success(list);
    }

    /**
     * 新增技能标签
     */
    @PostMapping
    @ApiOperation("新增技能标签")
    public AjaxResult add(@Validated @RequestBody SkillTag skillTag) {
        try {
            int rows = skillTagService.insertSkillTag(skillTag);
            return rows > 0 ? success("新增成功") : error("新增失败");
        } catch (ServiceException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("新增技能标签失败", e);
            return error("新增失败，系统异常");
        }
    }

    /**
     * 修改技能标签
     */
    @PutMapping
    @ApiOperation("修改技能标签")
    public AjaxResult edit(@Validated @RequestBody SkillTag skillTag) {
        try {
            if (skillTag.getId() == null) {
                return error("ID不能为空");
            }
            int rows = skillTagService.updateSkillTag(skillTag);
            return rows > 0 ? success("修改成功") : error("修改失败");
        } catch (ServiceException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("修改技能标签失败", e);
            return error("修改失败，系统异常");
        }
    }

    /**
     * 删除技能标签
     */
    @DeleteMapping("/{ids}")
    @ApiOperation("删除技能标签")
    public AjaxResult remove(@PathVariable Long[] ids) {
        try {
            int rows = skillTagService.deleteSkillTagByIds(ids);
            return rows > 0 ? success("删除成功") : error("删除失败");
        } catch (Exception e) {
            logger.error("删除技能标签失败", e);
            return error("删除失败，系统异常");
        }
    }
}