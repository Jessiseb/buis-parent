package com.buis.web.controller.api;

import cn.hutool.core.bean.BeanUtil;
import com.buis.common.core.domain.entity.SysDictData;
import com.buis.common.core.page.MyPageVo;
import com.buis.system.service.ISysDictDataService;
import com.buis.vo.SysDictDataVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "字典信息管理Api")
@RestController
@RequestMapping("/api/dict")
public class DictApiController {
    @Autowired
    private ISysDictDataService sysDictDataService;
    /**
     * 查询职称信息字典
     */
    @ApiOperation("查询职称信息字典")
    @GetMapping("/getTitle")
    public MyPageVo<List<SysDictDataVo>> getTitle() {
        return getDictDataByType("teacher_title");
    }
    /**
     * 查询班级字典
     */
    @ApiOperation("查询班级字典")
    @GetMapping("/getStuClass")
    public MyPageVo<List<SysDictDataVo>> getStuClass() {
        return getDictDataByType("stu_class");
    }
    /**
     * 查询年级字典
     */
    @ApiOperation("查询年级字典")
    @GetMapping("/getStuGrade")
    public MyPageVo<List<SysDictDataVo>> getStuGrade() {
        return getDictDataByType("stu_grade");
    }
    /**
     * 查询性别字典
     */
    @ApiOperation("查询性别字典")
    @GetMapping("/getSex")
    public MyPageVo<List<SysDictDataVo>> getSex() {
        return getDictDataByType("sys_user_sex");
    }

    /**
     * 查询互选状态字典
     */
    @ApiOperation("查询互选状态字典")
    @GetMapping("/getChooseStatus")
    public MyPageVo<List<SysDictDataVo>> getChooseStatus() {
        return getDictDataByType("choose_status");
    }

    /**
     * 查询计分规则字典
     */
    @ApiOperation("查询计分规则字典")
    @GetMapping("/getScoreRule")
    public MyPageVo<List<SysDictDataVo>> getScoreRule() {
        return getDictDataByType("score_rule");
    }

    /**
     * 查询比赛类型字典
     */
    @ApiOperation("查询比赛类型字典")
    @GetMapping("/getRaceType")
    public MyPageVo<List<SysDictDataVo>> getRaceType() {
        return getDictDataByType("race_type");
    }

    /**
     * 查询比赛状态字典
     */
    @ApiOperation("查询比赛状态字典")
    @GetMapping("/getRaceStatus")
    public MyPageVo<List<SysDictDataVo>> getRaceStatus() {
        return getDictDataByType("race_status");
    }

    /**
     * 查询比赛级别字典
     */
    @ApiOperation("查询比赛级别字典")
    @GetMapping("/getRaceLevel")
    public MyPageVo<List<SysDictDataVo>> getRaceLevel() {
        return getDictDataByType("race_level");
    }

    /**
     * 查询审核状态字典
     */
    @ApiOperation("查询审核状态字典")
    @GetMapping("/getReviewStatus")
    public MyPageVo<List<SysDictDataVo>> getReviewStatus() {
        return getDictDataByType("review_status");
    }

    /**
     * 查询字典信息
     * @param dictType 字典类型
     * @return MyPageVo<List<SysDictDataVo>> 返回字典信息的分页数据
     */
    private MyPageVo<List<SysDictDataVo>> getDictDataByType(String dictType) {
        SysDictData dictData = new SysDictData();
        dictData.setDictType(dictType);  // 设置字典类型
        dictData.setStatus("0");  // 只查询有效的字典数据
        List<SysDictData> list = sysDictDataService.selectDictDataList(dictData);
        // 将字典数据列表转换为VO对象
        List<SysDictDataVo> sysDictDataVo = BeanUtil.copyToList(list, SysDictDataVo.class);
        return new MyPageVo<>(sysDictDataVo, new PageInfo(sysDictDataVo).getTotal());
    }

}
