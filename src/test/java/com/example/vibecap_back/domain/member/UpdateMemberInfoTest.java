package com.example.vibecap_back.domain.member;

import com.example.vibecap_back.domain.member.application.MemberInfoService;
import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.member.dto.ChangeNicknameResult;
import com.example.vibecap_back.domain.member.dto.request.ChangeNicknameRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UpdateMemberInfoTest {

    private MemberRepository memberRepository = Mockito.mock(MemberRepository.class);
    private MemberInfoService memberInfoService;
    private Member testMember;

    @BeforeEach
    void setUpTest() {
        this.memberInfoService = new MemberInfoService(memberRepository);
        this.testMember = MemberFactory.selectMember();
    }

    @Test
    void 닉네임_수정_테스트() {
        // given
        ChangeNicknameResult result;
        ChangeNicknameRequest request = new ChangeNicknameRequest(testMember.getMemberId(), "newNickName");
        String newNickname = "Eren";

        // when : 닉네임 수정
        Mockito.when(memberRepository.findById(testMember.getMemberId()))
                .thenReturn(Optional.of(testMember));

        Member changedMember = testMember;
        changedMember.setNickname(newNickname);
        Mockito.when(memberRepository.save(changedMember))
                .thenReturn(changedMember);
        result = memberInfoService.updateMemberNickname(request);

        // then
        Assertions.assertThat(result.getNewNickname()).isEqualTo(request.getNewNickname());
    }
}
