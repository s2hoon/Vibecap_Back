package com.example.vibecap_back.domain.mypage.api;

import com.example.vibecap_back.domain.mypage.application.MyPageService;
import com.example.vibecap_back.domain.mypage.dto.response.GetMyPageResponse;
import com.example.vibecap_back.domain.mypage.exception.InvalidMemberException;
import com.example.vibecap_back.global.common.response.BaseException;
import com.example.vibecap_back.global.common.response.BaseResponse;
import com.example.vibecap_back.global.common.response.BaseResponseStatus;
import com.example.vibecap_back.global.config.storage.FileSaveErrorException;
import com.example.vibecap_back.global.config.storage.FireBaseService;
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

    private final MyPageService myPageService;
    private final FireBaseService fireBaseService;

    @Autowired
    public MyPage(MyPageService myPageService, FireBaseService fireBaseService) {
        this.myPageService = myPageService;
        this.fireBaseService = fireBaseService;
    }


    /**
     * 마이페이지 조회
     * [GET] /app/my-page/:member_id
     */
    @ResponseBody
    @GetMapping("/{member_id}")
    public BaseResponse<GetMyPageResponse> getMyPage(@PathVariable("member_id") Long memberId) {
        try {
            myPageService.checkMemberValid(memberId);
            GetMyPageResponse getMyPageResponse = myPageService.getMyPage(memberId);

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
    @PostMapping(value = "/profile-image")
    public BaseResponse<String> updateProfileImage(@RequestParam(name = "member_id") Long memberId,
                                                   @RequestParam(name = "profile_image") MultipartFile profileImage) {
        try {
            if (memberId == null) {
                return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR);
            }
            if (profileImage.isEmpty()) {
                return new BaseResponse<>(BaseResponseStatus.EMPTY_PROFILE_IMAGE);
            }

            myPageService.checkMemberValid(memberId);

            String profileImgUrl = fireBaseService.uploadFiles(profileImage, "profile_image" + memberId);
            myPageService.updateProfileImage(memberId, profileImage); // profileImgUrl 전달해서 DB에 저장해야 함

            return new BaseResponse<>(profileImgUrl);
        } catch (InvalidMemberException e) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_MEMBER_JWT);
        } catch (FileSaveErrorException e) {
            return new BaseResponse<>(BaseResponseStatus.FILE_SAVE_ERROR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
