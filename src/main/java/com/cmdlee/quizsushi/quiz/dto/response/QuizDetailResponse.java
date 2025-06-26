package com.cmdlee.quizsushi.domain.dto.response;

import com.cmdlee.quizsushi.domain.model.Quiz;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizDetailResponse {
    private Long id;
    private String author;
    private String title;
    private String description;
    private boolean useSubject;
    private CategoryResponse category;
    private double rating;
    private long reviewCount;
    private List<QuestionResponse> questions;

    public static QuizDetailResponse from(Quiz quiz) {
        List<QuestionResponse> questions = quiz.getQuestions().stream()
                .map(QuestionResponse::from)
                .toList();
        CategoryResponse categoryResponse = CategoryResponse.builder()
                .id(quiz.getCategory().getId())
                .title(quiz.getCategory().getTitle())
                .build();

        return QuizDetailResponse.builder()
                .id(quiz.getId())
                .author(quiz.getAuthorName())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .useSubject(quiz.isUseSubject())
                .category(categoryResponse)
                .rating(quiz.getRating())
                .reviewCount(quiz.getReviewCount())
                .questions(questions)
                .build();
    }
}