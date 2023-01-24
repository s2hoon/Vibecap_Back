package com.example.vibecap_back.domain.mypage.application;

import com.example.vibecap_back.domain.mypage.dao.MyLikesRepository;
import com.example.vibecap_back.domain.mypage.dao.MyPageRepository;
import com.example.vibecap_back.domain.mypage.dao.MyPostsRepository;
import com.example.vibecap_back.domain.mypage.dto.request.GetMyPostsRequest;
import com.example.vibecap_back.domain.mypage.dto.response.GetMyPostsResponse;
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

    @Autowired
    public MyPostsService(MyPageRepository myPageRepository, MyPostsRepository myPostsRepository, MyLikesRepository myLikesRepository) {
        this.myPageRepository = myPageRepository;
        this.myPostsRepository = myPostsRepository;
        this.myLikesRepository = myLikesRepository;
    }


    // 내 게시물 (전체) 조회
    public List<GetMyPostsResponse> getMyPosts(GetMyPostsRequest request) throws BaseException {

        return myPostsRepository.findMyPostsByMember_id(request.getMemberId())
                .stream().map(GetMyPostsResponse::new).collect(Collectors.toList());
    }

    // 좋아요 한 게시물 (전체) 조회
//    public List<GetMyLikesResponse> getMyLikes(GetMyPostsRequest request) throws BaseException {
//        List<Long> postIdList = myLikesRepository.findPostIdByMemberId(request.getMemberId());
////        LOGGER.info("postIdList: {}", postIdList.toString());
//
//        return myLikesRepository.findMyLikesById(postIdList, request.getMemberId())
//                .stream().map(GetMyLikesResponse::new).collect(Collectors.toList());
//    }

}
