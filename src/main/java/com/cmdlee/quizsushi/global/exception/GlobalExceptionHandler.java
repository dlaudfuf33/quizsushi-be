package com.cmdlee.quizsushi.global.exception;

import com.cmdlee.quizsushi.global.dto.CommonApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(GlobalException.class)
    public Object handleGlobal(GlobalException ex,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        ErrorCode code = ex.getErrorCode();
        log.warn("[GlobalException] {} - {}", code.getCode(), code.getMessage());
        if (request.getRequestURI().contains("/auth/google/callback")) {
            String redirectUrl = switch (code) {
                case BANNED_MEMBER -> "/login?error=banned";
                default -> "/login?error=oauth_failed";
            };

            response.sendRedirect(redirectUrl);
            return null;
        }

        return ResponseEntity
                .status(code.getStatus())
                .body(CommonApiResponse.error(code));
    }

}