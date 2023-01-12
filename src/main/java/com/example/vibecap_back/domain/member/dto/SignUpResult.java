package com.example.vibecap_back.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignUpResult {
    @JsonProperty("member_id")
    private Long memberId;
    // private String jwt;
}
