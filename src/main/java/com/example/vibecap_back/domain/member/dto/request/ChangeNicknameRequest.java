package com.example.vibecap_back.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
<<<<<<< HEAD
=======
import lombok.AllArgsConstructor;
>>>>>>> 7a50d302fb7694d19f611a87898caf97f5bb1dbb
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
<<<<<<< HEAD
=======
@AllArgsConstructor
>>>>>>> 7a50d302fb7694d19f611a87898caf97f5bb1dbb
public class ChangeNicknameRequest {
    @JsonProperty("member_id")
    private Long memberId;
    @JsonProperty("new_nickname")
    private String newNickname;
}
