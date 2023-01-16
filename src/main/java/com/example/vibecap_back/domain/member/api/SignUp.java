package com.example.vibecap_back.domain.member.api;

import com.example.vibecap_back.domain.member.application.SignService;
import com.example.vibecap_back.domain.member.dto.MemberDto;
import com.example.vibecap_back.domain.member.dto.SignUpResult;
import com.example.vibecap_back.domain.member.dto.request.SignUpRequest;
import com.example.vibecap_back.domain.member.exception.EmailAlreadyExistException;
import com.example.vibecap_back.domain.model.Authority;
import com.example.vibecap_back.domain.model.MemberStatus;
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
@RequestMapping("/app/sign-api")
public class SignUp {

    private SignService signService;

    @Autowired
    public SignUp(SignService signService) {
        this.signService = signService;
    }

    /**
     * Google 계정 없이 계정 생성.
     * @param request email, password, nickname
     * @return member_id
     */
    @PostMapping("/sign-up")
    public BaseResponse<SignUpResult> signupWithoutGmail(
            @RequestBody SignUpRequest request) {

        MemberDto memberDto = MemberDto.builder()
                                .email(request.getEmail())
                                .password(request.getPassword())
                                .role(Authority.ROLE_MEMBER.toString())
                                .nickname(request.getNickname())
                                .status(MemberStatus.ACTIVE.toString())
                                .build();

        MemberDto createdMemberDto;
        SignUpResult result;
        BaseResponse<SignUpResult> response;

        try {
            // 계정 생성 시도
            createdMemberDto = signService.signUp(memberDto);
            result = new SignUpResult(createdMemberDto.getMemberId());
            response = new BaseResponse<>(result);
        } catch (EmailAlreadyExistException e) {
            // 이미 회원가입에 사용된 이메일이였던 경우
            response = new BaseResponse<>(BaseResponseStatus.EMAIL_ALREADY_EXIST);
        }

        return response;
    }

}
