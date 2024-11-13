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
     * 专业名称
     */
    @ApiModelProperty(value = "专业的名称")
    private String majorName;

    /**
     * 指导队伍数
     */
    @ApiModelProperty(value = "老师指导的队伍数量-仅老师有此字段")
    private Long teamNum = 0L;

    /**
     * 是否为老师标识 0 - 学生 1 - 老师
     */
    @ApiModelProperty("是否为老师标识 0 - 学生 1 - 老师")
    private Integer isTeacher;


    /**
     * 老师职称
     */
    @ApiModelProperty("老师职称")
    @Excel(name = "老师职称")
    private Long title;

}
