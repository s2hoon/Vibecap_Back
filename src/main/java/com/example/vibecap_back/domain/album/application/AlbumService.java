package com.example.vibecap_back.domain.album.application;

import com.example.vibecap_back.domain.album.dao.AlbumRepository;
import com.example.vibecap_back.domain.album.domain.Album;
import com.example.vibecap_back.domain.album.dto.GetAlbumResponse;
import com.example.vibecap_back.domain.album.dto.GetVibeResponse;
import com.example.vibecap_back.domain.album.exception.NoAccessToVibeException;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.mypage.application.MyPageService;
import com.example.vibecap_back.domain.mypage.dao.MyPageRepository;
import com.example.vibecap_back.domain.post.application.PostService;
import com.example.vibecap_back.domain.post.dao.PostsRepository;
import com.example.vibecap_back.domain.post.domain.Post;
import com.example.vibecap_back.domain.post.domain.Tag.Tag;
import com.example.vibecap_back.domain.vibe.domain.Vibe;
import com.example.vibecap_back.global.common.response.BaseException;
import com.example.vibecap_back.global.config.security.JwtTokenProvider;
import com.example.vibecap_back.global.config.storage.FireBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {

    private final Logger LOGGER = LoggerFactory.getLogger(MyPageService.class);

    private final FireBaseService fireBaseService;
    private final PostService postService;
    private final AlbumRepository albumRepository;
    private final MyPageRepository myPageRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AlbumService(FireBaseService fireBaseService,
                        PostService postService,
                        AlbumRepository albumRepository,
                        MyPageRepository myPageRepository, JwtTokenProvider jwtTokenProvider) {
        this.fireBaseService = fireBaseService;
        this.postService = postService;
        this.albumRepository = albumRepository;
        this.myPageRepository = myPageRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    // 앨범 조회
    public GetAlbumResponse getAlbum(Long memberId) throws BaseException {
        Optional<Member> optionalMember = myPageRepository.findById(memberId);
        Member member = optionalMember.get();

        List<Vibe> myVibe = albumRepository.findByMemberId(member.getMemberId());
        List<Album> vibes = new ArrayList<>();

        for (Vibe vibe : myVibe) {
            vibes.add(new Album(vibe.getVibeId(), vibe.getVibeImage()));
        }

        return new GetAlbumResponse(member.getNickname(), member.getEmail(), member.getGmail(), vibes);
    }

    // 앨범에서 개별 Vibe 조회
    public GetVibeResponse getVibe(Long vibeId) throws BaseException, NoAccessToVibeException {
        checkAccessToVibe(vibeId);

        Optional<Vibe> optionalVibe = albumRepository.findById(vibeId);
        Vibe vibe = optionalVibe.get();

        return new GetVibeResponse(vibe.getVibeId(), vibe.getMember().getMemberId(), vibe.getVibeImage(),
                vibe.getYoutubeLink(), vibe.getVibeKeywords());
    }

    // 앨범에서 개별 Vibe 삭제
    @Transactional
    public void deleteVibe(Long vibeId) throws BaseException, NoAccessToVibeException, IOException {
        checkAccessToVibe(vibeId);
        Optional<Vibe> optionalVibe = albumRepository.findById(vibeId);
        Vibe vibe = optionalVibe.get();
        // tag 삭제를 위해 post_id 획득
        Long targetPostId = albumRepository.getPostIdByVibeId(vibeId);
        albumRepository.deleteTagByPostId(targetPostId);

        // firebase 에서 사진 삭제
        String fileName = fireBaseService.getFileName(vibe.getVibeImage());
        fireBaseService.delete(fileName);

        albumRepository.deleteById(vibeId);
    }

    // 요청한 vibe 에 접근 가능한 회원인지 검사
    public void checkAccessToVibe(Long vibeId) throws NoAccessToVibeException {
        // JWT 에서 email 추출
        String email = jwtTokenProvider.extractEmail();
        Optional<Member> optionalMember = myPageRepository.findByEmail(email);
        Member member = optionalMember.get();

        // memberId 추출해서 회원이 생성한 vibe 인지 확인
        List<Long> vibeIdList = albumRepository.findVibeIdByMemberId(member.getMemberId());

        if (!vibeIdList.contains(vibeId)) {
            throw new NoAccessToVibeException();
        }
    }
}