package com.buis.domain;

        import com.buis.common.annotation.Excel;
            import com.buis.common.core.domain.BaseEntity;
    import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 队伍老师管理对象 team_member_teacher
 *
 * @author buis
 * @date 2024-10-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMemberTeacher extends BaseEntity
        {   private static final long serialVersionUID = 1L;
        /** id */
                    @Excel(name = "id")
    private Long id;

        /** 比赛 */
                    @Excel(name = "比赛")
    private Long race;

        /** 队伍 */
                    @Excel(name = "队伍")
    private Long team;

        /** 老师 */
                    @Excel(name = "老师")
    private Long teacher;

        /** 决定者 */
                    @Excel(name = "决定者")
    private Long decider;

        /** 互选状态 */
                    @Excel(name = "互选状态")
    private Long isChoosed;

        /** 选择原因 */
                    @Excel(name = "选择原因")
    private String reason;

        /** 标识是否被删除，0表示未删除 */
            private Integer isDeleted;

        


        }
