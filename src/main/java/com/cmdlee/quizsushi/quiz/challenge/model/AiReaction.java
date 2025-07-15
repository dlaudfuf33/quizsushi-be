package com.cmdlee.quizsushi.quiz.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiReaction implements Serializable {
    private String onCreated;
    private String onProgress;
    private List<String> onGradedComments = new ArrayList<>();

    public AiReaction(String onCreated, String onProgress) {
        this.onCreated = onCreated;
        this.onProgress = onProgress;
    }
    public static AiReaction of(String onCreated, String onProgress){
        return AiReaction.builder()
                .onCreated(onCreated)
                .onProgress(onProgress)
                .build();
    }

    public void applyGradedComments(List<String> comments) {
        this.onGradedComments = comments;
    }
}
