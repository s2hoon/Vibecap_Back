package com.example.vibecap_back.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원 탈퇴 요청
 */
@Getter
public class QuitRequest {
    @JsonProperty("member_id")
    private Long memberId;
}
