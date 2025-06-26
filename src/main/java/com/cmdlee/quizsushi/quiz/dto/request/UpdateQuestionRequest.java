package com.cmdlee.quizsushi.quiz.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateQuestionRequest {
    private Integer no;
    private String type;
    private String subject;
    private String question;
    private List<String> options;
    private List<Integer> correctAnswer;
    private String correctAnswerText;
    private String explanation;
}
