package com.cmdlee.quizsushi.quiz.challenge.service;

import com.cmdlee.quizsushi.global.config.scheduler.ChallengeProperties;
import com.cmdlee.quizsushi.global.exception.ErrorCode;
import com.cmdlee.quizsushi.global.exception.GlobalException;
import com.cmdlee.quizsushi.quiz.challenge.model.ChallengeSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChallengeSessionRedisServiceTest {
    private static final String SESSION_ID_SET_KEY = "challenge:sessions";
    private static final String SESSION_KEY_PREFIX = "challenge:sessions:";

    @Mock
    private ChallengeProperties challengeProperties;

    @Mock
    private RedisTemplate<String, ChallengeSession> challengeSessionRedisTemplate;

    @Mock
    private RedisTemplate<String, String> stringRedisTemplate;

    @Mock
    private ValueOperations<String, ChallengeSession> valueOperations;

    @Mock
    private SetOperations<String, String> setOperations;

    @InjectMocks
    private ChallengeSessionRedisService challengeSessionRedisService;

    @BeforeEach
    void setUp() {
        when(challengeSessionRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(stringRedisTemplate.opsForSet()).thenReturn(setOperations);
        when(challengeProperties.getMaxSessions()).thenReturn(10);
    }

    @DisplayName("새로운 세션을 성공적으로 생성한다")
    @Test
    void createNewSession_success() {
        // Given
        String rawId = UUID.randomUUID().toString();
        String sessionKey = SESSION_KEY_PREFIX + rawId;
        when(challengeSessionRedisTemplate.hasKey(sessionKey)).thenReturn(false);
        when(setOperations.size(SESSION_ID_SET_KEY)).thenReturn(0L);

        // When
        String createdSessionId = challengeSessionRedisService.createNewSession();

        // Then
        assertThat(createdSessionId).isNotNull();
        verify(valueOperations, times(1)).set(eq(sessionKey), any(ChallengeSession.class), any(Duration.class));
        verify(setOperations, times(1)).add(SESSION_ID_SET_KEY, sessionKey);
    }

    @DisplayName("세션이 이미 존재할 때 createNewSession 호출 시 GlobalException을 던진다")
    @Test
    void createNewSession_sessionAlreadyExists_throwsException() {
        // Given
        String rawId = UUID.randomUUID().toString();
        String sessionKey = SESSION_KEY_PREFIX + rawId;
        when(challengeSessionRedisTemplate.hasKey(sessionKey)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> challengeSessionRedisService.createNewSession())
                .isInstanceOf(GlobalException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.SESSION_ALREADY_EXISTS);
    }

    @DisplayName("세션 개수가 최대치일 경우 예외를 던진다")
    @Test
    void createNewSession_sessionLimitExceeded_throwsException() {
        // Given
        String rawId = UUID.randomUUID().toString();
        String sessionKey = SESSION_KEY_PREFIX + rawId;
        when(challengeSessionRedisTemplate.hasKey(sessionKey)).thenReturn(false);
        when(setOperations.size(SESSION_ID_SET_KEY)).thenReturn(10L); // Max sessions is 10

        // When & Then
        assertThatThrownBy(() -> challengeSessionRedisService.createNewSession())
                .isInstanceOf(GlobalException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.SESSION_LIMIT_EXCEEDED);
    }

    @DisplayName("세션을 성공적으로 저장한다")
    @Test
    void saveSession_success() {
        // Given
        ChallengeSession session = new ChallengeSession("testSessionId");
        String sessionKey = SESSION_KEY_PREFIX + session.getSessionId();

        // When
        challengeSessionRedisService.saveSession(session);

        // Then
        verify(valueOperations, times(1)).set(eq(sessionKey), eq(session), any(Duration.class));
        verify(setOperations, times(1)).add(SESSION_ID_SET_KEY, sessionKey);
    }

    @DisplayName("세션을 성공적으로 조회한다")
    @Test
    void getSession_success() {
        // Given
        String sessionId = "testSessionId";
        String sessionKey = SESSION_KEY_PREFIX + sessionId;
        ChallengeSession expectedSession = new ChallengeSession(sessionId);
        when(valueOperations.get(sessionKey)).thenReturn(expectedSession);

        // When
        ChallengeSession actualSession = challengeSessionRedisService.getSession(sessionId);

        // Then
        assertThat(actualSession).isEqualTo(expectedSession);
        verify(valueOperations, times(1)).get(sessionKey);
    }

    @DisplayName("세션을 성공적으로 삭제한다")
    @Test
    void deleteSession_success() {
        // Given
        String sessionId = "testSessionId";
        String sessionKey = SESSION_KEY_PREFIX + sessionId;

        // When
        challengeSessionRedisService.deleteSession(sessionId);

        // Then
        verify(challengeSessionRedisTemplate, times(1)).delete(sessionKey);
        verify(setOperations, times(1)).remove(SESSION_ID_SET_KEY, sessionKey);
    }

    @DisplayName("활성 세션 수를 올바르게 반환한다")
    @Test
    void getActiveSessionCount_returnsCorrectCount() {
        // Given
        when(setOperations.size(SESSION_ID_SET_KEY)).thenReturn(5L);

        // When
        long count = challengeSessionRedisService.getActiveSessionCount();

        // Then
        assertThat(count).isEqualTo(5L);
    }

    @DisplayName("만료된 세션 ID를 성공적으로 정리한다")
    @Test
    void cleanExpiredSessionIds_removesExpired() {
        // Given
        String expiredSessionKey = SESSION_KEY_PREFIX + "expired";
        String activeSessionKey = SESSION_KEY_PREFIX + "active";
        Set<String> sessionKeys = Set.of(expiredSessionKey, activeSessionKey);

        when(setOperations.members(SESSION_ID_SET_KEY)).thenReturn(sessionKeys);
        when(challengeSessionRedisTemplate.hasKey(expiredSessionKey)).thenReturn(false); // Expired
        when(challengeSessionRedisTemplate.hasKey(activeSessionKey)).thenReturn(true);  // Active

        // When
        challengeSessionRedisService.cleanExpiredSessionIds();

        // Then
        verify(setOperations, times(1)).members(SESSION_ID_SET_KEY);
        verify(challengeSessionRedisTemplate, times(1)).hasKey(expiredSessionKey);
        verify(challengeSessionRedisTemplate, times(1)).hasKey(activeSessionKey);
        verify(setOperations, times(1)).remove(SESSION_ID_SET_KEY, expiredSessionKey);
        verify(setOperations, never()).remove(SESSION_ID_SET_KEY, activeSessionKey);
    }

    @DisplayName("활성 세션 ID가 없을 때 cleanExpiredSessionIds 호출 시 아무것도 하지 않는다")
    @Test
    void cleanExpiredSessionIds_noSessionIds_doesNothing() {
        // Given
        when(setOperations.members(SESSION_ID_SET_KEY)).thenReturn(Collections.emptySet());

        // When
        challengeSessionRedisService.cleanExpiredSessionIds();

        // Then
        verify(setOperations, times(1)).members(SESSION_ID_SET_KEY);
        verify(challengeSessionRedisTemplate, never()).hasKey(anyString());
        verify(setOperations, never()).remove(anyString(), anyString());
    }
}
