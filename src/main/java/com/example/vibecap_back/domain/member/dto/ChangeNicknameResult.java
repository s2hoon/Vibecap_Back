package com.example.vibecap_back.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeNicknameResult {
    @JsonProperty("new_nickname")
    private String newNickname;
}
