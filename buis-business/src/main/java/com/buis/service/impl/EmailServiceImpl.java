package com.buis.service.impl;


import com.buis.common.core.domain.R;
import com.buis.common.exception.ServiceException;
import com.buis.domain.UserInfo;
import com.buis.service.IEmailService;
import com.buis.service.ITeamInfoService;
import com.buis.service.IUserInfoService;
import com.buis.vo.TeamInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class EmailServiceImpl implements IEmailService {

    /*
     * springboot 提供的一个发送邮件的简单抽象， 直接注入即可
     */
    @Resource
    private JavaMailSender mailSender;
    @Resource
    private IUserInfoService userInfoService;
    @Resource
    private ITeamInfoService teamInfoService;

    @Value("${spring.mail.from}")
    private String from;

    /**
     *  发送邮件接口
     * @param addressee 收件人
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public void sendEmail(String addressee, String subject, String content) {
        //创建简单邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        //谁发的
        message.setFrom(from);
        //谁要接收
        message.setTo(addressee);
        //邮件标题
        message.setSubject(subject);
        //邮件内容
        message.setText(content);
        // 发送邮件
        mailSender.send(message);

        log.info("邮件发送成功：{}", message);

    }

    @Override
    public boolean inviteMember(Long teamId, long userId) {
        // 获取当前登录用户信息
        UserInfo loginUser = userInfoService.getLoginUser();
        // 获取队伍信息
        TeamInfoVo teamInfoVo = teamInfoService.selectTeamInfoById(teamId);
        // 获取成员信息
        UserInfo member = userInfoService.selectUserInfoById(userId);

//        if (!loginUser.getId().equals(teamInfoVo.getLeader())) {
//            throw new ServiceException("您不是该队伍的队长，无法邀请成员！");
//        }

        // 构建信息
        String message = "团队邀请通知:\n您被邀请加入队长为:" + loginUser.getName()+ "--队伍名称为:" + teamInfoVo.getName() +
                "的团队，请尽快登录官网:race.baiyunsc.cn确认！";

        try {
            // 发送邮件
            this.sendEmail(member.getEmail(), "广东白云学院大数据与计算机学院创新创业中心", message);
            //log打印谁发送给谁
            log.info("邮件发送成功：{}", member.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("邮件发送失败！: ",e);
            throw new ServiceException("邮件发送失败！");
        }

        return true;
    }

    @Override
    public boolean requestJoin(Long teamId) {
        // 获取当前登录用户信息
        UserInfo loginUser = userInfoService.getLoginUser();
        // 获取队伍信息
        TeamInfoVo teamInfoVo = teamInfoService.selectTeamInfoById(teamId);
        // 获取队长信息
        UserInfo teamLeader = userInfoService.selectUserInfoById(teamInfoVo.getLeader());

        // 构建信息
        String message = "请求加入队伍通知:\n"+loginUser.getName() + "用户请求加入队伍名称为：" + teamInfoVo.getName() +
                "请尽快登录官网:race.baiyunsc.cn确认！";

        try {
            // 发送邮件
            this.sendEmail(teamLeader.getEmail(), "广东白云学院大数据与计算机学院创新创业中心", message);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("邮件发送失败！: ",e);
        }
        return true;
    }
}
