package com.cmdlee.quizsushi.admin.dto.response;

import com.cmdlee.quizsushi.report.model.Report;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReportResponse {
    private Long id;
    private String reason;
    private String email;
    private String nickname;
    private String title;
    private boolean isRead;
    private String targetType;
    private Long targetId;
    private LocalDateTime createdAt;

    public static ReportResponse from(Report report) {
        String nickName = report.getReporter().getNickname();
        String email = report.getReporter().getEmail();

        return ReportResponse.builder()
                .id(report.getId())
                .reason(report.getReason())
                .email(email)
                .nickname(nickName)
                .title(report.getTitle())
                .isRead(report.isRead())
                .targetType(report.getTargetType() != null ? report.getTargetType().name() : null)
                .targetId(report.getTargetId())
                .createdAt(report.getCreatedAt())
                .build();
    }
}
