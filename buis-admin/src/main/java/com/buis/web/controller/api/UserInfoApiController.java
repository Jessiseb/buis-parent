package com.buis.web.controller.api;

import cn.dev33.satoken.stp.StpUtil;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.R;
import com.buis.common.exception.ServiceException;
import com.buis.dto.RegisterDTO;
import com.buis.dto.UserLoginDTO;
import com.buis.service.IUserInfoService;
import com.buis.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(tags = "用户信息Api")
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserInfoApiController extends BaseController {
    @Resource
    private IUserInfoService userInfoService;
    @Resource
    private RedisTemplate redisTemplate;



    @ApiOperation("用户登录功能")
    @PostMapping("/login")
    public R<UserInfoVo> login(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        if (userLoginDTO == null){
            throw new ServiceException("参数有误!");
        }
        UserInfoVo user = userInfoService.userLogin(userLoginDTO,request);
        return R.ok(user);

    }

    @ApiOperation("用户注册功能")
    @PostMapping("/register")
    public R<Long> register(@RequestBody RegisterDTO registerDTO) {
        //判空
        if (registerDTO == null){
            throw new ServiceException("参数错误!");
        }
        userInfoService.register(registerDTO);
        return R.ok();
    }

    @ApiOperation("用户退出登录功能")
    @PostMapping("/logout")
    public R<String> logout(Long id) {
        StpUtil.logout(id);
        return R.ok("退出成功!");
    }


    @ApiOperation("检查用户是否登录功能")
    @GetMapping("checkLogin")
    public R checkLogin() {
        // 检验当前会话是否已经登录, 如果未登录，则抛出异常：`NotLoginException`
        StpUtil.checkLogin();
        // 抛出异常后，代码将走入全局异常处理（GlobalException.java），如果没有抛出异常，则代表通过了登录校验，返回下面信息
        return R.ok("校验登录成功");
    }

}
