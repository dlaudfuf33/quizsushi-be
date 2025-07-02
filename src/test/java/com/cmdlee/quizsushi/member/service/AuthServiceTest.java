
package com.cmdlee.quizsushi.member.service;

import com.cmdlee.quizsushi.global.auth.jwt.JwtTokenProvider;
import com.cmdlee.quizsushi.member.domain.model.QuizsushiMember;
import com.cmdlee.quizsushi.member.dto.OAuthUserInfo;
import com.cmdlee.quizsushi.member.security.TokenPair;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private KakaoOAuthService kakaoOAuthService;

    @Mock
    private GoogleOAuthService googleOAuthService;

    @Mock
    private MemberService memberService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private OAuthUserInfo oAuthUserInfo;
    private QuizsushiMember quizsushiMember;
    private String accessToken;
    private String refreshTokenUUID;

    @BeforeEach
    void setUp() {
        quizsushiMember = QuizsushiMember.builder().id(1L).email("test@example.com").build();
        accessToken = "mockAccessToken";
        refreshTokenUUID = UUID.randomUUID().toString();
    }

    @Test
    @DisplayName("Google 로그인 URL을 올바르게 반환한다")
    void getGoogleLoginUrl_returnsCorrectUrl() {
        String expectedUrl = "http://google.login.url";
        when(googleOAuthService.generateGoogleLoginUrl()).thenReturn(expectedUrl);

        String actualUrl = authService.getGoogleLoginUrl();

        assertThat(actualUrl).isEqualTo(expectedUrl);
        verify(googleOAuthService, times(1)).generateGoogleLoginUrl();
    }

    @Test
    @DisplayName("Google 콜백 처리 후 토큰 쌍을 반환한다")
    void handleGoogleCallback_returnsTokenPair() {
        // given
        String code = "authCode";
        when(googleOAuthService.handleCallback(code)).thenReturn(oAuthUserInfo);
        when(memberService.findOrCreateByOAuth(any(OAuthUserInfo.class))).thenReturn(quizsushiMember);
        when(jwtTokenProvider.createToken(anyString())).thenReturn(accessToken);
        doNothing().when(refreshTokenService).save(anyString(), anyString(), any(HttpServletRequest.class));

        // when
        TokenPair tokenPair = authService.handleGoogleCallback(code, request);

        // then
        assertThat(tokenPair.getAccessToken()).isEqualTo(accessToken);
        assertThat(tokenPair.getRefreshToken()).isNotNull();
        verify(googleOAuthService, times(1)).handleCallback(code);
        verify(memberService, times(1)).findOrCreateByOAuth(oAuthUserInfo);
        verify(refreshTokenService, times(1)).save(eq(quizsushiMember.getId().toString()), anyString(), eq(request));
        verify(jwtTokenProvider, times(1)).createToken(quizsushiMember.getId().toString());
    }

    @Test
    @DisplayName("Kakao 로그인 URL을 올바르게 반환한다")
    void getKakaoLoginUrl_returnsCorrectUrl() {
        String expectedUrl = "http://kakao.login.url";
        when(kakaoOAuthService.generateKakaoLoginUrl()).thenReturn(expectedUrl);

        String actualUrl = authService.getKakaoLoginUrl();

        assertThat(actualUrl).isEqualTo(expectedUrl);
        verify(kakaoOAuthService, times(1)).generateKakaoLoginUrl();
    }

    @Test
    @DisplayName("Kakao 콜백 처리 후 토큰 쌍을 반환한다")
    void handleKakaoCallback_returnsTokenPair() {
        // given
        String code = "authCode";
        String kakaoAccessToken = "kakaoAccessToken";
        when(kakaoOAuthService.requestAccessToken(code)).thenReturn(kakaoAccessToken);
        when(kakaoOAuthService.getUserInfo(kakaoAccessToken)).thenReturn(oAuthUserInfo);
        when(memberService.findOrCreateByOAuth(any(OAuthUserInfo.class))).thenReturn(quizsushiMember);
        when(jwtTokenProvider.createToken(anyString())).thenReturn(accessToken);
        doNothing().when(refreshTokenService).save(anyString(), anyString(), any(HttpServletRequest.class));

        // when
        TokenPair tokenPair = authService.handleKakaoCallback(code, request);

        // then
        assertThat(tokenPair.getAccessToken()).isEqualTo(accessToken);
        assertThat(tokenPair.getRefreshToken()).isNotNull();
        verify(kakaoOAuthService, times(1)).requestAccessToken(code);
        verify(kakaoOAuthService, times(1)).getUserInfo(kakaoAccessToken);
        verify(memberService, times(1)).findOrCreateByOAuth(oAuthUserInfo);
        verify(refreshTokenService, times(1)).save(eq(quizsushiMember.getId().toString()), anyString(), eq(request));
        verify(jwtTokenProvider, times(1)).createToken(quizsushiMember.getId().toString());
    }

    @Test
    @DisplayName("유효한 Refresh Token으로 로그아웃 시 토큰을 삭제하고 쿠키를 만료시킨다")
    void logout_validRefreshToken_deletesTokenAndExpiresCookies() {
        // given
        String validRefreshToken = refreshTokenUUID;
        when(jwtTokenProvider.resolveRefreshToken(request)).thenReturn(validRefreshToken);
        when(jwtTokenProvider.validateToken(validRefreshToken)).thenReturn(true);
        doNothing().when(refreshTokenService).delete(validRefreshToken);

        // when
        authService.logout(request, response);

        // then
        verify(refreshTokenService, times(1)).delete(validRefreshToken);
        verify(response, times(2)).addHeader(eq("Set-Cookie"), anyString());
    }

    @Test
    @DisplayName("유효하지 않은 Refresh Token으로 로그아웃 시 토큰을 삭제하지 않는다")
    void logout_invalidRefreshToken_doesNotDeleteToken() {
        // given
        String invalidRefreshToken = "invalidToken";
        when(jwtTokenProvider.resolveRefreshToken(request)).thenReturn(invalidRefreshToken);
        when(jwtTokenProvider.validateToken(invalidRefreshToken)).thenReturn(false);

        // when
        authService.logout(request, response);

        // then
        verify(refreshTokenService, never()).delete(anyString());
        verify(response, times(2)).addHeader(eq("Set-Cookie"), anyString());
    }

    @Test
    @DisplayName("Refresh Token이 없는 경우 로그아웃 시 토큰을 삭제하지 않는다")
    void logout_noRefreshToken_doesNotDeleteToken() {
        // given
        when(jwtTokenProvider.resolveRefreshToken(request)).thenReturn(null);

        // when
        authService.logout(request, response);

        // then
        verify(refreshTokenService, never()).delete(anyString());
        verify(response, times(2)).addHeader(eq("Set-Cookie"), anyString());
    }
}
