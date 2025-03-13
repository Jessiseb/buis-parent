package com.buis.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreamResponse {
    private String id;
    private String event;  // 可能为"message"或"end"
    private String content;
    private Boolean isEnd;
    private Long created;
}