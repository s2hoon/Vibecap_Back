package com.example.vibecap_back.domain.member.api;

import com.example.vibecap_back.domain.member.application.SignService;
import com.example.vibecap_back.domain.member.dto.SignInResult;
import com.example.vibecap_back.domain.member.dto.request.SignInRequest;
import com.example.vibecap_back.domain.member.exception.DisabledMemberException;
import com.example.vibecap_back.domain.member.exception.WrongEmailException;
import com.example.vibecap_back.domain.member.exception.WrongPasswordException;
import com.example.vibecap_back.global.common.response.BaseResponse;
import com.example.vibecap_back.global.common.response.BaseResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 로그인
 */
@RestController
@RequestMapping("/app/sign-api")
public class SignIn {

    private final Logger LOGGER = LoggerFactory.getLogger(SignIn.class);
    private final SignService signService;

    @Autowired
    public SignIn(SignService signService) {
        this.signService = signService;
    }

    /**
     * 구글 계정 없이 로그인
     */
    @PostMapping("/sign-in")
    public BaseResponse<SignInResult> signinWithoutGmail(
            @RequestBody SignInRequest request) {

        BaseResponse<SignInResult> response;

        try {
            SignInResult result = signService.signIn(request);
            response = new BaseResponse<>(result);
        } catch (WrongEmailException emailException) {
            response = new BaseResponse<>(BaseResponseStatus.WRONG_EMAIL);
        } catch (WrongPasswordException pwdException) {
            response = new BaseResponse<>(BaseResponseStatus.WRONG_PASSWORD);
        } catch (DisabledMemberException e) {
            response = new BaseResponse<>(BaseResponseStatus.DISABLED_MEMBER);
        }

        return response;
    }
}
