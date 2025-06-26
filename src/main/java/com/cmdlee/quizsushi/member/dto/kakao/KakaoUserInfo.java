package com.cmdlee.quizsushi.member.dto;

import com.cmdlee.quizsushi.member.domain.model.enums.OAuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoUserInfo implements OAuthUserInfo {
    private Long id;
    private String email;
    private String nickname;

    @Override
    public OAuthProvider getProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public String getProviderId() {
        return String.valueOf(id);
    }
}