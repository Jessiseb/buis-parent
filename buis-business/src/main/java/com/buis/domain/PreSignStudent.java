package com.buis.domain;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 学生预报名对象 pre_sign_student
 *
 * @author buis
 * @date 2024-10-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreSignStudent extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @Excel(name = "id")
    private Long id;

    /**
     * 学生
     */
    @Excel(name = "学生")
    @ApiModelProperty("学生id")
    private Long student;

    /**
     * 比赛
     */
    @Excel(name = "比赛")
    @ApiModelProperty("比赛id")
    private Long race;

    /**
     * 预报名时间
     */
    @ApiModelProperty("预报名时间")
    private Date updateTime;

    /**
     * 标识是否被删除，0表示未删除
     */
    @ApiModelProperty("标识是否被删除，0表示未删除")
    private Integer isDeleted;


}
