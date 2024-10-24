package com.buis.web.controller.api;

import cn.hutool.core.util.RandomUtil;
import com.buis.common.constant.RedisConstant;
import com.buis.common.core.domain.R;
import com.buis.common.utils.StringUtils;
import com.buis.service.IEmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Results;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 发送邮箱功能
 *
 * @author zjx
 * @time 2024-10-24
 */
@Api(tags = "发送邮箱功能Api")
@RestController
@RequestMapping("/email")
public class EmailController {

    @Resource
    private IEmailService emailService;

    @Resource
    private RedisTemplate redisTemplate;

    @ApiOperation("邮箱发送")
    @PostMapping("/sendEmail/{email}")
    public R<String> sendEmail(@PathVariable("email") String email) {
        //生成四位数验证码
        String code = RandomUtil.randomNumbers(4);

        //构建信息
        String massage = "验证码为：" + code + "。您正在登录或注册创新创业中心平台，此验证码5分钟内有效，请勿外传！";
        try {
            //发送邮件
            emailService.sendEmail(email, "创新创业中心", massage);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("发送邮箱验证码失败!");
        }
        //不管有无发过,,直接更新缓存的验证码
        redisTemplate.opsForValue().set(RedisConstant.EMAIL + email,code,RedisConstant.EMAIL_TTL, TimeUnit.MINUTES);
        return R.ok("验证码发送成功!!");
    }
}
