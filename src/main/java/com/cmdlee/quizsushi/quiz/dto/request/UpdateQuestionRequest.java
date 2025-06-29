package com.cmdlee.quizsushi.quiz.dto.request;

import com.cmdlee.quizsushi.quiz.domain.model.enums.QuestionType;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdateQuestionRequest {
    private Integer no;
    private QuestionType type;
    private String subject;
    private String question;
    private List<String> options;
    private List<Integer> correctAnswer;
    private String correctAnswerText;
    private String explanation;
}
