package com.buis.service;

public interface IEmailService {

    void sendEmail(String addressee,String subject,String content);

    /**
     * 邀请成员后发送邮箱通知
     * @param teamId
     * @param userId
     * @return
     */
    boolean inviteMember(Long teamId,long userId);


    /**
     * 请求加入队伍后发送邮箱通知
     * @param teamId
     * @return
     */
    boolean requestJoin(Long teamId);



}
