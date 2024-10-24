package com.buis.web.controller.api;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.buis.common.constant.RedisConstant;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.R;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.StringUtils;
import com.buis.domain.UserInfo;
import com.buis.dto.RegisterDTO;
import com.buis.dto.UserDTO;
import com.buis.service.IUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.naming.ldap.Rdn;

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
    public R<String> login(@RequestBody UserDTO userDTO) {
        boolean isLogin = StpUtil.isLogin();
        if (isLogin){
            //已经登录过就无需登录
            throw new ServiceException("您已登录~");
        }

        String email = userDTO.getEmail();
        String passWord = userDTO.getPassWord();
        //TODO 验证码校验
        String code = userDTO.getCode();
        //密码加密
        String encryptPassword = SaSecureUtil.md5(passWord);
        //获取登录用户id
        Long id = userDTO.getId();
        //根据id查到用户
        UserInfo user = userInfoService.selectUserInfoById(id);
        if (user == null){
            throw new ServiceException("用户不存在,请先注册!!");
        }
        //从缓存中取验证码
        String rcode = (String) redisTemplate.opsForValue().get(RedisConstant.EMAIL + email);
        //缓存中没有验证码说明还没有发送验证码
        if (StringUtils.isEmpty(rcode)){
            throw new ServiceException("请先发送验证码!");
        }
        if (email.equals(user.getEmail()) && encryptPassword.equals(user.getPassword()) && code.equals(rcode)) {
            StpUtil.login(id);
            return R.ok("登录成功!");
        }
        return R.fail("登录失败!");
    }

    @ApiOperation("用户注册功能")
    @PostMapping("/register")
    public R<Integer> register(@RequestBody RegisterDTO registerDTO) {
        try {
            int result = userInfoService.register(registerDTO);
            return R.ok(result);
        } catch (Exception e) {
            //TODO 日志记录用户注册失败信息
            e.printStackTrace();
            throw new ServiceException("注册失败,请重新注册!");
        }

    }

    @ApiOperation("用户退出登录功能")
    @PostMapping("/logout")
    public R<String> logout(Long id) {
        StpUtil.logout(id);
        return R.ok("退出成功!");
    }

    @GetMapping("checkLogin")
    public R checkLogin() {
        // 检验当前会话是否已经登录, 如果未登录，则抛出异常：`NotLoginException`
        StpUtil.checkLogin();
        // 抛出异常后，代码将走入全局异常处理（GlobalException.java），如果没有抛出异常，则代表通过了登录校验，返回下面信息
        return R.ok("校验登录成功");
    }


}
