package com.buis.domain;

        import com.buis.common.annotation.Excel;
            import com.buis.common.core.domain.TreeEntity;
    import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 蓝桥杯赛道类型对象 race_lanqiao_type
 *
 * @author buis
 * @date 2024-10-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RaceLanqiaoType extends TreeEntity
        {   private static final long serialVersionUID = 1L;
        /** id */
                    @Excel(name = "id")
    private Long id;

        /** 一级类型或二级类型名称 */
                    @Excel(name = "一级类型或二级类型名称")
    private String name;

            /** 排序号 */
                    @Excel(name = "排序号")
    private Long sort;

        /** 标识是否被删除，0表示未删除 */
            private Integer isDeleted;

        
        }
