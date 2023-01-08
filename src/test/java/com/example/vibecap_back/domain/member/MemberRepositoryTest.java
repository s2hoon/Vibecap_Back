package com.example.vibecap_back.domain.member;

import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.member.dto.MemberDto;
import com.example.vibecap_back.domain.model.Authority;
import com.example.vibecap_back.domain.model.MemberStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.IntStream;

// TODO spring boot 없이 service, dao 객체를 사용한 테스트 작성
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MemberRepositoryTest {

    private String TEST_EMAIL = "hello@naver.com";
    private String TEST_PW = "qwer1234";
    private String TEST_NICKNAME = "Nick";

    @Autowired
    private MemberRepository memberRepository;
    private MemberDto memberDto;
    private Member member;

    @BeforeEach
    void init() {
        memberDto = MemberDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PW)
                .role(Authority.NORMAL)
                .nickname(TEST_NICKNAME)
                .status(MemberStatus.ACTIVE)
                .build();

        member = Member.builder()
                .email(TEST_EMAIL)
                .password(TEST_PW)
                .role(Authority.NORMAL)
                .nickname(TEST_NICKNAME)
                .status(MemberStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("DB 동작 확인")
    void insertDummies() {
        IntStream.rangeClosed(1, 100).forEach(i->{
            MemberDto dummy = createMemberDto(i);
            memberRepository.save(member);
        });
    }

    MemberDto createMemberDto(Integer i) {
        MemberDto dummy = MemberDto.builder()
                .email(TEST_EMAIL + i.toString())
                .password(TEST_PW)
                .role(Authority.NORMAL)
                .nickname(TEST_NICKNAME)
                .status(MemberStatus.ACTIVE)
                .build();

        return dummy;
    }

}
