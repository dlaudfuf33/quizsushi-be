package com.cmdlee.quizsushi.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StatRawResponse {

    private final String type;
    private final LocalDateTime timestamp;
    private final Long count;
}
