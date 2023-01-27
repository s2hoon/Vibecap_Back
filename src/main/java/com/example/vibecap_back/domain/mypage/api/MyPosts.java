package com.example.vibecap_back.domain.mypage.api;

import com.example.vibecap_back.domain.mypage.application.MyPageService;
import com.example.vibecap_back.domain.mypage.application.MyPostsService;
import com.example.vibecap_back.domain.mypage.dao.MyPageRepository;
import com.example.vibecap_back.domain.mypage.dto.response.GetMyLikesResponse;
import com.example.vibecap_back.domain.mypage.dto.response.GetMyPostsResponse;
import com.example.vibecap_back.domain.mypage.exception.InvalidMemberException;
import com.example.vibecap_back.global.common.response.BaseException;
import com.example.vibecap_back.global.common.response.BaseResponse;
import com.example.vibecap_back.global.common.response.BaseResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/my-page")
public class MyPosts {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final MyPageRepository myPageRepository;
    private final MyPageService myPageService;
    private final MyPostsService myPostsService;

    @Autowired
    public MyPosts(MyPageRepository myPageRepository, MyPageService myPageService, MyPostsService myPostsService) {
        this.myPageRepository = myPageRepository;
        this.myPageService = myPageService;
        this.myPostsService = myPostsService;
    }

    /**
     * 내 게시물 (전체) 조회
     * [GET] /app/my-page/posts/:member_id
     */
    @ResponseBody
    @GetMapping("/posts/{member_id}")
    public BaseResponse<List<GetMyPostsResponse>> getMyPosts(@PathVariable("member_id") Long memberId) {
        try {
            myPageService.checkMemberValid(memberId);
            List<GetMyPostsResponse> getMyPostsResponse = myPostsService.getMyPosts(memberId);

            return new BaseResponse<>(getMyPostsResponse);
        } catch (InvalidMemberException e) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_MEMBER_JWT);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 좋아요 한 게시물 (전체) 조회
     * [GET] /app/my-page/likes/:member_id
     */
    @ResponseBody
    @GetMapping("/likes/{member_id}")
    public BaseResponse<List<GetMyLikesResponse>> getMyLikes(@PathVariable("member_id") Long memberId) {
        try {
            myPageService.checkMemberValid(memberId);
            List<GetMyLikesResponse> getMyLikesResponses = myPostsService.getMyLikes(memberId);

            return new BaseResponse<>(getMyLikesResponses);
        } catch (InvalidMemberException e) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_MEMBER_JWT);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
