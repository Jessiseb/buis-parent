package com.buis.domain;

        import com.buis.common.annotation.Excel;
            import com.buis.common.core.domain.BaseEntity;
    import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 用户信息对象 user_info
 *
 * @author buis
 * @date 2024-10-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo extends BaseEntity
        {   private static final long serialVersionUID = 1L;
        /** id */
                    @Excel(name = "id")
    private Long id;

        /** 学号或工号 */
                    @Excel(name = "学号或工号")
    private String number;

        /** 密码 */
                    @Excel(name = "密码")
    private String password;

        /** 邮箱 */
                    @Excel(name = "邮箱")
    private String email;

        /** 手机号 */
                    @Excel(name = "手机号")
    private String phone;

        /** 姓名 */
                    @Excel(name = "姓名")
    private String name;

        /** 个人简介 */
                    @Excel(name = "个人简介")
    private String description;

        /** 用户性别 */
                    @Excel(name = "用户性别")
    private Long sex;

        /** 学院 */
                    @Excel(name = "学院")
    private Long college;

        /** 专业 */
                    @Excel(name = "专业")
    private Long major;

        /** 老师职称 */
                    @Excel(name = "老师职称")
    private Long title;

        /** 禁用状态 */
                    @Excel(name = "禁用状态")
    private Long isDisabled;

        /** 标识是否被删除，0表示未删除 */
            private Integer isDeleted;

        


        }
