package com.example.vibecap_back.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangeNicknameRequest {
    @JsonProperty("member_id")
    private Long memberId;
    @JsonProperty("new_nickname")
    private String newNickname;
}
