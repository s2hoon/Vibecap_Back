package com.example.vibecap_back.domain.mypage.api;

import com.example.vibecap_back.domain.mypage.application.MyPageService;
import com.example.vibecap_back.domain.mypage.dao.MyPageRepository;
import com.example.vibecap_back.domain.mypage.dto.response.GetMyPageResponse;
import com.example.vibecap_back.domain.mypage.dto.request.GetMyPageRequest;
import com.example.vibecap_back.domain.mypage.exception.InvalidMemberException;
import com.example.vibecap_back.global.common.response.BaseException;
import com.example.vibecap_back.global.common.response.BaseResponse;
import com.example.vibecap_back.global.common.response.BaseResponseStatus;
import com.example.vibecap_back.global.config.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 마이페이지
 */
@RestController
@RequestMapping("/app/my-page")
public class MyPage {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final MyPageRepository myPageRepository;
    private final MyPageService myPageService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public MyPage(MyPageRepository myPageRepository, MyPageService myPageService, JwtTokenProvider jwtTokenProvider) {
        this.myPageRepository = myPageRepository;
        this.myPageService = myPageService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 마이페이지 조회
     * [GET] /app/my-page
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetMyPageResponse> getMyPage(@RequestBody GetMyPageRequest request) {
        try {
            myPageService.checkMemberValid(request.getMemberId());
            GetMyPageResponse getMyPageResponse = myPageService.getMyPage(request);

            return new BaseResponse<>(getMyPageResponse);
        } catch (InvalidMemberException e) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_MEMBER_JWT);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 프로필 이미지 변경
     * [PATCH] /app/my-page/profile-image
     */
    @ResponseBody
    @PatchMapping(value = "/profile-image")
    public BaseResponse<String> updateProfileImage(@RequestPart(name = "member_id") Long memberId, @RequestPart(name = "profile_image") MultipartFile profileImage) {
        try {
            if (memberId == null) {
                return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR);
            }
            if (profileImage.isEmpty()) {
                return new BaseResponse<>(BaseResponseStatus.EMPTY_PROFILE_IMAGE);
            }
            myPageService.checkMemberValid(memberId);
            myPageService.updateProfileImage(memberId, profileImage);

            String result = "프로필 이미지 변경에 성공했습니다.";
            return new BaseResponse<>(result);
        } catch (InvalidMemberException e) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_MEMBER_JWT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
