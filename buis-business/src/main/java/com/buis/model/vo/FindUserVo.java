package com.buis.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("找回密码信息VO")
public class FindUserVo {
    //邮箱
    private String email;
    //验证码
    private String code;
    //新密码
    private String newPassword;
    //确认密码
    private String checkPassword;
}
