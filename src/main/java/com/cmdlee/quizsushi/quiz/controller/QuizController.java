package com.cmdlee.quizsushi.controller;

import com.cmdlee.quizsushi.domain.dto.*;
import com.cmdlee.quizsushi.domain.dto.request.CreateQuizRequest;
import com.cmdlee.quizsushi.domain.dto.request.DeleteQuizRequest;
import com.cmdlee.quizsushi.domain.dto.request.GenerateQuizRequest;
import com.cmdlee.quizsushi.domain.dto.request.UpdateQuizRequest;
import com.cmdlee.quizsushi.domain.dto.response.*;
import com.cmdlee.quizsushi.service.AiService;
import com.cmdlee.quizsushi.service.CategoryService;
import com.cmdlee.quizsushi.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quizzes")
@Slf4j
public class QuizController {
    private final CategoryService categoryService;
    private final QuizService quizService;
    private final AiService aiService;

    @GetMapping("/categories/introductions")
    public ResponseEntity<CommonApiResponse<Map<String, Object>>> getIntroductionCategories() {
        List<IntroductionCategoryResponse> introductions = categoryService.findIntroductionCategories();
        Map<String, Object> responseData = Map.of("introductions", introductions);
        return ResponseEntity.ok(CommonApiResponse.ok(responseData, "카테고리 소개 목록 조회 성공"));
    }

    @GetMapping("/categories")
    public ResponseEntity<CommonApiResponse<Map<String, Object>>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.findAll();
        Map<String, Object> responseData = Map.of("categories", categories);
        return ResponseEntity.ok(CommonApiResponse.ok(responseData, "카테고리 목록 조회 성공"));
    }

    @GetMapping
    public ResponseEntity<CommonApiResponse<Map<String, Object>>> findAllQuizSummary() {
        List<QuizSummaryResponse> quizzes = quizService.findAllQuizSummary();
        Map<String, Object> responseData = Map.of("quizzes", quizzes);
        return ResponseEntity.ok(CommonApiResponse.ok(responseData, "카테고리 목록 조회 성공"));
    }

    @PostMapping
    public ResponseEntity<CommonApiResponse<Map<String, Object>>> createQuiz(
            @RequestBody CreateQuizRequest request
    ) {
        Long id = quizService.createQuiz(request);
        return ResponseEntity.ok(CommonApiResponse.ok(Map.of("quizId", id), "퀴즈 생성 성공"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Map<String, Object>>> getQuizById(@PathVariable Long id) {
        QuizDetailResponse quiz = quizService.getQuizById(id);
        return ResponseEntity.ok(CommonApiResponse.ok(Map.of("quiz", quiz), "퀴즈 조회 성공"));
    }

    @PatchMapping
    public ResponseEntity<CommonApiResponse<Map<String, Object>>> updateQuizById(
            @RequestBody UpdateQuizRequest request
    ) {
        long quizId = quizService.updateQuizById(request);
        return ResponseEntity.ok(CommonApiResponse.ok(Map.of("quizId", quizId), "수정 성공"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuiz(
            @PathVariable("id") Long quizId,
            @RequestBody DeleteQuizRequest request) {
        quizService.deleteQuiz(quizId, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/generate")
    public List<GenerateQuizResponse> generateQuizzes(@RequestBody GenerateQuizRequest request) {
        return aiService.generateQuizByAI(request);
    }


}
