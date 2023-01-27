package com.example.vibecap_back.domain.mypage.application;

import com.example.vibecap_back.domain.mypage.dao.MyLikesRepository;
import com.example.vibecap_back.domain.mypage.dao.MyPageRepository;
import com.example.vibecap_back.domain.mypage.dao.MyPostsRepository;
import com.example.vibecap_back.domain.mypage.dao.MyScrapsRepository;
import com.example.vibecap_back.domain.mypage.dto.response.GetMyLikesResponse;
import com.example.vibecap_back.domain.mypage.dto.response.GetMyPostsResponse;
import com.example.vibecap_back.domain.mypage.dto.response.GetMyScrapsResponse;
import com.example.vibecap_back.global.common.response.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyPostsService {

    private final Logger LOGGER = LoggerFactory.getLogger(MyPostsService.class);

    private final MyPageRepository myPageRepository;
    private final MyPostsRepository myPostsRepository;
    private final MyLikesRepository myLikesRepository;
    private final MyScrapsRepository myScrapsRepository;

    @Autowired
    public MyPostsService(MyPageRepository myPageRepository, MyPostsRepository myPostsRepository, MyLikesRepository myLikesRepository, MyScrapsRepository myScrapsRepository) {
        this.myPageRepository = myPageRepository;
        this.myPostsRepository = myPostsRepository;
        this.myLikesRepository = myLikesRepository;
        this.myScrapsRepository = myScrapsRepository;
    }


    // 내 게시물 (전체) 조회
    public List<GetMyPostsResponse> getMyPosts(Long memberId) throws BaseException {

        return myPostsRepository.findMyPostsByMemberId(memberId)
                .stream().map(GetMyPostsResponse::new).collect(Collectors.toList());
    }

    // 좋아요 한 게시물 (전체) 조회
    public List<GetMyLikesResponse> getMyLikes(Long memberId) throws BaseException {
        List<Long> postIdList = myLikesRepository.findPostIdByMemberId(memberId);

        return myLikesRepository.findMyLikesById(postIdList, memberId)
                .stream().map(GetMyLikesResponse::new).collect(Collectors.toList());
    }

    // 스크랩 한 게시물 (전체) 조회
    public List<GetMyScrapsResponse> getMyScraps(Long memberId) throws BaseException {
        List<Long> postIdList = myScrapsRepository.findPostIdByMemberId(memberId);

        return myScrapsRepository.findMyScrapsById(postIdList, memberId)
                .stream().map(GetMyScrapsResponse::new).collect(Collectors.toList());
    }
}
