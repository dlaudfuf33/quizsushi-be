package com.cmdlee.quizsushi.quiz.dto.response;

import com.cmdlee.quizsushi.quiz.domain.model.Quiz;
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
    private Author author;
    private String title;
    private String description;
    private boolean useSubject;
    private CategoryResponse category;
    private double rating;
    private long ratingCount;
    private List<QuestionResponse> questions;

    public static QuizDetailResponse from(Quiz quiz) {
        Author author = Author.builder()
                .id(quiz.getAuthor().getId().toString())
                .nickName(quiz.getAuthor().getNickname())
                .avatar(quiz.getAuthor().getProfileImage())
                .build();

        List<QuestionResponse> questions = quiz.getQuestions().stream()
                .map(QuestionResponse::from)
                .toList();
        CategoryResponse categoryResponse = CategoryResponse.builder()
                .id(quiz.getCategory().getId())
                .title(quiz.getCategory().getTitle())
                .build();


        return QuizDetailResponse.builder()
                .id(quiz.getId())
                .author(author)
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .useSubject(quiz.isUseSubject())
                .category(categoryResponse)
                .rating(quiz.getRating())
                .ratingCount(quiz.getRatingCount())
                .questions(questions)
                .build();
    }


}