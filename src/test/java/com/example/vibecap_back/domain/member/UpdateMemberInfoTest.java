package com.example.vibecap_back.domain.member;

import com.example.vibecap_back.domain.member.application.MemberInfoService;
import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

public class UpdateMemberInfoTest {

    private MemberRepository memberRepository = Mockito.mock(MemberRepository.class);
    private MemberInfoService memberInfoService;

    @BeforeEach
    void setUpTest() {
        this.memberInfoService = new MemberInfoService(memberRepository);
    }

    @Test
    void 닉네임_수정_테스트() {
        // given
        // Member givenMember = Member;
    }
}
