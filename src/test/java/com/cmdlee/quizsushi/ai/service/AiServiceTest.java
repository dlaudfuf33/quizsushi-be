
package com.cmdlee.quizsushi.ai.service;

import com.cmdlee.quizsushi.ai.adapter.AiModelAdapter;
import com.cmdlee.quizsushi.ai.router.AiModelRouter;
import com.cmdlee.quizsushi.quiz.dto.request.GenerateQuizRequest;
import com.cmdlee.quizsushi.quiz.dto.response.GenerateQuizResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AiServiceTest {

    @InjectMocks
    private AiService aiService;

    @Mock
    private AiModelRouter aiModelRouter;

    @Mock
    private AiModelAdapter aiModelAdapter;

    @Test
    @DisplayName("AI 퀴즈 생성 요청 시, 적절한 어댑터를 호출하고 결과를 반환한다")
    void generateQuizByAI_success() {
        // given
        GenerateQuizRequest request = GenerateQuizRequest.builder()
                .topic("Java")
                .difficulty("EASY")
                .count(1)
                .build();

        GenerateQuizResponse fakeResponse = GenerateQuizResponse.builder()
                .no(1)
                .type("multiple")
                .subject("Java")
                .question("What is Java?")
                .options(List.of("A", "B", "C", "D"))
                .correctAnswer(List.of(1))
                .correctAnswerText("A")
                .explanation("Explanation")
                .build();
        List<GenerateQuizResponse> expectedResponse = Collections.singletonList(fakeResponse);

        when(aiModelRouter.getAdapter(anyString())).thenReturn(aiModelAdapter);
        when(aiModelAdapter.generateQuiz(any(GenerateQuizRequest.class))).thenReturn(expectedResponse);

        // when
        List<GenerateQuizResponse> actualResponse = aiService.generateQuizByAI(request);

        // then
        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(aiModelRouter, times(1)).getAdapter("llama3");
        verify(aiModelAdapter, times(1)).generateQuiz(request);
    }
}
