package com.example.vibecap_back.domain.mypage.dto.response;

import com.example.vibecap_back.domain.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"memberId", "email", "gmail", "nickname", "profileImage"})
public class GetMyPageResponse {
    @JsonProperty("member_id")
    private Long memberId;
    private String email;
    @JsonProperty("google_email")
    private String gmail;
    private String nickname;
    @JsonProperty("profile_image")
    private String profileImage;

    public GetMyPageResponse(Member member) {
        this.memberId = member.getMemberId();
        this.email = member.getEmail();
        this.gmail = member.getGmail();
        this.nickname = member.getNickname();
        this.profileImage = member.getProfileImage();
    }
}

