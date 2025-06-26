package com.cmdlee.quizsushi.domain.dto.request;

import com.cmdlee.quizsushi.domain.model.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateQuestionRequest {
    private Long id;
    private Integer no;
    private String type;
    private String subject;
    private String question;
    private List<String> options;
    private Integer correctAnswer;
    private String correctAnswerText;
    private String explanation;

    public static UpdateQuestionRequest from(Question question) {
        return UpdateQuestionRequest.builder()
                .no(question.getNo())
                .type(question.getType().toString())
                .subject(question.getSubject())
                .question(question.getQuestionText())
                .options(question.getOptions())
                .correctAnswer(question.getCorrectIdx())
                .correctAnswerText(question.getCorrectAnswerText())
                .explanation(question.getExplanation())
                .build();
    }

}
