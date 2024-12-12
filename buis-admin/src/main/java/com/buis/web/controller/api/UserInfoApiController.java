package com.buis.web.controller.api;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.R;
import com.buis.common.core.domain.entity.SysDictData;
import com.buis.common.core.page.MyPageVo;
import com.buis.common.core.page.TableDataInfo;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.SecurityUtils;
import com.buis.domain.CollegeMajor;
import com.buis.domain.UserInfo;
import com.buis.dto.RegisterDto;
import com.buis.dto.UserLoginDto;
import com.buis.mapper.UserInfoMapper;
import com.buis.service.ICollegeMajorService;
import com.buis.service.IUserInfoService;
import com.buis.system.service.ISysDictDataService;
import com.buis.vo.SysDictDataVo;
import com.buis.vo.UserInfoVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.buis.service.impl.UserInfoServiceImpl.SALT;

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
        String password = userInfo.getPassword();
        String encryptPassword = SaSecureUtil.md5(SALT + password);
        userInfo.setPassword(encryptPassword);
        return R.ok(userInfoService.updateUserInfo(userInfo));
    }

    /**
     * 查询专业信息列表
     */
    @ApiOperation("查询专业信息列表")
    @GetMapping("/getCollegeMajor")
    public R<List<CollegeMajor>> getCollegeMajor(
            @ApiParam(name = "collegeMajor", value = "筛选条件", required = false) CollegeMajor collegeMajor) {
        List<CollegeMajor> list = collegeMajorService.selectCollegeMajorList(collegeMajor);
        return R.ok(list);
    }

    /**
     * 找回密码
     */
    @ApiOperation("找回密码")
    @PostMapping("/findPassword")
    public R findPassword(@ApiParam(name = "userInfo", value = "用户信息", required = true) @RequestBody UserInfo userInfo) {
        //判空
        if (userInfo == null || userInfo.getEmail() == null || userInfo.getPassword() == null) {
            throw new ServiceException("参数错误!");
        }
        //判断用户是否存在
        UserInfo user = userInfoService.selectUserInfoByEmail(userInfo.getEmail());
        if (user==null){
            throw new ServiceException("用户不存在,请先注册!");
        }
        Long userId = user.getId();
        userInfo.setId(userId);
        String password = userInfo.getPassword();
        //密码加密
        String encryptPassword = SaSecureUtil.md5(SALT + password);
        userInfo.setPassword(encryptPassword);
        //更新
        return R.ok(userInfoService.updateUserInfo(userInfo));

    }



}
