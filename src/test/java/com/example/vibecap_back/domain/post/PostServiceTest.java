package com.example.vibecap_back.domain.post;

import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.notice.application.NoticeManager;
import com.example.vibecap_back.domain.post.application.PostService;
import com.example.vibecap_back.domain.post.dao.PostsLikeRepository;
import com.example.vibecap_back.domain.post.dao.PostsRepository;
import com.example.vibecap_back.domain.post.dao.PostsScrapRepository;
import com.example.vibecap_back.domain.vibe.dao.VibeRepository;
import com.example.vibecap_back.domain.vibe.domain.Vibe;
import com.example.vibecap_back.factory.MemberFactory;
import com.example.vibecap_back.global.config.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@DataJpaTest
public class PostServiceTest {

    private MemberRepository memberRepository = Mockito.mock(MemberRepository.class);
    private PostsRepository postsRepository = Mockito.mock(PostsRepository.class);
    private PostsLikeRepository postsLikeRepository = Mockito.mock(PostsLikeRepository.class);
    private PostsScrapRepository postsScrapRepository = Mockito.mock(PostsScrapRepository.class);
    private JwtTokenProvider jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
    private NoticeManager noticeManager = Mockito.mock(NoticeManager.class);
    @Autowired
    private VibeRepository vibeRepository;

//    private PostService postService = new PostService(
//            memberRepository, postsRepository, postsLikeRepository,
//            postsScrapRepository, vibeRepository, jwtTokenProvider, noticeManager);
//
//    private static int TEST_SIZE = 50;
//    private Member author;
//    private List<Vibe> vibes = new ArrayList<>();
//
//    @BeforeEach
//    void init() {
//        author = MemberFactory.selectMember();
//    }
//
//    void 태그로_게시글_조회() {
//        //
//    }

}
