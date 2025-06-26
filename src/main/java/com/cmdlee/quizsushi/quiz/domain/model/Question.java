package com.cmdlee.quizsushi.domain.model;

import com.cmdlee.quizsushi.domain.dto.request.UpdateQuestionRequest;
import com.cmdlee.quizsushi.domain.model.Enum.QuestionType;
import com.cmdlee.quizsushi.domain.model.converter.StringListToJsonConverter;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Question extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int no;

    @Column(length = 100)
    private String subject;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Convert(converter = StringListToJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> options;

    // 객관식 정답
    @Column(name = "correct_answer")
    private Integer correctIdx;

    // 주관식 정답
    private String correctAnswerText;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Builder
    public Question(int no, String subject, QuestionType type, String questionText, List<String> options,
                    Integer correctIdx, String correctAnswerText, String explanation, Quiz quiz) {
        this.no = no;
        this.subject = subject;
        this.type = type;
        this.questionText = questionText;
        this.options = options != null ? options : new ArrayList<>();
        this.correctIdx = correctIdx;
        this.correctAnswerText = correctAnswerText;
        this.explanation = explanation;
        this.quiz = quiz;
    }

    public void updateFrom(UpdateQuestionRequest dto,boolean useSubject) {
        this.no = dto.getNo();
        this.subject = useSubject ? dto.getSubject() : "";
        this.type = QuestionType.valueOf(dto.getType());
        this.questionText = dto.getQuestion();
        this.options = dto.getOptions();
        this.correctIdx = dto.getCorrectAnswer();
        this.correctAnswerText = dto.getCorrectAnswerText();
        this.explanation = dto.getExplanation();
    }
}