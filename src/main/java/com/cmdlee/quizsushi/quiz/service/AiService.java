package com.cmdlee.quizsushi.service;

import com.cmdlee.quizsushi.domain.dto.request.GenerateQuizRequest;
import com.cmdlee.quizsushi.domain.dto.response.GenerateQuizResponse;
import com.cmdlee.quizsushi.domain.repository.AiPromptRepository;
import com.cmdlee.quizsushi.exception.ErrorCode;
import com.cmdlee.quizsushi.exception.GlobalException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {
    private final AiPromptRepository promptRepository;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public List<GenerateQuizResponse> generateQuizByAI(GenerateQuizRequest request) {
        log.info("[AI GENERATE] Request started: topic={}, description={}, count={}, difficulty={}, questionType={}",
                request.getTopic(), request.getDescription(), request.getCount(), request.getDifficulty(), request.getQuestionType());

        String template = getLatestQuizPrompt();
        String prompt = buildPrompt(request, template);
        log.debug("[AI PROMPT] Prompt generated:\n{}", prompt);

        String aiRaw;
        try {
            log.info("[AI REQUEST] Sending request to WebClient");
            aiRaw = webClient.post()
                    .uri("http://localhost:11434/api/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of("model", "mistral", "prompt", prompt, "stream", false))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (aiRaw == null || aiRaw.isBlank()) {
                throw new GlobalException(ErrorCode.AI_EMPTY_RESPONSE);
            }
            log.info("[AI RESPONSE] Received response: bytes={}", aiRaw.length());
        } catch (Exception e) {
            log.error("[AI REQUEST ERROR] WebClient call failed", e);
            throw new GlobalException(ErrorCode.AI_COMMUNICATION_FAILED, e);
        }

        return parseAiResponse(aiRaw);
    }

    public String getLatestQuizPrompt() {
        return promptRepository.findFirstByNameOrderByUpdatedAtDesc("quiz_generation")
                .orElseThrow(() -> new GlobalException(ErrorCode.PROMPT_NOT_FOUND))
                .getTemplate();
    }

    private String buildPrompt(GenerateQuizRequest request, String template) {
        Map<String, String> valuesMap = Map.of(
                "TOPIC", request.getTopic(),
                "DESCRIPTION", request.getDescription(),
                "COUNT", String.valueOf(request.getCount()),
                "DIFFICULTY", request.getDifficulty(),
                "QUESTIONTYPE", request.getQuestionType()
        );

        StringSubstitutor substitution = new StringSubstitutor(valuesMap, "{{", "}}");
        return substitution.replace(template);
    }

    private List<GenerateQuizResponse> parseAiResponse(String aiRaw) {
        try {
            TypeReference<Map<String, Object>> mapType = new TypeReference<>() {
            };
            Map<String, Object> aiRawMap = objectMapper.readValue(aiRaw, mapType);
            Object responseObj = aiRawMap.get("response");

            if (responseObj == null) {
                throw new GlobalException(ErrorCode.AI_RESPONSE_PARSE_FAILED);
            }

            String responseStr = objectMapper.convertValue(responseObj, String.class);
            String cleanedJson = responseStr.replaceAll("(?s)```json\\s*|```", "").trim();
            List<GenerateQuizResponse> parsed = Arrays.stream(objectMapper.readValue(cleanedJson, GenerateQuizResponse[].class)).toList();
            log.info("[AI PARSE] Quiz parsing successful - {} items generated", parsed.size());
            return parsed;

        } catch (Exception e) {
            log.error("[AI RESPONSE ERROR] Failed to parse AI response: {}", aiRaw, e);
            throw new GlobalException(ErrorCode.AI_RESPONSE_PARSE_FAILED, e);
        }
    }
}