package com.cmdlee.quizsushi.member.dto.request;

import lombok.Getter;

@Getter
public class UpdateProfileRequest {
    private String nickName;
    private String birthDate;
    private String gender;
}
