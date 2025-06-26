package com.cmdlee.quizsushi.domain.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class UpdateQuizRequest {
    private Long id;
    private String description;
    private String password;
    private boolean useSubject;
    private Set<UpdateQuestionRequest> questions;
}
