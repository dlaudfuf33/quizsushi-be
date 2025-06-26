package com.cmdlee.quizsushi.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // === [V] Validation / Client Errors (400 ~ 409) ===
    INVALID_INPUT_VALUE("V001", "유효하지 않은 입력 값입니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_ENTITY("V002", "이미 존재하는 데이터입니다.", HttpStatus.CONFLICT),
    ENTITY_NOT_FOUND("V003", "존재하지 않는 리소스입니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_QUESTION_NO("V004", "중복된 문제 번호가 존재합니다.", HttpStatus.BAD_REQUEST),
    BAD_SORT_KEY("V005", "잘못된 정렬 기준입니다.", HttpStatus.BAD_REQUEST),
    INVALID_SEARCHTYPE("V006", "검색 타입은 title 또는 author만 가능합니다.", HttpStatus.BAD_REQUEST),
    BOT_ACCESS_BLOCKED("V007", "자동화 접근이 차단되었습니다.", HttpStatus.FORBIDDEN),
    DUPLICATE_RATING("V008", "중복 평가 차단", HttpStatus.BAD_REQUEST),
    UNSUPPORTED_REPORT_TARGET("V009", "지원하지 않는 신고 유형입니다.", HttpStatus.BAD_REQUEST),
    UNSUPPORTED_REPORT_STATUS("V010", "지원하지 않는 신고 상태입니다.", HttpStatus.BAD_REQUEST),
    WRONG_DATE_RANGE("V0011", "미래 시각은 지정할 수 없습니다.", HttpStatus.BAD_REQUEST),

    // === [A] Auth Errors (401/403) ===
    USER_ID_REQUIRED("A001", "userId 정보가 필요합니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("A002", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN_ACCESS("A003", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    INVALID_PASSWORD("A004", "비밀번호가 일치하지 않습니다.", HttpStatus.FORBIDDEN),
    REFRESH_TOKEN_MISSING("A005", "리프레시 토큰이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("A006", "유효하지 않은 리프레시 토큰입니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_CLIENT_MISMATCH("A007", "접속 환경이 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_ACCESS_TOKEN("A008", "유효하지 않은 엑세스 토큰입니다.", HttpStatus.UNAUTHORIZED),
    WRONG_MEMBER("A009", "작성자가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    MEMBER_ALREADY_DELETED("A010", "이미 탈퇴한 회원입니다.", HttpStatus.UNAUTHORIZED),
    BANNED_MEMBER("A011", "정지된 회원입니다.", HttpStatus.UNAUTHORIZED),
    LOGIN_REQUEST_PARSING_FAILED("A012", "로그인 요청 파싱에 실패했습니다.", HttpStatus.UNAUTHORIZED),

    // === [B] Business Logic Errors (422) ===
    QUIZ_ALREADY_DELETED("B001", "이미 삭제된 퀴즈입니다.", HttpStatus.UNPROCESSABLE_ENTITY),
    QUIZ_EDIT_FORBIDDEN("B002", "이 퀴즈는 더 이상 수정할 수 없습니다.", HttpStatus.UNPROCESSABLE_ENTITY),

    // === [S] System / Internal Server Errors (500) ===
    INTERNAL_SERVER_ERROR("S001", "서버 내부 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_MOVE_FAILED("S002", "파일 이동에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_DELETE_FAILED("S003", "파일 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_UPLOAD_FAILED("S004", "파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    URL_PARSE_FAILED("S005", "URL 파싱에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // === [R] Redis 관련 ===
    REDIS_SERIALIZATION_FAILED("R001", "Redis 직렬화에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    REDIS_DESERIALIZATION_FAILED("R002", "Redis에서 refresh token 역직렬화에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // === [E] AI 관련 ===
    PROMPT_NOT_FOUND("E001", "AI 프롬프트를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_AI_OUTPUT("E002", "AI 응답 형식이 잘못되었습니다.", HttpStatus.BAD_REQUEST),
    AI_EMPTY_RESPONSE("E003", "AI 응답이 비어 있습니다.", HttpStatus.BAD_REQUEST),
    AI_RESPONSE_PARSE_FAILED("E004", "AI 응답 파싱에 실패했습니다.", HttpStatus.BAD_REQUEST),
    AI_COMMUNICATION_FAILED("E005", "AI 서버와의 통신에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // === [O] OAuth 관련 ===
    OAUTH_REDIRECT_FAILED("O001", "OAuth 리다이렉트에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    OAUTH_GOOGLE_CALLBACK_FAILED("O002", "구글 로그인 콜백 처리 중 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR),
    OAUTH_KAKAO_CALLBACK_FAILED("O003", "카카오 로그인 콜백 처리 중 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR),
    UNSUPPORTED_OAUTH_PROVIDER("O004", "지원하지 않는 OAuth 제공자입니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}