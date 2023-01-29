package com.example.vibecap_back.domain.member;

import com.example.vibecap_back.domain.member.application.MemberInfoService;
import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.factory.MemberFactory;
import com.example.vibecap_back.global.config.storage.FireBaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MemberInfoServiceTest {

    private MemberRepository memberRepository = Mockito.mock(MemberRepository.class);
    private MemberInfoService memberInfoService;
    private FireBaseService fireBaseService;
    private Member testMember;

    @BeforeEach
    void setUpTest() {
        this.memberInfoService = new MemberInfoService(fireBaseService, memberRepository);
        this.testMember = MemberFactory.selectMember();
    }

    @Test
    void 닉네임_수정_테스트() {
        //
    }

    @Test
    void 프로필_사진_수정_테스트() {
        //
    }
}
