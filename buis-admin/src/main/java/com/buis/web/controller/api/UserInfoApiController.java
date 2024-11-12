package com.buis.web.controller.api;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.AjaxResult;
import com.buis.common.core.domain.R;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.SecurityUtils;
import com.buis.domain.CollegeMajor;
import com.buis.domain.UserInfo;
import com.buis.dto.RegisterDto;
import com.buis.dto.UserLoginDto;
import com.buis.service.ICollegeMajorService;
import com.buis.service.IUserInfoService;
import com.buis.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "用户信息Api")
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserInfoApiController extends BaseController {
    @Resource
    private IUserInfoService userInfoService;
    @Autowired
    private ICollegeMajorService collegeMajorService;



    @ApiOperation("用户登录功能")
    @PostMapping("/login")
    public R<String> login(@ApiParam(name = "userLoginDTO", value = "用户登录信息", required = true)@RequestBody UserLoginDto userLoginDTO, HttpServletRequest request) {
        if (userLoginDTO == null) {
            throw new ServiceException("参数有误!");
        }
        String token = userInfoService.userLogin(userLoginDTO, request);
        return R.ok(token);
    }


    @ApiOperation("用户注册功能")
    @PostMapping("/register")
    public R<Long> register(@ApiParam(name = "registerDTO", value = "用户注册信息", required = true)@RequestBody RegisterDto registerDTO) {
        //判空
        if (registerDTO == null) {
            throw new ServiceException("参数错误!");
        }
        long result = userInfoService.register(registerDTO);
        //返回注册id
        return R.ok(result);
    }

    @ApiOperation("用户退出登录功能")
    @PostMapping("/logout")
    public R logout() {
        StpUtil.logout();
        return R.ok(null,"退出成功!");
    }


    /**
     * 查询用户信息
     * @return
     */
    @ApiOperation("查询用户信息")
    @PostMapping("/getUserInfo")
    public R<UserInfoVo> getUserInfo(){
        Long userId = SecurityUtils.getFrontLoginUserId();
        return R.ok(userInfoService.selectUserInfoById(userId));
    }

    /**
     * 编辑用户信息
     * @return
     */
    @ApiOperation("编辑用户信息")
    @PostMapping("/updateUserInfo")
    public R updateUserInfo(@RequestBody UserInfo userInfo){
        Long userId = SecurityUtils.getFrontLoginUserId();
        userInfo.setId(userId);
        return R.ok(userInfoService.updateUserInfo(userInfo));
    }



    /**
     * 查询专业信息列表
     */
    @ApiOperation("查询专业信息列表")
    @GetMapping("/listCollegeMajor")
    public R<List<CollegeMajor>> listCollegeMajor(CollegeMajor collegeMajor) {
        List<CollegeMajor> list = collegeMajorService.selectCollegeMajorList(collegeMajor);
        return R.ok(list);
    }

}
