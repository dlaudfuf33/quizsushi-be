package com.cmdlee.quizsushi.quiz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GenerateQuizRequest {
    private String topic;
    private String description;
    private int count;
    private String difficulty;
    private String questionType;
}