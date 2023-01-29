package com.example.vibecap_back.domain.post.dto.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostLikeDto {

    @JsonProperty("member_id")
    private Long memberId;
}
