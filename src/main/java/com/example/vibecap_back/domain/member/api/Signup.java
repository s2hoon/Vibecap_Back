package com.example.vibecap_back.domain.member.api;

import com.example.vibecap_back.domain.member.application.MemberService;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.member.dto.MemberDto;
import com.example.vibecap_back.domain.member.exception.EmailAlreadyExistException;
import com.example.vibecap_back.domain.model.Authority;
import com.example.vibecap_back.domain.model.MemberStatus;
import com.example.vibecap_back.domain.model.SignupResult;
import com.example.vibecap_back.domain.model.request.SignupWithoutGmailRequest;
import com.example.vibecap_back.global.common.response.BaseResponse;
import com.example.vibecap_back.global.common.response.BaseResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 가입
 */
@RestController
@RequestMapping("/app/member")
public class Signup {

    private MemberService memberService;

    @Autowired
    public Signup(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * Google 계정 없이 계정 생성.
     * @param request email, password, nickname
     * @return member_id
     */
    @PostMapping("/signup")
    public BaseResponse<SignupResult> signupWithoutGmail(
            @RequestBody SignupWithoutGmailRequest request) {

        MemberDto memberDto = MemberDto.builder()
                                .email(request.getEmail())
                                .password(request.getPassword())
                                .role(Authority.NORMAL)
                                .nickname(request.getNickname())
                                .status(MemberStatus.ACTIVE)
                                .build();

        MemberDto createdMemberDto;
        SignupResult result;
        BaseResponse<SignupResult> response;

        try {
            // 계정 생성 시도
            createdMemberDto = memberService.createMemberWithoutGmail(memberDto);
            result = new SignupResult(createdMemberDto.getMemberId());
            response = new BaseResponse<>(result);
        } catch (EmailAlreadyExistException e) {
            // 이미 회원가입에 사용된 이메일이였던 경우
            response = new BaseResponse<>(BaseResponseStatus.EMAIL_ALREADY_EXIST);
        }

        return response;
    }

//    @PostMapping("/signin")
//    public BaseResponse<Sign>

}
