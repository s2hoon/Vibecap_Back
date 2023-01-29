package com.example.vibecap_back.domain.member;

import com.example.vibecap_back.domain.member.application.MemberInfoService;
import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.member.dto.ChangeNicknameResult;
import com.example.vibecap_back.domain.member.dto.request.ChangeNicknameRequest;
import com.example.vibecap_back.global.config.storage.FireBaseService;
import com.example.vibecap_back.util.FileWorker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.Optional;

import static com.example.vibecap_back.domain.member.MemberFactory.TEST_MEMBER_ID;

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
