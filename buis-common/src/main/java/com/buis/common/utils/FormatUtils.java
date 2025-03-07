package com.buis.common.utils;

import com.buis.common.exception.ServiceException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtils {
    public static boolean checkEmailFormat(String email){
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Matcher matcher = Pattern.compile(emailRegex).matcher(email);
        if (!matcher.find()) {
            throw new ServiceException("邮箱格式有误,请重新输入!");
        }
        return true;
    }
}
