package com.buis.web.controller.api;

import cn.hutool.core.util.RandomUtil;
import com.buis.common.constant.RedisConstant;
import com.buis.common.core.domain.R;
import com.buis.common.exception.ServiceException;
import com.buis.domain.UserInfo;
import com.buis.service.IEmailService;
import com.buis.service.ITeamInfoService;
import com.buis.service.IUserInfoService;
import com.buis.vo.TeamInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 发送邮箱功能
 *
 * @author zjx
 * @time 2024-10-24
 */
@Api(tags = "发送邮箱功能Api")
@RestController
@RequestMapping("/api/email")
public class EmailApiController {

    @Resource
    private IEmailService emailService;
    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private ITeamInfoService teamInfoService;
    /**
     * 发送邮箱验证码
     * @param email
     * @return
     */
    @ApiOperation("邮箱发送")
    @PostMapping("/sendEmail/{email}")
    public R<String> sendEmail(@PathVariable("email") String email) {
        //校验账户
        //邮箱的正则表达式
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Matcher matcher = Pattern.compile(emailRegex).matcher(email);
        if (!matcher.find()) {
            throw new ServiceException("邮箱格式有误,请重新输入!");
        }
//        //验证是否有对应账号
//        UserInfo user = userInfoService.selectUserInfoByEmail(email);
//        if (user==null){
//            throw new ServiceException("用户不存在,请先注册!");
//        }


        //生成四位数验证码
        String code = RandomUtil.randomNumbers(4);

        //构建信息
        String massage = "验证码为：" + code + "。您正在登录或注册广东白云学院大数据与计算机学院创新创业中心平台，此验证码5分钟内有效，请勿外传！";
        try {
            //发送邮件
            emailService.sendEmail(email, "广东白云学院大数据与计算机学院创新创业中心", massage);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("发送邮箱验证码失败!");
        }
        //不管有无发过,,直接更新缓存的验证码
        redisTemplate.opsForValue().set(RedisConstant.EMAIL + email, code, RedisConstant.EMAIL_TTL, TimeUnit.MINUTES);
        return R.ok("验证码发送成功!!");
    }

    /**
     * 发送驳回通知到指定邮箱
     * @param email
     * @param inform
     * @return
     */
    @ApiOperation("发送驳回通知到指定邮箱")
    @PostMapping("/sendInformToEmail")
    public R<String> sendInformToEmail(@RequestParam String email, @RequestParam String inform) {
        //校验账户
        //邮箱的正则表达式
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Matcher matcher = Pattern.compile(emailRegex).matcher(email);
        if (!matcher.find()) {
            throw new ServiceException("邮箱格式有误,请重新输入!");
        }

        //构建信息
        String massage = "经过审核,您的提交的材料不符合要求,原因如下:" + inform + "。请修改后重新提交!";
        try {
            //发送邮件
            emailService.sendEmail(email, "广东白云学院大数据与计算机学院创新创业中心", massage);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("发送通知失败!");
        }
        return R.ok("通知发送成功!!");
    }

    /**
     * 队伍队长邀请成员
     * @param teamId 队伍ID
     * @param memberId 成员ID
     * @return
     */

    @ApiOperation("队长邀请成员")
    @PostMapping("/inviteMember")
    public R<String> inviteMember(@RequestParam Long teamId, @RequestParam Long memberId) {
        // 获取当前登录用户信息
        UserInfo loginUser = userInfoService.getLoginUser();
        // 获取队伍信息
        TeamInfoVo teamInfoVo = teamInfoService.selectTeamInfoById(teamId);
        // 获取成员信息
        UserInfo member = userInfoService.selectUserInfoById(memberId);

        if (!loginUser.getId().equals(teamInfoVo.getLeader())) {
            throw new ServiceException("您不是该队伍的队长，无法邀请成员！");
        }

        // 构建信息
        String message = "您已被邀请加入队长为:" + loginUser.getName()+ "--队伍名称为:" + teamInfoVo.getName() + "的团队，请尽快确认！";

        try {
            // 发送邮件
            emailService.sendEmail(member.getEmail(), "团队邀请通知", message);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("发送邀请邮件失败!");
        }

        return R.ok("邀请邮件发送成功!!");
    }

    /**
     * 成员请求加入
     * @param teamId 队伍ID
     * @return
     */
    @ApiOperation("成员请求加入")
    @PostMapping("/requestJoin")
    public R<String> requestJoin(@RequestParam Long teamId) {
        // 获取当前登录用户信息
        UserInfo loginUser = userInfoService.getLoginUser();
        // 获取队伍信息
        TeamInfoVo teamInfoVo = teamInfoService.selectTeamInfoById(teamId);
        // 获取队长信息
        UserInfo teamLeader = userInfoService.selectUserInfoById(teamInfoVo.getLeader());

        // 构建信息
        String message = loginUser.getName() + "用户请求加入队伍名称为：" + teamInfoVo.getName() + "请尽快确认！";

        try {
            // 发送邮件
            emailService.sendEmail(teamLeader.getEmail(), "成员请求加入通知", message);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("发送请求邮件失败!");
        }

        return R.ok("请求邮件发送成功!!");
    }

}
