package com.example.vibecap_back.domain.member.api;

import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.global.common.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;

@RestController
@RequestMapping("/app/sign-api")
public class DuplicationCheck {

    private final MemberRepository memberRepository;

    @Autowired
    public DuplicationCheck(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 이미 가입된 이메일인지 확인
     * @param email
     * @return
     * 이미 가입된 이메일이라면 true, 아니라면 false 반환.
     */
    @GetMapping("/email/{email}")
    public BaseResponse<Boolean> emailDuplicationCheck(
            @PathVariable("email") String email) {

        Optional<Member> member = memberRepository.findByEmail(email);

        return new BaseResponse<>(member.isPresent());
    }

    @GetMapping("/nickname/{nickname}")
    public BaseResponse<Boolean> nicknameDuplicationCheck(
            @PathVariable("nickname") String nickname) {

        Optional<Member> member = memberRepository.findByNickname(nickname);

        return new BaseResponse<>(member.isPresent());
    }

}
