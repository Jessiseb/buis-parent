package com.buis.vo;

import com.buis.bo.MemberInfoBO;
import com.buis.domain.TeamInfo;
import lombok.Data;

import java.util.List;

@Data
public class TeamInfoVo extends TeamInfo {
//    private UserInfo userInfo;
    //队长名
    private  String LeaderName;
    //队长手机号
    private String leaderPhone;
    //队长邮箱
    private String leaderEmail;
    //队长简介
    private String leaderDescription;
    //老师职称（判断队长是否为老师）
    private Byte leaderTitle;
    private Integer college;
    private Integer major;
    //比赛名称
    private String raceName;

    //学院名称
    private String collegeName;

    //专业名称
    private String majorName;

    //队伍已有人数
    private Long teamNumbers;

    //队伍已有成员
//    private List<String> teamMemberList;

    private List<MemberInfoBO> teamMemberList;

}
