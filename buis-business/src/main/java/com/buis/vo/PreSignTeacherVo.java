package com.buis.vo;

import com.buis.domain.PreSignTeacher;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PreSignTeacherVo extends PreSignTeacher {

    /**
     * 老师所属学院
     */
    @ApiModelProperty(value = "老师所属学院的名称")
    private String collegeName;

    /**
     * 专业名
     */
    @ApiModelProperty(value = "老师所教授的专业名称")
    private String majorName;

    /**
     * 老师邮箱
     */
    @ApiModelProperty(value = "老师的邮箱地址")
    private String teacherEmail;

    /**
     * 老师电话
     */
    @ApiModelProperty(value = "老师的联系电话")
    private String teacherPhone;

    /**
     * 老师名
     */
    @ApiModelProperty(value = "老师的姓名")
    private String teacherName;

    /**
     * 比赛名
     */
    @ApiModelProperty(value = "所属比赛的名称")
    private String raceName;

    /**
     * 学院id
     */
    @ApiModelProperty(value = "学院的唯一标识符")
    private Integer collegeId;

    /**
     * 专业id
     */
    @ApiModelProperty(value = "专业的唯一标识符")
    private Integer majorId;
}
