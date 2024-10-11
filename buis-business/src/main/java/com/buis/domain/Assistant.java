package com.buis.domain;

        import com.buis.common.annotation.Excel;
            import com.buis.common.core.domain.BaseEntity;
    import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 学生助理对象 t_assistant
 *
 * @author buis
 * @date 2024-10-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assistant extends BaseEntity
        {   private static final long serialVersionUID = 1L;
        /** id */
                    @Excel(name = "id")
    private Long id;

        /** 学生助理 */
                    @Excel(name = "学生助理")
    private Long student;

        /** 比赛 */
                    @Excel(name = "比赛")
    private Long race;

        /** 标识是否被删除，0表示未删除 */
            private Integer isDeleted;

        


        }
