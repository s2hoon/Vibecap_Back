package com.example.vibecap_back.domain.mypage.api;

import com.example.vibecap_back.domain.mypage.application.SettingService;
import com.example.vibecap_back.domain.mypage.dao.MyPageRepository;
import com.example.vibecap_back.domain.mypage.dto.response.SaveGmailResponse;
import com.example.vibecap_back.domain.mypage.dto.request.SaveGmailRequest;
import com.example.vibecap_back.domain.mypage.exception.InvalidMemberException;
import com.example.vibecap_back.global.common.response.BaseException;
import com.example.vibecap_back.global.common.response.BaseResponse;
import com.example.vibecap_back.global.common.response.BaseResponseStatus;
import com.example.vibecap_back.global.config.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 설정
 */
@RestController
@RequestMapping("/app/my-page/setting")
public class Setting {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final MyPageRepository myPageRepository;
    private final SettingService settingService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public Setting(MyPageRepository myPageRepository, SettingService settingService, JwtTokenProvider jwtTokenProvider) {
        this.myPageRepository = myPageRepository;
        this.settingService = settingService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 구글 계정 연동(저장)
     * [PATCH] /app/my-page/setting/sync-gmail
     */
    @ResponseBody
    @PatchMapping("/sync-gmail")
    public BaseResponse<SaveGmailResponse> updateGmail(@RequestBody SaveGmailRequest request) {

        try {
            settingService.checkMemberValid(request.getMemberId());
            SaveGmailResponse saveGmailResponse = settingService.updateGmail(request);

            return new BaseResponse<>(saveGmailResponse);
        } catch (InvalidMemberException e) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_MEMBER_JWT);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

}
