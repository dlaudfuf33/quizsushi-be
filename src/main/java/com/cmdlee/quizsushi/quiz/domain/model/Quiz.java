package com.cmdlee.quizsushi.quiz.domain.model;

import com.cmdlee.quizsushi.global.exception.ErrorCode;
import com.cmdlee.quizsushi.global.exception.GlobalException;
import com.cmdlee.quizsushi.member.domain.model.QuizsushiMember;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class Quiz extends TimeBaseEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "quiz_seq"
    )
    @SequenceGenerator(
            name = "quiz_seq",
            sequenceName = "quiz_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false)
    private QuizsushiMember author;

    @Column(nullable = false, length = 200)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private boolean useSubject;

    @Column(nullable = false)
    private String mediaKey;

    @Column(nullable = false)
    private int questionCount;

    @Column(nullable = false)
    private double rating = 0.0;

    @Column(nullable = false)
    private Long ratingCount = 0L;

    @Column(nullable = false)
    private Long viewCount = 0L;

    @Column(nullable = false)
    private Long solveCount = 0L;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<QuizRating> quizRatings = new HashSet<>();

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 50)
    private List<Question> questions = new ArrayList<>();

    @Builder
    public Quiz(QuizsushiMember author, String title, Category category, String description, boolean useSubject, String mediaKey, int questionCount) {
        this.author = author;
        this.title = title;
        this.category = category;
        this.questionCount = questionCount;
        this.mediaKey = mediaKey;
        this.description = description;
        this.useSubject = useSubject;
    }

    public void addQuestions(List<Question> questions) {
        validateDuplicate(questions);
        for (Question question : questions) {
            question.setQuiz(this); // 연관관계 주인 설정
        }
        this.questions.addAll(questions);
    }

    private void validateDuplicate(List<Question> newQuestions) {
        for (Question newQuestion : newQuestions) {
            boolean exists = this.questions.stream().anyMatch(q -> q.getNo() == newQuestion.getNo());
            if (exists) {
                throw new GlobalException(ErrorCode.DUPLICATE_QUESTION_NO);
            }
        }
    }

    public void updateMetadata(String description, boolean useSubject, int questionCount) {
        this.description = description;
        this.useSubject = useSubject;
        this.questionCount = questionCount;
    }

    public void clearQuestions() {
        this.questions.clear();
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void increaseSolveCount() {
        this.solveCount++;
    }

    public void addRate(QuizRating quizRating) {
        this.quizRatings.add(quizRating);
        this.rating = ((this.rating * this.ratingCount) + quizRating.getRating()) / (this.ratingCount + 1);
        this.ratingCount++;
    }

    public void updateRate(int oldRating, int newRating) {
        if (this.ratingCount == 0) return;
        int total = (int) (this.rating * this.ratingCount);
        total = total - oldRating + newRating;
        this.rating = (double) total / this.ratingCount;
    }

}