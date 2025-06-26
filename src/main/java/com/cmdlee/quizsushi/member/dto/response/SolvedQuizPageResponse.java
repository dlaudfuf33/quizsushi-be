package com.cmdlee.quizsushi.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SolvedQuizResponse {
    private List<SolvedQuizResponse> quizzes;
    private long totalElements;
    private int totalPages;
    private int currentPage;


}