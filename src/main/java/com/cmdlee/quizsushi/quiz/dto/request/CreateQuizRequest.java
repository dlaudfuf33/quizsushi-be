package com.cmdlee.quizsushi.domain.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateQuizRequest {
    private String authorName;
    private String password;
    private String title;
    private Long categoryId;
    private String description;
    private boolean useSubject;
    private List<CreateQuestionRequest> questions;
}
