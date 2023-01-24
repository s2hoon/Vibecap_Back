package com.example.vibecap_back.domain.member.api;

import com.example.vibecap_back.domain.member.application.MemberInfoService;
import com.example.vibecap_back.domain.member.dto.ChangeNicknameResult;
import com.example.vibecap_back.domain.member.dto.QuitResult;
import com.example.vibecap_back.domain.member.dto.request.ChangeNicknameRequest;
import com.example.vibecap_back.domain.member.dto.request.QuitRequest;
import com.example.vibecap_back.global.common.response.BaseResponse;
import com.example.vibecap_back.global.common.response.BaseResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @PatchMapping(value = "/profile-image", consumes = "multipart/form-data")
    public BaseResponse<Long> setProfileImage(@RequestParam(name = "member_id") Long memberId,
                                              @RequestParam(name = "image") MultipartFile image) {

        try {
            byte[] data = image.getBytes();
            Long result = memberInfoService.updateProfileImage(memberId, data);
            return new BaseResponse<>(result);
        } catch (IOException e) {
            return new BaseResponse<>(BaseResponseStatus.SAVE_TEMPORARY_FILE_FAILED);
        }
    }
}
