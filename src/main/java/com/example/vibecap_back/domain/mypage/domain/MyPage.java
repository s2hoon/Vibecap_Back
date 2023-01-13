package com.example.vibecap_back.domain.mypage.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MyPage {
    private Long memberId;
    private String email;
    private String gmail;
    private String nickname;
    private byte[] profileImage;
}
