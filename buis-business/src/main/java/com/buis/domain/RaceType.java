package com.buis.domain;

        import com.buis.common.annotation.Excel;
            import com.buis.common.core.domain.TreeEntity;
    import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 比赛类型对象 race_type
 *
 * @author buis
 * @date 2024-10-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RaceType extends TreeEntity
        {   private static final long serialVersionUID = 1L;
        /** id */
                    @Excel(name = "id")
    private Long id;

        /** 类型名称 */
                    @Excel(name = "类型名称")
    private String name;

            /** 排序号 */
                    @Excel(name = "排序号")
    private Long sort;

        /** 标识是否被删除，0表示未删除 */
            private Integer isDeleted;

        
        }
