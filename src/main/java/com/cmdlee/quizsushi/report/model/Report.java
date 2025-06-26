package com.cmdlee.quizsushi.admin.domain.model;

import com.cmdlee.quizsushi.member.domain.model.QuizsushiMember;
import com.cmdlee.quizsushi.quiz.domain.model.Category;
import com.cmdlee.quizsushi.quiz.domain.model.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.tool.schema.TargetType;

@Entity
@Getter
@NoArgsConstructor
public class Report extends TimeBaseEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "report_seq"
    )
    @SequenceGenerator(
            name = "report_seq",
            sequenceName = "report_seq",
            allocationSize = 1
    )
    private Long id;

    private String reason;
    private String title;
    private String message;

    @Enumerated(EnumType.STRING)
    private TargetType targetType;

    private Long targetId;

    private boolean isRead = false;

    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private QuizsushiMember reporter;

    @Builder
    public Report(Long id, String reason, String title, String message,
                  TargetType targetType, Long targetId, boolean isRead, String status,
                  QuizsushiMember reporter) {
        this.id = id;
        this.reason = reason;
        this.title = title;
        this.message = message;
        this.targetType = targetType;
        this.targetId = targetId;
        this.isRead = isRead;
        this.status = status;
        this.reporter = reporter;
    }

    public void readReport() {
        this.isRead = true;
    }
}