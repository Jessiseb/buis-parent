package com.buis.model.vo;

import com.buis.common.annotation.Excel;
import com.buis.model.domain.UserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户信息对象 user_info
 *
 * @author buis
 * @date 2024-10-11
 */
@Data
@ApiModel("用户信息VO")
public class UserInfoVo extends UserInfo {

    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 学号或工号
     */
    @ApiModelProperty("学号或工号")
    private String number;



    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;

    /**
     * 学院名称
     */
    @ApiModelProperty(value = "学院的名称")
    private String collegeName;

    /**
     * 年级
     */
    @ApiModelProperty(value = "年级")
    private String stuGrade;

    /**
     * 专业名称
     */
    @ApiModelProperty(value = "专业的名称")
    private String majorName;

    /**
     * 班级
     */
    @ApiModelProperty(value = "班级")
    private String stuClass;

    /**
     * 指导队伍数
     */
    @ApiModelProperty(value = "老师指导的队伍数量-仅老师有此字段")
    private Long teamNum = 0L;

}
