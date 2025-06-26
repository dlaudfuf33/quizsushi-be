package com.cmdlee.quizsushi.global.tmp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // ### 400 - Client Errors ###
    INVALID_INPUT_VALUE("V001", "유효하지 않은 입력 값입니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_ENTITY("V002", "이미 존재하는 데이터입니다.", HttpStatus.CONFLICT),
    ENTITY_NOT_FOUND("V003", "존재하지 않는 리소스입니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_QUESTION_NO("V004", "중복된 문제 번호가 존재합니다.", HttpStatus.BAD_REQUEST),
    BAD_SORT_KEY("V005", "잘못된 정렬 기준입니다.", HttpStatus.BAD_REQUEST),
    INVALID_SEARCHTYPE("V006", "검색 타입은 title 또는 author만 가능합니다.", HttpStatus.BAD_REQUEST),
    FORBIDDEN_REQUEST("V007", "봇 접근 차단", HttpStatus.BAD_REQUEST),
    DUPLICATE_RATING("V008", "중복 평가 차단", HttpStatus.BAD_REQUEST),

    // ### 401/403 - Auth Errors ###
    UNAUTHORIZED("A001", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN_ACCESS("A002", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    INVALID_PASSWORD("A003", "비밀번호가 일치하지 않습니다.", HttpStatus.FORBIDDEN),

    // ### 422 - Business Logic Errors ###
    QUIZ_ALREADY_DELETED("B001", "이미 삭제된 퀴즈입니다.", HttpStatus.UNPROCESSABLE_ENTITY),
    QUIZ_EDIT_FORBIDDEN("B002", "이 퀴즈는 더 이상 수정할 수 없습니다.", HttpStatus.UNPROCESSABLE_ENTITY),

    // ### 500 - Server/Internal Errors ###
    INTERNAL_SERVER_ERROR("S001", "서버 내부 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // ### 5XX - AI / External Service Errors ###
    PROMPT_NOT_FOUND("E001", "AI 프롬프트를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_AI_OUTPUT("E002", "AI 응답 형식이 잘못되었습니다.", HttpStatus.BAD_REQUEST),
    AI_EMPTY_RESPONSE("E003", "AI 응답이 비어 있습니다.", HttpStatus.BAD_REQUEST),
    AI_RESPONSE_PARSE_FAILED("E004", "AI 응답 파싱에 실패했습니다.", HttpStatus.BAD_REQUEST),
    AI_COMMUNICATION_FAILED("E005", "AI 서버와의 통신에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_MOVE_FAILED("S002", "파일 이동에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_DELETE_FAILED("S003", "파일 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    OAUTH_REDIRECT_FAILED("O001", "OAuth 리다이렉트에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;
}