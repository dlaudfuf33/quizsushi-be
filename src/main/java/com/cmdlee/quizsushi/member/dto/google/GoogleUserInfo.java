package com.cmdlee.quizsushi.member.dto;

import com.cmdlee.quizsushi.member.domain.model.enums.OAuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleUserInfo implements OAuthUserInfo {
    private String id;
    private String email;
    private String nickname;

    @Override
    public OAuthProvider getProvider() {
        return OAuthProvider.GOOGLE;
    }

    @Override
    public String getProviderId() {
        return id;
    }
}