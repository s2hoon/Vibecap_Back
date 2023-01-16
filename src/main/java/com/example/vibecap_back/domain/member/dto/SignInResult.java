package com.example.vibecap_back.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SignInResult {
    private String token;
    private String nickname;
    @JsonProperty("member_id")
    private Long memberId;
}
