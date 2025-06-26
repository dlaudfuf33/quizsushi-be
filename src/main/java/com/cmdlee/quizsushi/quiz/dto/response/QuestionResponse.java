package com.cmdlee.quizsushi.quiz.dto.response;

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
public class QuestionResponse {
    private long id;
    private Integer no;
    private String type;
    private String subject;
    private String question;
    private List<String> options;
    private List<Integer> correctAnswer;
    private String correctAnswerText;
    private String explanation;

    public static QuestionResponse from(Question question) {
        ObjectMapper mapper = new ObjectMapper();
        List<String> options = List.of();
        List<Integer> correctIndexes = List.of();
        try {
            if (question.getOptions() != null) {
                options = mapper.readValue(question.getOptions(), new TypeReference<>() {
                });
            }
            if (question.getCorrectIndexes() != null) {
                correctIndexes = mapper.readValue(question.getCorrectIndexes(), new TypeReference<>() {
                });
            }
        } catch (Exception e) {
            // 로깅 또는 예외 처리
            throw new RuntimeException("JSON parsing failed", e);
        }
        return QuestionResponse.builder()
                .id(question.getId())
                .no(question.getNo())
                .type(question.getType().toString())
                .subject(question.getSubject())
                .question(question.getQuestionText())
//                .options(question.getOptions())
                .options(options)
//                .correctAnswers(question.getCorrectIndexes())
                .correctAnswer(correctIndexes)
                .correctAnswerText(question.getCorrectAnswerText())
                .explanation(question.getExplanation())
                .build();
    }

}
