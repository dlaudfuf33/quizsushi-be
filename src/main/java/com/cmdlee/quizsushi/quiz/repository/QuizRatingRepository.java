package com.cmdlee.quizsushi.quiz.domain.repository;

import com.cmdlee.quizsushi.quiz.domain.model.Quiz;
import com.cmdlee.quizsushi.quiz.domain.model.QuizRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRatingRepository extends JpaRepository<QuizRating, Long> {

    Optional<QuizRating> findByQuizAndAnonKey(Quiz quiz, String anonKey);
}
