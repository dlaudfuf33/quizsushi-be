package com.cmdlee.quizsushi.global.infra.redis;

import lombok.*;

import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenData {
    private String token;
    private String userId;
    private String ip;
    private String userAgent;
    private Instant createdAt;

}