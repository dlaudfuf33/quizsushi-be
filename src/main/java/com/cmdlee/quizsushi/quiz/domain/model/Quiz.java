package com.cmdlee.quizsushi.domain.model;

import com.cmdlee.quizsushi.domain.dto.request.UpdateQuestionRequest;
import com.cmdlee.quizsushi.domain.dto.request.UpdateQuizRequest;
import com.cmdlee.quizsushi.domain.model.Enum.QuestionType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Quiz extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String authorName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean useSubject;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizRating> quizRatings = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Question> questions = new HashSet<>();

    public double getRating() {
        if (quizRatings == null || quizRatings.isEmpty()) return 0.0;
        return quizRatings.stream()
                .mapToInt(QuizRating::getRating)
                .average()
                .orElse(0.0);
    }

    public long getReviewCount() {
        if (quizRatings == null) return 0;
        return quizRatings.size();
    }

    @Builder
    public Quiz(String authorName, String password, String title, Category category, String description, boolean useSubject) {
        this.authorName = authorName;
        this.password = password;
        this.title = title;
        this.category = category;
        this.description = description;
        this.useSubject = useSubject;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
        question.setQuiz(this);
    }

    public void updateQuiz(UpdateQuizRequest dto) {
        this.description = dto.getDescription();
        this.useSubject = dto.isUseSubject();

        Map<Long, Question> existingMap = this.questions.stream()
                .filter(q -> q.getId() != null)
                .collect(Collectors.toMap(Question::getId, q -> q));

        Set<Question> updated = new HashSet<>();

        for (UpdateQuestionRequest qDto : dto.getQuestions()) {
            if (qDto.getId() != null && existingMap.containsKey(qDto.getId())) {
                // ✅ 수정
                Question existing = existingMap.get(qDto.getId());
                existing.updateFrom(qDto, this.useSubject);
                updated.add(existing);
            } else {
                // ✅ 추가
                Question newQ = Question.builder()
                        .no(qDto.getNo())
                        .type(QuestionType.valueOf(qDto.getType()))
                        .subject(this.useSubject ? qDto.getSubject() : "")
                        .questionText(qDto.getQuestion())
                        .options(qDto.getOptions())
                        .correctIdx(qDto.getCorrectAnswer())
                        .correctAnswerText(qDto.getCorrectAnswerText())
                        .explanation(qDto.getExplanation())
                        .quiz(this)
                        .build();
                updated.add(newQ);
            }
        }

        // ✅ 삭제: 기존 문제들 중, dto에 없는 것 제거
        this.questions.removeIf(q -> q.getId() != null &&
                updated.stream().noneMatch(u -> Objects.equals(u.getId(), q.getId())));

        // ✅ 새로 추가된 문제들 add
        for (Question q : updated) {
            this.addQuestion(q);
        }
    }

}