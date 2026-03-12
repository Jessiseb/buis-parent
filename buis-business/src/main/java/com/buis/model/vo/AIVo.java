package com.buis.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AIVo {
    private String reasoningContent; //思考内容
    private String finalContent; //回复内容


}
