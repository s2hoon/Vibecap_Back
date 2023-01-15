package com.example.vibecap_back.domain.mypage.api;

import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.mypage.application.MyPageService;
import com.example.vibecap_back.domain.mypage.dao.MyPageRepository;
import com.example.vibecap_back.domain.mypage.dto.GetMyPageResult;
import com.example.vibecap_back.domain.mypage.dto.request.GetMyPageRequest;
import com.example.vibecap_back.global.common.response.BaseException;
import com.example.vibecap_back.global.common.response.BaseResponse;
import com.example.vibecap_back.global.common.response.BaseResponseStatus;
import com.example.vibecap_back.global.config.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

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
    public BaseResponse<GetMyPageResult> getMyPage(@RequestBody GetMyPageRequest request) {

        try {
            // JWT
            // jwt에서 email 추출
            String email = jwtTokenProvider.extractEmail();
            Optional<Member> member = myPageRepository.findByEmail(email);
            // memberId와 접근한 회원이 같은지 확인
            if (!Objects.equals(request.getMemberId(), member.get().getMemberId())) {
                return new BaseResponse<>(BaseResponseStatus.INVALID_MEMBER_JWT);
            }

            GetMyPageResult getMyPageResult = myPageService.getMyPage(request);

            return new BaseResponse<>(getMyPageResult);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
