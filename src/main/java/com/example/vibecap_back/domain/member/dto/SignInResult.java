package com.example.vibecap_back.domain.member.dto;

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
    private Long memberId;
}
