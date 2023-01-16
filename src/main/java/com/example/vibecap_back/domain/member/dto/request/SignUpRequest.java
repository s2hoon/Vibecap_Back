package com.example.vibecap_back.domain.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class SignUpRequest {
    private String email;
    private String password;
    private String nickname;
}
