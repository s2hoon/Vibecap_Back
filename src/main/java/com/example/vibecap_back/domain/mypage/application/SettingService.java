package com.example.vibecap_back.domain.mypage.application;

import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.mypage.dao.MyPageRepository;
import com.example.vibecap_back.domain.mypage.dto.response.SaveGmailResponse;
import com.example.vibecap_back.domain.mypage.dto.request.SaveGmailRequest;
import com.example.vibecap_back.global.common.response.BaseException;
import com.example.vibecap_back.global.config.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SettingService {

    private final Logger LOGGER = LoggerFactory.getLogger(SettingService.class);

    private final MyPageRepository myPageRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SettingService(MyPageRepository myPageRepository, JwtTokenProvider jwtTokenProvider) {
        this.myPageRepository = myPageRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    // 구글 계정 연동(저장)
    public SaveGmailResponse updateGmail(SaveGmailRequest request) throws BaseException {
        Optional<Member> optionalMember = myPageRepository.findById(request.getMemberId());
        Member member = optionalMember.get();
        member.setGmail(request.getGmail());

        myPageRepository.save(member);

        return new SaveGmailResponse(member.getGmail());
    }

}
