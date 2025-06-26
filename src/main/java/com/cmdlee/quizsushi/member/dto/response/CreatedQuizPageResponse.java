package com.cmdlee.quizsushi.member.dto.response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CreatedQuizPageResponse {
    private List<CreatedQuizResponse> quizzes;
    private int currentPage;
    private int totalPages;
    private long totalElements;
}