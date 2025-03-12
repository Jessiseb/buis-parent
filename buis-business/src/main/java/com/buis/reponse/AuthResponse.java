package com.buis.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor // 生成无参构造函数
public class AuthResponse {
    public String access_token;
    public int expires_in;
    public String error_description;
    public String error;

}