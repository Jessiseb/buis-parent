package com.buis.domain;

        import com.buis.common.annotation.Excel;
            import com.buis.common.core.domain.TreeEntity;
    import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 专业信息对象 t_college_major
 *
 * @author buis
 * @date 2024-10-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollegeMajor extends TreeEntity
        {   private static final long serialVersionUID = 1L;
        /** id */
                    @Excel(name = "id")
    private Long id;

        /** 名称 */
                    @Excel(name = "名称")
    private String name;

        /** 所属学院 */
                    @Excel(name = "所属学院")
    private Long college;

        /** 排序号 */
                    @Excel(name = "排序号")
    private Long sort;

        /** 标识是否被删除，0表示未删除 */
            private Integer isDeleted;

        


        }
