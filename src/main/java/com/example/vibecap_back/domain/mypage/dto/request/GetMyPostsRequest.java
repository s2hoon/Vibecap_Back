package com.example.vibecap_back.domain.mypage.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetMyPostsRequest {
    @JsonProperty("member_id")
    private Long memberId;
}