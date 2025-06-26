package com.cmdlee.quizsushi.quiz.util;

import com.cmdlee.quizsushi.global.util.PasswordHasher;
import com.cmdlee.quizsushi.minio.service.MinioService;
import com.cmdlee.quizsushi.quiz.dto.QuestionCreationData;
import com.cmdlee.quizsushi.quiz.dto.QuizCreationData;
import com.cmdlee.quizsushi.quiz.dto.request.CreateQuizRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataProcessor {
    private final MinioService minioService;
    private final PasswordHasher passwordHasher;


    public QuizCreationData process(CreateQuizRequest request, Long quizId) {
        List<QuestionCreationData> processedQuestions = request.getQuestions().stream()
                .map(q -> new QuestionCreationData(
                        q.getNo(),
                        q.getType(),
                        request.isUseSubject() ? q.getSubject() : "",
                        minioService.rewriteTempMediaLinks(q.getQuestion(), quizId),
                        q.getOptions().stream()
                                .map(opt -> minioService.rewriteTempMediaLinks(opt, quizId))
                                .toList(),
                        q.getCorrectAnswer(),
                        minioService.rewriteTempMediaLinks(q.getCorrectAnswerText(), quizId),
                        minioService.rewriteTempMediaLinks(q.getExplanation(), quizId)
                ))
                .toList();

        return new QuizCreationData(
                request.getAuthorName(),
                passwordHasher.hash(request.getPassword()),
                request.getTitle(),
                request.getDescription(),
                request.isUseSubject(),
                processedQuestions
        );
    }

}
