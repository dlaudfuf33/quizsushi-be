package com.cmdlee.quizsushi.quiz.dto.request;

import com.cmdlee.quizsushi.quiz.domain.model.Question;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateQuestionRequest {
    private Integer no;
    private String type;
    private String subject;
    private String question;
    private List<String> options;
    private List<Integer> correctAnswer;
    private String correctAnswerText;
    private String explanation;

    public static CreateQuestionRequest from(Question question) {
        ObjectMapper mapper = new ObjectMapper();
        List<String> options = List.of();
        List<Integer> correctAnswer = List.of();

        try {
            if (question.getOptions() != null) {
                options = mapper.readValue(question.getOptions(), new TypeReference<>() {
                });
            }
            if (question.getCorrectIndexes() != null) {
                correctAnswer = mapper.readValue(question.getCorrectIndexes(), new TypeReference<>() {
                });
            }
        } catch (Exception e) {
            throw new RuntimeException("JSON parsing error", e);
        }
        return CreateQuestionRequest.builder()
                .no(question.getNo())
                .type(question.getType().toString())
                .subject(question.getSubject())
                .question(question.getQuestionText())
//                .options(question.getOptions())
//                .correctAnswer(question.getCorrectIndexes())
                .options(options)
                .correctAnswer(correctAnswer)
                .correctAnswerText(question.getCorrectAnswerText())
                .explanation(question.getExplanation())
                .build();
    }

}
