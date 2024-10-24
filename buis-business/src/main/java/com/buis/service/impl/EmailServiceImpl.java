package com.buis.service.impl;


import com.buis.service.IEmailService;
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
}
