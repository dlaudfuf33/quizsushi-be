package com.cmdlee.quizsushi.quiz.domain.factory;

import com.cmdlee.quizsushi.quiz.domain.model.Question;
import com.cmdlee.quizsushi.quiz.domain.model.Quiz;
import com.cmdlee.quizsushi.quiz.dto.QuestionCreationData;

import java.util.List;

public interface QuestionFactory {
    Question create(QuestionCreationData data, Quiz quiz);

    default List<Question> createAll(List<QuestionCreationData> dataList, Quiz quiz) {
        return dataList.stream()
                .map(data -> create(data, quiz))
                .toList();
    }

}
