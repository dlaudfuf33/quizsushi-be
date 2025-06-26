package com.cmdlee.quizsushi.service;

import com.cmdlee.quizsushi.global.security.BCryptPasswordHasher;
import com.cmdlee.quizsushi.domain.dto.request.CreateQuestionRequest;
import com.cmdlee.quizsushi.domain.dto.request.CreateQuizRequest;
import com.cmdlee.quizsushi.domain.dto.request.DeleteQuizRequest;
import com.cmdlee.quizsushi.domain.dto.request.UpdateQuizRequest;
import com.cmdlee.quizsushi.domain.dto.response.QuizDetailResponse;
import com.cmdlee.quizsushi.domain.dto.response.QuizSummaryResponse;
import com.cmdlee.quizsushi.domain.model.Category;
import com.cmdlee.quizsushi.domain.model.Enum.QuestionType;
import com.cmdlee.quizsushi.domain.model.Question;
import com.cmdlee.quizsushi.domain.model.Quiz;
import com.cmdlee.quizsushi.domain.repository.CategoryRepository;
import com.cmdlee.quizsushi.domain.repository.QuizRepository;
import com.cmdlee.quizsushi.exception.ErrorCode;
import com.cmdlee.quizsushi.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final CategoryRepository categoryRepository;
    private final BCryptPasswordHasher bCryptPasswordHasher;

    public List<QuizSummaryResponse> findAllQuizSummary() {
        return quizRepository.findAllQuizSummary().stream()
                .map(QuizSummaryResponse::from)
                .toList();
    }


    @Transactional
    public Long createQuiz(CreateQuizRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new GlobalException(ErrorCode.ENTITY_NOT_FOUND));
        String hashedPasswd = bCryptPasswordHasher.hash(request.getPassword());
        Quiz quiz = Quiz.builder()
                .authorName(request.getAuthorName())
                .password(hashedPasswd)
                .title(request.getTitle())
                .category(category)
                .description(request.getDescription())
                .useSubject(request.isUseSubject())
                .build();

        for (CreateQuestionRequest q : request.getQuestions()) {
            String subject = request.isUseSubject() ? q.getSubject() : "";

            Question question = Question.builder()
                    .no(q.getNo())
                    .type(QuestionType.valueOf(q.getType()))
                    .subject(subject)
                    .questionText(q.getQuestion())
                    .options(q.getOptions())
                    .correctIdx(q.getCorrectAnswer())
                    .correctAnswerText(q.getCorrectAnswerText())
                    .explanation(q.getExplanation())
                    .quiz(quiz)
                    .build();

            quiz.addQuestion(question);
        }
        quizRepository.save(quiz);

        return quiz.getId();
    }

    public QuizDetailResponse getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(
                () -> new GlobalException(ErrorCode.ENTITY_NOT_FOUND)
        );
        return QuizDetailResponse.from(quiz);
    }

    @Transactional
    public void deleteQuiz(Long quizId, DeleteQuizRequest request) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new GlobalException(ErrorCode.ENTITY_NOT_FOUND));
        if (!bCryptPasswordHasher.matches(request.getPassword(), quiz.getPassword())) {
            throw new GlobalException(ErrorCode.INVALID_PASSWORD);
        }
        quizRepository.delete(quiz);
    }

    @Transactional
    public Long updateQuizById(UpdateQuizRequest request) {
        Quiz quiz = quizRepository.findById(request.getId())
                .orElseThrow(() -> new GlobalException(ErrorCode.ENTITY_NOT_FOUND));
        if (bCryptPasswordHasher.matches(request.getPassword(), quiz.getPassword())) {
            quiz.updateQuiz(request);
        } else {
            throw new GlobalException(ErrorCode.INVALID_PASSWORD);
        }
        return quiz.getId();
    }
}
