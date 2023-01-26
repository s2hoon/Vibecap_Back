package com.example.vibecap_back.domain.album.application;

import com.example.vibecap_back.domain.album.dao.AlbumRepository;
import com.example.vibecap_back.domain.album.domain.Album;
import com.example.vibecap_back.domain.album.dto.GetAlbumResponse;
import com.example.vibecap_back.domain.album.dto.GetVibeResponse;
import com.example.vibecap_back.domain.album.dto.request.GetAlbumRequest;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.mypage.application.MyPageService;
import com.example.vibecap_back.domain.mypage.dao.MyPageRepository;
import com.example.vibecap_back.domain.mypage.exception.InvalidMemberException;
import com.example.vibecap_back.domain.vibe.domain.Vibe;
import com.example.vibecap_back.global.common.response.BaseException;
import com.example.vibecap_back.global.config.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {

    private final Logger LOGGER = LoggerFactory.getLogger(MyPageService.class);

    private final AlbumRepository albumRepository;
    private final MyPageRepository myPageRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AlbumService(AlbumRepository albumRepository, MyPageRepository myPageRepository, JwtTokenProvider jwtTokenProvider) {
        this.albumRepository = albumRepository;
        this.myPageRepository = myPageRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    // 앨범 조회
    public GetAlbumResponse getAlbum(GetAlbumRequest request) throws BaseException {
        Optional<Member> optionalMember = myPageRepository.findById(request.getMemberId());
        Member member = optionalMember.get();

        List<Vibe> myVibe = albumRepository.findByMemberId(member.getMemberId());
        List<Album> vibes = new ArrayList<>();

        for (Vibe vibe : myVibe) {
            vibes.add(new Album(vibe.getVibeId(), vibe.getVibeImage()));
        }

        return new GetAlbumResponse(member.getNickname(), member.getEmail(), member.getGmail(), vibes);
    }

    // 앨범에서 개별 Vibe 조회
    public GetVibeResponse getVibe(Long vibeId) throws BaseException, InvalidMemberException {
//        checkVibeValid(vibeId);

        Optional<Vibe> optionalVibe = albumRepository.findById(vibeId);
        Vibe vibe = optionalVibe.get();

        return new GetVibeResponse(vibe.getVibeId(), vibe.getMemberId(), vibe.getVibeImage(),
                vibe.getYoutubeLink(), vibe.getVibeKeywords());
    }

    // 요청한 vibe 에 접근 가능한 회원인지 검사
//    public void checkVibeValid(Long vibeId) throws InvalidMemberException {
//        // JWT 에서 email 추출
//        String email = jwtTokenProvider.extractEmail();
//        Optional<Member> optionalMember = myPageRepository.findByEmail(email);
//        Member member = optionalMember.get();
//
//        // memberId 추출해서 그 회원이 생성한 vibe 인지 확인
//        List<Long> vibeIdList = albumRepository.findVibeIdByMemberId(member.getMemberId());
//
//        if (!albumRepository.existsVibeIdByMemberId(vibeIdList, vibeId)) {
//            throw new InvalidMemberException();
//        }
//    }
}