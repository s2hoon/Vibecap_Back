package com.example.vibecap_back.factory;

import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.model.Authority;
import com.example.vibecap_back.domain.model.MemberStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MemberFactory {
    private static final String TEST_EMAIL = "hello@naver.com";
    // qwer1234
    private static final String TEST_PW = "{bcrypt}$2a$10$t5y2mIDK1fP2MUe5K0Nrjej2aWqU/uTcHbFdKfjgVouNVGCdCabb.";
    private static final String TEST_NICKNAME = "Nick";
    public static final Long TEST_MEMBER_ID = 123L;

    /**
     * email, password, role, nickname, status 필드에 값이 들어있다.
     * @param i
     * @return
     */
    public static Member getMember(Integer i) {
        Member dummy = Member.builder()
                .email(TEST_EMAIL + i.toString())
                .password(TEST_PW)
                .role(Authority.ROLE_MEMBER.toString())
                .nickname(TEST_NICKNAME + i.toString())
                .status(MemberStatus.ACTIVE.toString())
                .build();

        return dummy;
    }

    /**
     * memberId, email, password, role, nickname, status 필드에 값이 들어있다.
     * 한 명의 회원에 대해서만 테스트 할 때 사용
     */
    public static Member getMember() {
        Member dummy = Member.builder()
                .email(TEST_EMAIL)
                .password(TEST_PW)
                .role(Authority.ROLE_MEMBER.toString())
                .nickname(TEST_NICKNAME)
                .status(MemberStatus.ACTIVE.toString())
                .build();

        return dummy;
    }

    /**
     * DB에 저장된 형태의 member 객체 반환.
     * memberId 값을 가진다.
     * 한 명의 회원에 대해서만 테스트할 때 사용
     */
    public static Member selectMember() {

        Member dummy = Member.builder()
                .memberId(TEST_MEMBER_ID)
                .email(TEST_EMAIL)
                .password(TEST_PW)
                .role(Authority.ROLE_MEMBER.toString())
                .nickname(TEST_NICKNAME)
                .status(MemberStatus.ACTIVE.toString())
                .build();

        return dummy;
    }

    /**
     * memberId가 i인 DB에 저장된 형태의 member 객체 반환.
     * @param i
     * @return
     */
    public static Member selectMember(Integer i) {

        Member dummy = Member.builder()
                .memberId(i.longValue())
                .email(TEST_EMAIL + i.toString())
                .password(TEST_PW)
                .role(Authority.ROLE_MEMBER.toString())
                .nickname(TEST_NICKNAME + i.toString())
                .status(MemberStatus.ACTIVE.toString())
                .build();

        return dummy;
    }

}
