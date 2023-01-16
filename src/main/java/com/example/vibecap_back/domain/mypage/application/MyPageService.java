package com.example.vibecap_back.domain.mypage.application;

import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.mypage.dao.MyPageRepository;
import com.example.vibecap_back.domain.mypage.dto.GetMyPageResult;
import com.example.vibecap_back.domain.mypage.dto.request.GetMyPageRequest;
import com.example.vibecap_back.global.common.response.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyPageService {

    private final Logger LOGGER = LoggerFactory.getLogger(MyPageService.class);

    private final MyPageRepository myPageRepository;

    @Autowired
    public MyPageService(MyPageRepository myPageRepository) {
        this.myPageRepository = myPageRepository;
    }


    // 마이페이지 조회 (사용자 정보 load)
    public GetMyPageResult getMyPage(GetMyPageRequest request) throws BaseException {

        Optional<Member> optionalMember = myPageRepository.findById(request.getMemberId());
        Member member = optionalMember.get();

        return new GetMyPageResult(member.getMemberId(), member.getEmail(),
                member.getGmail(), member.getNickname(), member.getProfileImage());
    }
}
