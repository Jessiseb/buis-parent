package com.buis.web.controller.api;


import com.buis.common.annotation.Log;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.R;
import com.buis.common.core.page.MyPageVo;
import com.buis.common.enums.BusinessType;
import com.buis.model.domain.Score;
import com.buis.model.domain.UserInfo;
import com.buis.service.IScoreService;
import com.buis.service.IUserInfoService;
import com.buis.model.vo.ScoreVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "评分Api")
@RestController
@RequestMapping("/api/score")
public class ScoreApiController extends BaseController {
    @Autowired
    private IScoreService scoreService;
    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 查询评分管理列表
     */
    @ApiOperation("查询评分管理列表")
    @GetMapping("/list")
    public MyPageVo<List<Score>> list(Score score) {
        startPage();
        //只可以查询自己的评分
        UserInfo loginUser = userInfoService.getLoginUser();
        score.setTeacher(loginUser.getId());
        List<ScoreVo> list = scoreService.selectScoreList(score);
        return new MyPageVo(list);
    }

    /**
     * 获取评分管理详细信息
     */
    @ApiOperation("获取评分管理详细信息")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") Long id) {
        return R.ok(scoreService.selectScoreById(id));
    }

    /**
     * 新增评分管理
     */
    @ApiOperation("新增评分管理")
    @Log(title = "评分管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody Score score) {
        return scoreService.addScore(score);
    }

    /**
     * 修改评分管理
     */
    @ApiOperation("修改评分管理")
    @PutMapping
    public R edit(@RequestBody Score score) {
        return R.ok(scoreService.updateScore(score));
    }

    /**
     * 删除评分管理
     */
    @ApiOperation("删除评分管理")
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable Long[] ids) {
        return R.ok(scoreService.deleteScoreByIds(ids));
    }
}
