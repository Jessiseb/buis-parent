package com.buis.domain.vo;

import lombok.Data;

/**
 * 队伍信息表返回队伍成员id和名称
 */
@Data
public class MemberNameWithId {
    private Long id;
    private String name;
}
