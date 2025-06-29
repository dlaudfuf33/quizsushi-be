package com.cmdlee.quizsushi.quiz.dto;

import com.cmdlee.quizsushi.quiz.domain.model.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@Getter
@AllArgsConstructor
public class QuestionCreationData {
    Integer no;
    QuestionType type;
    String subject;
    String questionText;
    List<String> options;
    List<Integer> correctIndexes;
    String correctAnswerText;
    String explanation;
}
