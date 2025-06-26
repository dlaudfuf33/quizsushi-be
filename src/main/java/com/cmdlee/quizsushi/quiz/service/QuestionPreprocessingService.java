package com.cmdlee.quizsushi.quiz.service;

import com.cmdlee.quizsushi.minio.service.MinioService;
import com.cmdlee.quizsushi.quiz.dto.QuestionCreationData;
import com.cmdlee.quizsushi.quiz.dto.request.CreateQuestionRequest;
import com.cmdlee.quizsushi.quiz.dto.request.UpdateQuestionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionPreprocessingService {
    private final MinioService minioService;

    public QuestionCreationData process(CreateQuestionRequest request, boolean useSubject, String mediaKey) {
        return new QuestionCreationData(
                request.getNo(),
                request.getType(),
                useSubject ? request.getSubject() : "",
                minioService.rewriteTempMediaLinks(request.getQuestion(), mediaKey),
                request.getOptions().stream()
                        .map(opt -> minioService.rewriteTempMediaLinks(opt, mediaKey))
                        .toList(),
                request.getCorrectAnswer(),
                minioService.rewriteTempMediaLinks(request.getCorrectAnswerText(), mediaKey),
                minioService.rewriteTempMediaLinks(request.getExplanation(), mediaKey)
        );
    }


    public List<QuestionCreationData> processForCreate(List<CreateQuestionRequest> requests, boolean useSubject, String mediaKey) {
        return requests.stream()
                .map(q -> process(q, useSubject, mediaKey))
                .toList();
    }

    public QuestionCreationData process(UpdateQuestionRequest request, boolean useSubject, String mediaKey) {
        return new QuestionCreationData(
                request.getNo(),
                request.getType(),
                useSubject ? request.getSubject() : "",
                minioService.rewriteTempMediaLinks(request.getQuestion(), mediaKey),
                request.getOptions().stream()
                        .map(opt -> minioService.rewriteTempMediaLinks(opt, mediaKey))
                        .toList(),
                request.getCorrectAnswer(),
                minioService.rewriteTempMediaLinks(request.getCorrectAnswerText(), mediaKey),
                minioService.rewriteTempMediaLinks(request.getExplanation(), mediaKey)
        );
    }

    public List<QuestionCreationData> processForUpdate(List<UpdateQuestionRequest> requests, boolean useSubject, String mediaKey) {
        return requests.stream()
                .map(q -> process(q, useSubject, mediaKey))
                .toList();
    }
}