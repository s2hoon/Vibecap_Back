package com.example.vibecap_back.domain.album.application;

import com.example.vibecap_back.domain.album.dao.AlbumRepository;
import com.example.vibecap_back.domain.album.domain.Album;
import com.example.vibecap_back.domain.album.dto.GetAlbumResponse;
import com.example.vibecap_back.domain.album.dto.request.GetAlbumRequest;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.mypage.application.MyPageService;
import com.example.vibecap_back.domain.mypage.dao.MyPageRepository;
import com.example.vibecap_back.domain.vibe.domain.Vibe;
import com.example.vibecap_back.global.common.response.BaseException;
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

    @Autowired
    public AlbumService(AlbumRepository albumRepository, MyPageRepository myPageRepository) {
        this.albumRepository = albumRepository;
        this.myPageRepository = myPageRepository;
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

}