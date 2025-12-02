package com.buis.model.bo;

import lombok.Data;

//团队指导老师信息
@Data
public class TeacherInfoBO {
    private Long id;
    //姓名
    private String name;
    //学院
    private String collegeName;
    //手机话
    private String phone;
    //工号
    private String number;
    //性别
    private int sex;
    //职称
    private int title;
    //邮箱
    private String email;
}
