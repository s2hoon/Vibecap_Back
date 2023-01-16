package com.example.vibecap_back.domain.mypage.dto;

import com.example.vibecap_back.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MyPageDto {
    private Long memberId;
    private String email;
    private String gmail;
    private String nickname;
    private byte[] profileImage;

    public MyPageDto(Member member) {
        this.memberId = member.getMemberId();
        this.email = member.getEmail();
        this.gmail = member.getGmail();
        this.nickname = member.getNickname();
        this.profileImage = member.getProfileImage();
    }
}
