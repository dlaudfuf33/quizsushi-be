package com.cmdlee.quizsushi.admin.dto.response;

import com.cmdlee.quizsushi.report.model.Report;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ReportDetailResponse {
    private Long id;
    private String reason;
    private String title;
    private String message;
    private String nickname;
    private String email;
    private boolean isRead;
    private LocalDateTime createdAt;

    private String targetType;
    private Long targetId;

    private ReportTargetRsponse target;

    public static ReportDetailResponse of(Report report, ReportTargetRsponse targetResponse) {
        return ReportDetailResponse.builder()
                .id(report.getId())
                .type(report.getType().name())
                .title(report.getTitle())
                .message(report.getMessage())
                .nickname(report.getMember() != null ? report.getMember().getNickname() : "-")
                .email(report.getEmail() != null ? report.getEmail() : "-")
                .isRead(report.isRead())
                .createdAt(report.getCreatedAt())
                .targetType(report.getTargetType() != null ? report.getTargetType().name() : null)
                .targetId(report.getTargetId())
                .target(targetResponse)
                .build();
    }
}
