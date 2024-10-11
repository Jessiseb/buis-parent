package com.buis.domain;

        import java.math.BigDecimal;
    import com.buis.common.annotation.Excel;
            import com.buis.common.core.domain.BaseEntity;
    import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 评分管理对象 t_score
 *
 * @author buis
 * @date 2024-10-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Score extends BaseEntity
        {   private static final long serialVersionUID = 1L;
        /** id */
                    @Excel(name = "id")
    private Long id;

        /** 比赛 */
                    @Excel(name = "比赛")
    private Long race;

        /** 评委 */
                    @Excel(name = "评委")
    private Long teacher;

        /** 队伍 */
                    @Excel(name = "队伍")
    private Long team;

        /** 分数构成 */
                    @Excel(name = "分数构成")
    private String build;

        /** 最终分数 */
                    @Excel(name = "最终分数")
    private BigDecimal score;

        /** 标识是否被删除，0表示未删除 */
            private Integer isDeleted;

        


        }
