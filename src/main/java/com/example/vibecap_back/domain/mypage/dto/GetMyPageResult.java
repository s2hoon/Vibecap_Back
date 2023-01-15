package com.example.vibecap_back.domain.mypage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonPropertyOrder({"memberId", "email", "gmail", "nickname", "profileImage"})
public class GetMyPageResult {
    @JsonProperty("member_id")
    private Long memberId;
    private String email;
    @JsonProperty("google_email")
    private String gmail;
    private String nickname;
    @JsonProperty("profile_image")
    private byte[] profileImage;
}
