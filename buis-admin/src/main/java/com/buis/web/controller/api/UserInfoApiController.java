package com.buis.web.controller.api;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.buis.common.constant.RedisConstant;
import com.buis.common.core.controller.BaseController;
import com.buis.common.core.domain.R;
import com.buis.common.exception.ServiceException;
import com.buis.common.utils.SecurityUtils;
import com.buis.domain.Award;
import com.buis.domain.CollegeMajor;
import com.buis.domain.UserInfo;
import com.buis.dto.RegisterDto;
import com.buis.dto.UserLoginDto;
import com.buis.service.ICollegeMajorService;
import com.buis.service.IUserInfoService;
import com.buis.service.impl.AwardServiceImpl;
import com.buis.vo.FindUserVo;
import com.buis.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private AwardServiceImpl awardService;

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
     * 根据学号查询用户信息
     */
    @ApiOperation("根据学号查询用户信息")
    @PostMapping("/getUserInfoByNumber")
    public R<UserInfoVo> getUserInfoByNumber(@RequestParam("number") String number){
        UserInfo userInfo = userInfoService.selectUserInfoBynumber(number);
        if (userInfo==null){
            throw new ServiceException("用户不存在!");
        }
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtil.copyProperties(userInfo,userInfoVo);
        return R.ok(userInfoVo);
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
    public R findPassword(@ApiParam(name = "findUserVo", value = "找回密码信息", required = true) @RequestBody FindUserVo findUserVo) {
        //判空
        if (findUserVo == null) {
            throw new ServiceException("参数不能为空!");
        }
        //判断用户是否存在
        String eamil = findUserVo.getEmail();
        UserInfo user = userInfoService.selectUserInfoByEmail(eamil);
        if (user==null){
            throw new ServiceException("用户不存在,请先注册!");
        }
        //判断验证码输入是否正确
        //从缓存中取到code
        String rcode = (String) redisTemplate.opsForValue().get(RedisConstant.EMAIL + eamil);
        if (!rcode.equals(findUserVo.getCode())) {
            throw new ServiceException("验证码错误~");
        }
        //判断两次输入的密码是否一致
        String newPassword = findUserVo.getNewPassword();
        String checkPassword = findUserVo.getCheckPassword();
        if(!newPassword.equals(checkPassword)){
            throw new ServiceException("两次输入的密码不一致,请校验~");
        }
        //密码加密
        String encryptPassword = SaSecureUtil.md5(SALT + newPassword);
        user.setPassword(encryptPassword);
        //更新
        return R.ok(userInfoService.updateUserInfo(user));

    }

    /**
     * 根据用户id返回用户所获得的奖项
     */
    @ApiOperation("根据用户id返回该用户所获得的奖项")
    @GetMapping("/getUserAward")
    public R<List<Award>> getawardbyId(
            @ApiParam(value = "用户id", required = true)
            @RequestParam("userId") Long userId){
        List<Award> awards = awardService.selectAwardById(userId);
        return R.ok(awards);
    }
}
