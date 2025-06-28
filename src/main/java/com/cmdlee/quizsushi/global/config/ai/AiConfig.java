package com.cmdlee.quizsushi.global.config.ai;

import com.cmdlee.quizsushi.ai.adapter.AiModelAdapter;
import com.cmdlee.quizsushi.ai.adapter.Llama3Adapter;
import com.cmdlee.quizsushi.ai.prompt.PromptBuilder;
import com.cmdlee.quizsushi.ai.router.AiInstanceRouter;
import com.cmdlee.quizsushi.quiz.dto.request.GenerateQuizRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AiProperties.class)
public class AiConfig {

    @Bean("llama3")
    public AiModelAdapter llama3Adapter(WebClient webClient,
                                        ObjectMapper objectMapper,
                                        AiProperties aiProperties,
                                        AiInstanceRouter instanceRouter,
                                        PromptBuilder<GenerateQuizRequest> promptBuilder) {
        return new Llama3Adapter(webClient, objectMapper, aiProperties, instanceRouter, promptBuilder);
    }
}