package com.buis.vo;

import com.buis.common.annotation.Excel;
import com.buis.common.core.domain.BaseEntity;
import com.buis.domain.RaceInfo;
import com.buis.domain.Reward;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 比赛信息对象 race_info
 *
 * @author kaven
 * @date 2024-10-23
 */
@Data
@ApiModel("比赛信息")
public class RaceInfoVo extends RaceInfo
        {
            /**
             * 类型名称
             */
            @Excel(name = "一级类型名称")
            @ApiModelProperty(value = "一级类型名称")
            private String parentName;
            /**
             * 类型名称
             */
            @Excel(name = "二级类型名称")
            @ApiModelProperty(value = "二级类型名称")
            private String sonName;

        }
