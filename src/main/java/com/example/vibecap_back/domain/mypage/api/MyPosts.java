package com.example.vibecap_back.domain.mypage.api;

import com.example.vibecap_back.domain.mypage.application.MyPageService;
import com.example.vibecap_back.domain.mypage.dao.MyPageRepository;
import com.example.vibecap_back.domain.mypage.dto.response.GetMyPostsResponse;
import com.example.vibecap_back.domain.mypage.dto.request.GetMyPostsRequest;
import com.example.vibecap_back.domain.mypage.exception.InvalidMemberException;
import com.example.vibecap_back.global.common.response.BaseException;
import com.example.vibecap_back.global.common.response.BaseResponse;
import com.example.vibecap_back.global.common.response.BaseResponseStatus;
import com.example.vibecap_back.global.config.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/my-page/posts")
public class MyPosts {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final MyPageRepository myPageRepository;
    private final MyPageService myPageService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public MyPosts(MyPageRepository myPageRepository, MyPageService myPageService, JwtTokenProvider jwtTokenProvider) {
        this.myPageRepository = myPageRepository;
        this.myPageService = myPageService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 내 게시물 (전체) 조회
     * [GET] /app/my-page/posts
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetMyPostsResponse>> getMyPosts(@RequestBody GetMyPostsRequest request) {
        try {
            myPageService.checkMemberValid(request.getMemberId());
            List<GetMyPostsResponse> getMyPostsResponse = myPageService.getMyPosts(request);

            return new BaseResponse<>(getMyPostsResponse);
        } catch (InvalidMemberException e) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_MEMBER_JWT);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
