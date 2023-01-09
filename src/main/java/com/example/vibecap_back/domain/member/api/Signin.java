package com.example.vibecap_back.domain.member.api;

import com.example.vibecap_back.domain.member.dto.SigninResultDto;
import com.example.vibecap_back.domain.member.dto.SignupResultDto;
import com.example.vibecap_back.domain.member.dto.request.SigninWithoutGmailRequest;
import com.example.vibecap_back.global.common.response.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 로그인
 */
@RestController
@RequestMapping("/app/member")
public class Signin {
    /**
     * 구글 계정 없이 로그인
     */
    @PostMapping("/signin")
    public BaseResponse<SigninResultDto> signinWithoutGmail(
            @RequestBody SigninWithoutGmailRequest request) {

        return null;
    }
}
