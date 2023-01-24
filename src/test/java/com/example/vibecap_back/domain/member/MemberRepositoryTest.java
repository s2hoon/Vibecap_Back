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
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.IntStream;

// TODO spring boot 없이 service, dao 객체를 사용한 테스트 작성
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MemberRepositoryTest {

    private final String TEST_EMAIL = "hello@naver.com";
    private final String TEST_PW = "qwer1234";
    private final String TEST_NICKNAME = "Nick";

    @Autowired
    private MemberRepository memberRepository;

    /**
     * DB와의 연결이 제대로 되었는지 확인하기 위해 dummy data를 삽입하고 조회.
     */
    @Test
    @DisplayName("DB 동작 확인")
    void insertDummies() {
        IntStream.rangeClosed(1, 100).forEach(i->{
            Member dummy = createMember(i);
            memberRepository.save(dummy);
        });
    }

    Member createMember(Integer i) {
        Member dummy = Member.builder()
                .email(TEST_EMAIL + i.toString())
                .password(TEST_PW)
                .role(Authority.NORMAL.toString())
                .nickname(TEST_NICKNAME)
                .status(MemberStatus.ACTIVE.toString())
                .build();

        return dummy;
    }

}
