package com.cmdlee.quizsushi.quiz.domain.factory;

import com.cmdlee.quizsushi.quiz.domain.model.Question;
import com.cmdlee.quizsushi.quiz.domain.model.Quiz;
import com.cmdlee.quizsushi.quiz.dto.QuestionCreationData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuizQuestionFactory implements QuestionFactory {

    @Override
    public Question create(QuestionCreationData data, Quiz quiz) {
        return Question.builder()
                .no(data.getNo())
                .type(data.getType())
                .subject(data.getSubject())
                .questionText(data.getQuestionText())
                .options(data.getOptions())
                .correctIndexes(data.getCorrectIndexes())
                .correctAnswerText(data.getCorrectAnswerText())
                .explanation(data.getExplanation())
                .quiz(quiz)
                .build();
    }

}
