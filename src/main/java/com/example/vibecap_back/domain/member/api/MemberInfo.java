package com.example.vibecap_back.domain.member.api;

import com.example.vibecap_back.domain.member.application.MemberInfoService;
import com.example.vibecap_back.domain.member.dto.ChangeNicknameResult;
import com.example.vibecap_back.domain.member.dto.QuitResult;
import com.example.vibecap_back.domain.member.dto.request.ChangeNicknameRequest;
import com.example.vibecap_back.domain.member.dto.request.QuitRequest;
import com.example.vibecap_back.global.common.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

<<<<<<< HEAD
=======
import java.awt.desktop.QuitResponse;

>>>>>>> 7a50d302fb7694d19f611a87898caf97f5bb1dbb
@RestController
@RequestMapping("/app/member")
public class MemberInfo {

    private final MemberInfoService memberInfoService;

    @Autowired
    public MemberInfo(MemberInfoService memberInfoService) {
        this.memberInfoService = memberInfoService;
    }

    /**
     * 회원의 닉네임 업데이트
     * @param request
     * @return
     * 수정된 이후의 닉네임 반환
     */
    @PatchMapping("/nickname")
    public BaseResponse<ChangeNicknameResult> changeNickname(
            @RequestBody ChangeNicknameRequest request) {

        ChangeNicknameResult result = memberInfoService.updateMemberNickname(request);

        return new BaseResponse<>(result);
    }

    @PatchMapping("/quit")
    public BaseResponse<QuitResult> quitMember(
            @RequestBody QuitRequest request) {

        QuitResult result = memberInfoService.updateMemberStatus(request);

        return new BaseResponse<>(result);
    }
}
