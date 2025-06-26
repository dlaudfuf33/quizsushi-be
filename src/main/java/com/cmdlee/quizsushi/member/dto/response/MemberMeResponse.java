package com.cmdlee.quizsushi.member.dto.response;


import com.cmdlee.quizsushi.member.domain.model.QuizsushiMember;
import com.cmdlee.quizsushi.member.domain.model.enums.PlanTier;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class memberMeResponse implements MeResponse {
    public Long id;
    public String email;
    public String nickName;
    public PlanTier planTier;


    public static MeResponse from(QuizsushiMember member) {
        return MeResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickName(member.getNickname())
                .planTier(member.getPlanTier())
                .build();
    }
}
