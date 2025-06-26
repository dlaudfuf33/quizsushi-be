package com.cmdlee.quizsushi.domain.dto.response;

import com.cmdlee.quizsushi.domain.model.Quiz;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizSummaryResponse {
    private String id;
    private String title;
    private String author;
    private String category;
    private String categoryId;
    private String categoryIcon;
    private int questionCount;
    private double rating;
    private Long reviews;

    public static QuizSummaryResponse from(Quiz quiz) {
        return QuizSummaryResponse.builder()
                .id(quiz.getId().toString())
                .title(quiz.getTitle())
                .author(quiz.getAuthorName())
                .category(quiz.getCategory().getTitle())
                .categoryId(quiz.getCategory().getId().toString())
                .categoryIcon(quiz.getCategory().getIcon())
                .questionCount(quiz.getQuestions().size())
                .rating(quiz.getRating())
                .reviews(quiz.getReviewCount())
                .build();
    }
}
