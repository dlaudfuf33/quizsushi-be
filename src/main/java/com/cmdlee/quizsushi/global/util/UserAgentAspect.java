package com.cmdlee.quizsushi.global.tmp.util;

import com.cmdlee.quizsushi.global.tmp.exception.ErrorCode;
import com.cmdlee.quizsushi.global.tmp.exception.GlobalException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class UserAgentAspect {

    private final HttpServletRequest request;

    @Before("@annotation(com.cmdlee.quizsushi.global.tmp.util.RejectBot)")
    public void checkUserAgent() {
        String ua = Optional.ofNullable(request.getHeader("User-Agent")).orElse("").toLowerCase();
        List<String> blocked = List.of("curl", "python", "postman", "node", "bot");

        for (String agent : blocked) {
            if (ua.contains(agent)) {
                throw new GlobalException(ErrorCode.FORBIDDEN_REQUEST);
            }
        }
    }
}