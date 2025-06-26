package com.cmdlee.quizsushi.member.dto.response;


import com.cmdlee.quizsushi.quiz.domain.model.Quiz;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreatedQuizResponse {
    private Long id;
    private String title;
    private String category;
    private int questions;
    private long solvedCount;
    private double rating;
    private long ratingCount;
    private String createdAt;

    public static CreatedQuizResponse from(Quiz quiz) {
        return CreatedQuizResponse.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .category(quiz.getCategory().getTitle())
                .questions(quiz.getQuestions().size())
                .solvedCount(quiz.getSolveCount())
                .rating(quiz.getRating())
                .ratingCount(quiz.getRatingCount())
                .createdAt(quiz.getCreatedAt().toLocalDate().toString())
                .build();
    }
}
