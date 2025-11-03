package com.buis.vo;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import com.buis.domain.UserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
