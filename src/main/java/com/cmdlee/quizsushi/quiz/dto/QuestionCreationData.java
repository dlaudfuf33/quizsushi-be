package com.cmdlee.quizsushi.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@AllArgsConstructor
@Getter
public class QuestionCreationData {
    Integer no;
    String type;
    String subject;
    String questionText;
    List<String> options;
    List<Integer> correctIndexes;
    String correctAnswerText;
    String explanation;
}
