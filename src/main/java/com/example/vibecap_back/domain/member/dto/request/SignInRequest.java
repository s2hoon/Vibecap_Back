package com.example.vibecap_back.domain.member.dto.request;

import lombok.Getter;

@Getter
public class SignInRequest {
    private String email;
    private String password;
}
