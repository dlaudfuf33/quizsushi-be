package com.cmdlee.quizsushi.member.dto.request;


import com.cmdlee.quizsushi.admin.domain.model.enums.ReportTargetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReportRequest {
    @NotNull
    private ReportTargetType targetType;

    @NotNull
    private Long targetId;

    @NotBlank
    private String title;

    @NotBlank
    private String message;
}