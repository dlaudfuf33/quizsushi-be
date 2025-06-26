package com.cmdlee.quizsushi.domain.dto.response;

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
public class QuestionResponse {
    private long id;
    private Integer no;
    private String type;
    private String subject;
    private String question;
    private List<String> options;
    private Integer correctAnswer;
    private String correctAnswerText;
    private String explanation;

    public static QuestionResponse from(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
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
