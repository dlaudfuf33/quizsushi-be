package com.cmdlee.quizsushi.ai.ai.controller;

import com.cmdlee.quizsushi.global.config.security.member.CustomMemberDetails;
import com.cmdlee.quizsushi.global.dto.CommonApiResponse;
import com.cmdlee.quizsushi.global.exception.ErrorCode;
import com.cmdlee.quizsushi.global.exception.GlobalException;
import com.cmdlee.quizsushi.global.util.RejectBot;
import com.cmdlee.quizsushi.quiz.dto.request.GenerateQuizRequest;
import com.cmdlee.quizsushi.quiz.dto.response.GenerateQuizResponse;
import com.cmdlee.quizsushi.ai.ai.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RejectBot
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    @PostMapping("/generate")
    public ResponseEntity<CommonApiResponse<List<GenerateQuizResponse>>> generateQuizzes(
            @RequestBody GenerateQuizRequest request,
            @AuthenticationPrincipal CustomMemberDetails memberDetails) {
        getAuthenticatedMemberId(memberDetails);
        List<GenerateQuizResponse> generateQuizByAI = aiService.generateQuizByAI(request);
        return ResponseEntity.ok(CommonApiResponse.ok(generateQuizByAI, "생성 성공"));
    }

    private Long getAuthenticatedMemberId(CustomMemberDetails details) {
        if (details == null) throw new GlobalException(ErrorCode.UNAUTHORIZED);
        return details.getId();
    }
}
