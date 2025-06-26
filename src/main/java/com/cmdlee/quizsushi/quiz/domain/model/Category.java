package com.cmdlee.quizsushi.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Category extends TimeBaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Long count = 0L;

    @Column(nullable = false, length = 10)
    private String icon;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quiz> quizzes;


    @Builder
    public Category(String title, String description, String icon) {
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.count = 0L;
    }

    public void increaseCount() {
        this.count++;
    }

    public void decreaseCount() {
        this.count = Math.max(0, this.count - 1);
    }
}