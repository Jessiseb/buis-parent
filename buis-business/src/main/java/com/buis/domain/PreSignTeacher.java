package com.buis.domain;

        import com.buis.common.annotation.Excel;
            import com.buis.common.core.domain.BaseEntity;
    import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 老师预报名对象 pre_sign_teacher
 *
 * @author buis
 * @date 2024-10-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreSignTeacher extends BaseEntity
        {   private static final long serialVersionUID = 1L;
        /** id */
                    @Excel(name = "id")
    private Long id;

        /** 老师 */
                    @Excel(name = "老师")
    private Long teacher;

        /** 比赛 */
                    @Excel(name = "比赛")
    private Long race;

        /** 附件信息 */
                    @Excel(name = "附件信息")
    private String fileUrl;

        /** 材料审核状态 */
                    @Excel(name = "材料审核状态")
    private Long status;

        /** 不合格原因 */
                    @Excel(name = "不合格原因")
    private String reason;

        /** 标识是否被删除，0表示未删除 */
            private Integer isDeleted;

        


        }
