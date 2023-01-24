package com.example.vibecap_back.domain.mypage.api;

import com.example.vibecap_back.domain.mypage.application.MyPageService;
import com.example.vibecap_back.domain.mypage.application.MyPostsService;
import com.example.vibecap_back.domain.mypage.dao.MyPageRepository;
import com.example.vibecap_back.domain.mypage.dto.response.GetMyPostsResponse;
import com.example.vibecap_back.domain.mypage.dto.request.GetMyPostsRequest;
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
     * [GET] /app/my-page/posts
     */
    @ResponseBody
    @GetMapping("/posts")
    public BaseResponse<List<GetMyPostsResponse>> getMyPosts(@RequestBody GetMyPostsRequest request) {
        try {
            myPageService.checkMemberValid(request.getMemberId());
            List<GetMyPostsResponse> getMyPostsResponse = myPostsService.getMyPosts(request);

            return new BaseResponse<>(getMyPostsResponse);
        } catch (InvalidMemberException e) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_MEMBER_JWT);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 좋아요 한 게시물 (전체) 조회
     * [GET] /app/my-page/likes
     */
//    @ResponseBody
//    @GetMapping("/likes")
//    public BaseResponse<List<GetMyLikesResponse>> getMyLikes(@RequestBody GetMyPostsRequest request) {
//        try {
//            myPageService.checkMemberValid(request.getMemberId());
//            List<GetMyLikesResponse> getMyLikesResponses = myPostsService.getMyLikes(request);
//
//            return new BaseResponse<>(getMyLikesResponses);
//        } catch (InvalidMemberException e) {
//            return new BaseResponse<>(BaseResponseStatus.INVALID_MEMBER_JWT);
//        } catch (BaseException exception) {
//            return new BaseResponse<>(exception.getStatus());
//        }
//    }
}
