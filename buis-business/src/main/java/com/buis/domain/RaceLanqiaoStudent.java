package com.buis.domain;

        import com.buis.common.annotation.Excel;
            import com.buis.common.core.domain.BaseEntity;
        import io.swagger.annotations.ApiModelProperty;
        import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 蓝桥杯学生报名信息对象 race_lanqiao_student
 *
 * @author buis
 * @date 2024-10-24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RaceLanqiaoStudent extends BaseEntity
        {   private static final long serialVersionUID = 1L;
        /** id */
                    @Excel(name = "id")
    private Long id;

        /** 学生id */
                    @Excel(name = "学生id")
                    @ApiModelProperty("学生id")
    private Long userId;

        /** 姓名 */
                    @Excel(name = "姓名")
                    @ApiModelProperty("姓名")
    private String name;

        /** 学号 */
                    @Excel(name = "学号")
                    @ApiModelProperty(" 学号")
    private String number;

        /** 班级 */
                    @Excel(name = "班级")
                    @ApiModelProperty("班级 ")
    private String sClass;

        /** 手机号 */
                    @Excel(name = "手机号")
                    @ApiModelProperty("手机号 ")
    private String phone;

        /** 邮箱 */
                    @Excel(name = "邮箱")
                    @ApiModelProperty("邮箱 ")
    private String email;

        /** 身份证号码 */
                    @Excel(name = "身份证号码")
                    @ApiModelProperty("身份证号码")
    private String identity;

        /** 赛道父类别 */
                    @Excel(name = "赛道父类别")
                    @ApiModelProperty("赛道父类别")

    private Long parentTid;

        /** 赛道子类别（报名科目） */
                    @Excel(name = "赛道子类别", readConverterExp = "报=名科目")
                    @ApiModelProperty("赛道子类别")
    private Long sonTid;

        /** 第一意愿指导老师 */
                    @Excel(name = "第一意愿指导老师")
                    @ApiModelProperty("第一意愿指导老师")
    private String teacher1;

        /** 第二意愿指导老师 */
                    @Excel(name = "第二意愿指导老师")
                    @ApiModelProperty("第二意愿指导老师")
    private String teacher2;

        /** 第三意愿指导老师 */
                    @Excel(name = "第三意愿指导老师")
                    @ApiModelProperty("第三意愿指导老师")
    private String teacher3;

        /** 银行卡账号 */
                    @Excel(name = "银行卡账号")
                    @ApiModelProperty("银行卡号 ")
    private String bankNumber;

        /** 开户行 */
                    @Excel(name = "开户行")
                    @ApiModelProperty("开户行 ")
    private String bankName;

            /** 标识是否被删除，0表示未删除 */
            private Integer isDeleted;

        
        }
