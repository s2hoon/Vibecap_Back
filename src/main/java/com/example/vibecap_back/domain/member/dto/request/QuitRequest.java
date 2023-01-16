package com.example.vibecap_back.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
<<<<<<< HEAD
=======
import lombok.Setter;
>>>>>>> 7a50d302fb7694d19f611a87898caf97f5bb1dbb

/**
 * 회원 탈퇴 요청
 */
@Getter
public class QuitRequest {
    @JsonProperty("member_id")
    private Long memberId;
}
