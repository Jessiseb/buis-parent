package com.buis.framework.web.exception;

import com.alibaba.druid.wall.violation.ErrorCode;

public class TokenExpiredException extends RuntimeException {
    private int code;
    public TokenExpiredException(String message, int code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
