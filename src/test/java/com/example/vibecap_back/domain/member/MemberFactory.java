package com.example.vibecap_back.domain.member;

import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.model.Authority;
import com.example.vibecap_back.domain.model.MemberStatus;

public class MemberFactory {
    private static final String TEST_EMAIL = "hello@naver.com";
    private static final String TEST_PW = "qwer1234";
    private static final String TEST_NICKNAME = "Nick";

    /**
     * email, password, role, nickname, status 필드에 값이 들어있다.
     * @param i
     * @return
     */
    static Member createMember(Integer i) {
        Member dummy = Member.builder()
                .email(TEST_EMAIL + i.toString())
                .password(TEST_PW)
                .role(Authority.ROLE_MEMBER)
                .nickname(TEST_NICKNAME + i.toString())
                .status(MemberStatus.ACTIVE)
                .build();

        return dummy;
    }

    /**
     * memberId, email, password, role, nickname, status 필드에 값이 들어있다.
     * @return
     */
    static Member createMember() {
        Member dummy = Member.builder()
                .email(TEST_EMAIL)
                .password(TEST_PW)
                .role(Authority.ROLE_MEMBER)
                .nickname(TEST_NICKNAME)
                .status(MemberStatus.ACTIVE)
                .build();

        return dummy;
    }

    /**
     * DB에 저장된 형태의 member 객체 반환.
     * memberId 값을 가진다.
     */
    static Member selectMember() {

        Member dummy = Member.builder()
                .memberId(123L)
                .email(TEST_EMAIL)
                .password(TEST_PW)
                .role(Authority.ROLE_MEMBER)
                .nickname(TEST_NICKNAME)
                .status(MemberStatus.ACTIVE)
                .build();

        return dummy;
    }

    /**
     * memberId가 i인 DB에 저장된 형태의 member 객체 반환.
     * @param i
     * @return
     */
    static Member selectMember(Integer i) {

        Member dummy = Member.builder()
                .memberId(i.longValue())
                .email(TEST_EMAIL)
                .password(TEST_PW)
                .role(Authority.ROLE_MEMBER)
                .nickname(TEST_NICKNAME + i.toString())
                .status(MemberStatus.ACTIVE)
                .build();

        return dummy;
    }

}
