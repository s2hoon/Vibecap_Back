package com.example.vibecap_back.domain.mypage.api;

import com.example.vibecap_back.domain.mypage.application.MyPageService;
import com.example.vibecap_back.domain.mypage.dao.MyPageRepository;
import com.example.vibecap_back.domain.mypage.dto.GetMyPageResult;
import com.example.vibecap_back.domain.mypage.dto.request.MyPageRequest;
import com.example.vibecap_back.global.common.response.BaseException;
import com.example.vibecap_back.global.common.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 마이페이지
 */
@RestController
@RequestMapping("/app/my-page")
public class MyPage {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final MyPageRepository myPageRepository;
    private final MyPageService myPageService;

    @Autowired
    public MyPage(MyPageRepository myPageRepository, MyPageService myPageService) {
        this.myPageRepository = myPageRepository;
        this.myPageService = myPageService;
    }

    /**
     * 마이페이지 조회
     * [GET] /app/my-page
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetMyPageResult> loadMyPage(@RequestBody MyPageRequest request) {

        BaseResponse<GetMyPageResult> response;

        try {
            GetMyPageResult getMyPageRes = myPageService.loadMyPage(request);
            response = new BaseResponse<>(getMyPageRes);
        } catch (BaseException exception) {
            response = new BaseResponse<>((exception.getStatus()));
        }

        return response;
    }

}
