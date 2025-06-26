package com.cmdlee.quizsushi.member.dto.response;

import com.cmdlee.quizsushi.quiz.domain.model.QuizSolveLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SolvedQuizDto {
    private Long id;
    private String title;
    private Integer score;
    private String date;
    private String category;

    public static SolvedQuizDto from(QuizSolveLog log) {
        return SolvedQuizDto.builder()
                .id(log.getQuiz().getId())
                .title(log.getQuiz().getTitle())
                .score(log.getScore())
                .date(log.getSubmittedAt().toLocalDate().toString())
                .category(log.getQuiz().getCategory().getTitle())
                .build();
    }
}