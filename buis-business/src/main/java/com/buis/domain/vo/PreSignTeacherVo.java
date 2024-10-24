package com.buis.domain.vo;

import com.buis.domain.PreSignTeacher;
import lombok.Data;

@Data
public class PreSignTeacherVo extends PreSignTeacher {
    //老师所属学院
    private String collegeName;
    //专业名
    private String majorName;
    //老师邮箱
    private  String teacherEmail;
    //老师电话
    private  String teacherPhone;
    //老师名
    private  String teacherName;
    //比赛名
    private  String raceName;
    //学院id
    private Integer collegeId;
    //专业id
    private Integer majorId;
}
