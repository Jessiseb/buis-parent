package com.buis.vo;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import com.buis.domain.RaceLanqiaoStudent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 蓝桥杯学生报名信息对象 race_lanqiao_student
 *
 * @author buis
 * @date 2024-10-24
 */
@Data
@ApiModel("蓝桥杯学生报名信息Vo")
public class RaceLanqiaoStudentVo extends RaceLanqiaoStudent {

    @ApiModelProperty("比赛名字")
    private String raceName;

    @ApiModelProperty("赛道父类别名字")

    private String parentName;

    @ApiModelProperty("赛道子类别名字")
    private String sonName;
}
