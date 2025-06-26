package com.cmdlee.quizsushi.quiz.domain.model;

import com.cmdlee.quizsushi.member.domain.model.QuizsushiMember;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class MemberQuizSolveLog {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "quiz_solve_log_seq"
    )
    @SequenceGenerator(
            name = "quiz_solve_log_seq",
            sequenceName = "quiz_solve_log_seq",
            allocationSize = 1
    )
    private Long id;

    // 퀴즈를 푼 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private QuizsushiMember member;

    // 푼 퀴즈
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    // 획득한 점수
    private Integer score;

    private final LocalDateTime submittedAt = LocalDateTime.now();

    @Builder
    public QuizSolveLog(Quiz quiz, QuizsushiMember member, Integer score) {
        this.quiz = quiz;
        this.member = member;
        this.score = score;
    }


}