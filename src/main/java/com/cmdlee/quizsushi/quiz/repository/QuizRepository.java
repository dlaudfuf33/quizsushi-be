package com.cmdlee.quizsushi.quiz.domain.repository;

import com.cmdlee.quizsushi.quiz.domain.model.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @EntityGraph(attributePaths = {"category"})
    Page<Quiz> findAllByCategoryId(Long categoryId, Pageable pageable);

    @EntityGraph(attributePaths = {"category"})
    Page<Quiz> findAll(Pageable pageable);

    @Query("""
                SELECT q FROM Quiz q
                JOIN FETCH q.category c
                WHERE q.title LIKE :query
                  AND c.id = :categoryId
            """)
    Page<Quiz> searchQuizTitleWithCategory(
            @Param("query") String query,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

    @Query("""
            SELECT q FROM Quiz q
            JOIN FETCH q.category c
            WHERE q.title = :query
            """)
    Page<Quiz> searchQuizTitleNoCategory(
            @Param("query") String query,
            Pageable pageable
    );

    @Query("""
                SELECT q FROM Quiz q
                JOIN FETCH q.category c
                WHERE q.authorName LIKE :query
                  AND c.id = :categoryId
            """)
    Page<Quiz> searchQuizAuthorNameWithCategory(
            @Param("query") String query,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

    @Query("""
            SELECT q FROM Quiz q
            JOIN FETCH q.category c
            WHERE q.authorName = :query
            """)
    Page<Quiz> searchQuizAuthorNameNoCategory(
            @Param("query") String query,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"category", "questions"})
    @Query("SELECT q FROM Quiz q WHERE q.id = :id")
    Optional<Quiz> findQuizDetailById(@Param("id") Long id);


    @Modifying
    @Query("UPDATE Quiz q SET q.viewCount = q.viewCount + 1 WHERE q.id = :id")
    void increaseViewCount(@Param("id") Long id);

    @Query("SELECT q FROM Quiz q LEFT JOIN FETCH q.quizRatings c WHERE q.id = :quizId")
    Optional<Quiz> findByIdWithRating(@Param("quizId") Long quizId);
}
