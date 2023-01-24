package com.example.vibecap_back.domain.member;

import com.example.vibecap_back.domain.member.application.MemberInfoService;
import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.member.dto.ChangeNicknameResult;
import com.example.vibecap_back.domain.member.dto.request.ChangeNicknameRequest;
import com.example.vibecap_back.util.FileWorker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.Optional;

import static com.example.vibecap_back.domain.member.MemberFactory.TEST_MEMBER_ID;

public class MemberInfoServiceTest {

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

    @Test
    void 프로필_사진_수정_테스트() {
        // given : request parameters
        Long memberId = TEST_MEMBER_ID;
        Long result;
        byte[] image;
        try {
            image = FileWorker.loadFile("kiki.jpeg");
            // when : 프로필 수정
            Mockito.when(memberRepository.findById(memberId))
                    .thenReturn(Optional.of(testMember));
            Mockito.when(memberRepository.save(testMember))
                    .thenReturn(testMember);
            result = memberInfoService.updateProfileImage(memberId, image);
            // then
            Assertions.assertThat(result).isEqualTo(memberId);
        } catch (IOException e) {
            Assertions.fail("파일 변환 실패 : " + e.getMessage());
        } catch (InvalidPathException e) {
            Assertions.fail("존재하지 않는 경로 : " + e.getMessage());
        }

    }
}
