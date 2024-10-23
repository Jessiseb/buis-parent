package com.buis.domain;

        import com.buis.common.annotation.Excel;
            import com.buis.common.core.domain.BaseEntity;
    import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 蓝桥杯老师报名信息对象 race_lanqiao_teacher
 *
 * @author buis
 * @date 2024-10-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RaceLanqiaoTeacher extends BaseEntity
        {   private static final long serialVersionUID = 1L;
        /** id */
                    @Excel(name = "id")
    private Long id;

        /** 姓名 */
                    @Excel(name = "姓名")
    private String name;

        /** 性别（字典） */
                    @Excel(name = "性别", readConverterExp = "字=典")
    private Long sex;

        /** 部门 */
                    @Excel(name = "部门")
    private Long college;

        /** 所在系 */
                    @Excel(name = "所在系")
    private Long major;

        /** 职称（字典） */
                    @Excel(name = "职称", readConverterExp = "字=典")
    private Long title;

        /** 职务 */
                    @Excel(name = "职务")
    private String office;

        /** 手机号 */
                    @Excel(name = "手机号")
    private String phone;

        /** 邮箱 */
                    @Excel(name = "邮箱")
    private String email;

        /** 身份证号码 */
                    @Excel(name = "身份证号码")
    private String identity;

        /** 主授课程 */
                    @Excel(name = "主授课程")
    private String course;

        /** 指导科目 */
                    @Excel(name = "指导科目")
    private Long type;

        /** 本学期任课班级 */
                    @Excel(name = "本学期任课班级")
    private String tClass;

        /** 中国建设银行银行卡账号 */
                    @Excel(name = "中国建设银行银行卡账号")
    private String bankNumber;

        /** 指导经验 */
                    @Excel(name = "指导经验")
    private String teacher3;

            /** 标识是否被删除，0表示未删除 */
            private Integer isDeleted;

        
        }
