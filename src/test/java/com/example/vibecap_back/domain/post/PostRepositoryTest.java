package com.example.vibecap_back.domain.post;

import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.post.dao.PostsRepository;
import com.example.vibecap_back.domain.post.domain.Post;
import com.example.vibecap_back.domain.post.dto.Response.PostResponseDto;
import com.example.vibecap_back.domain.vibe.dao.VibeRepository;
import com.example.vibecap_back.domain.vibe.domain.Vibe;
import com.example.vibecap_back.factory.MemberFactory;
import com.example.vibecap_back.factory.PostFactory;
import com.example.vibecap_back.factory.VibeFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class PostRepositoryTest {

    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private VibeRepository vibeRepository;

    private final int TEST_SIZE = 100;
    private Member author;

    @BeforeEach
    void init() {
        author = createMember();
    }

    @Test
    @DisplayName("dummy data 삽입, 조회")
    void insertAndSelectDummies() {
        try {
            // when : vibe와 그에 대한 post TEST_NUM개 생성
            for (int i = 1; i< TEST_SIZE +1; i++) {
                Vibe dummyVibe = VibeFactory.getVibe(author.getMemberId(), i);
                Vibe savedVibe = vibeRepository.save(dummyVibe);
                Post dummyPost = PostFactory.getPost(author, savedVibe, i);
                Post savedPost = postsRepository.save(dummyPost);
                // then : 저장된 post 조회
                assertThat(savedPost).isEqualTo(PostFactory.selectPost(author, savedVibe, i));
            }
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void post_id로_post_조회_findByPost() {
        // given : vibe TEST_SIZE개에 대한 post 작성
        List<Post> expectedPosts = new ArrayList<>();
        PostResponseDto expectedPostResponseDto;
        PostResponseDto actualPostResponseDto;
        Vibe savedVibe;
        for (int i=1; i<=TEST_SIZE; i++) {
            savedVibe = vibeRepository.save(VibeFactory.getVibe(author.getMemberId(), i));
            expectedPosts.add(postsRepository.save(PostFactory.getPost(author, savedVibe, i)));
        }
        for (int i=1; i<=TEST_SIZE; i++) {
            // when :
            expectedPostResponseDto = new PostResponseDto(expectedPosts.get(i-1));
            actualPostResponseDto = postsRepository.findByPost((long) i).get(0);
            // then : 게시글 id, title, body 확인
            if (!comparePostResponseDto(actualPostResponseDto, expectedPostResponseDto))
                Assertions.fail();
        }
    }

    @Test
    void tag로_post_조회_findByTagName() {
        // given : vibe TEST_SIZE개에 대한 post 작성
        List<Post> expectedPosts = new ArrayList<>();
        Vibe savedVibe;
        for (int i=1; i<=TEST_SIZE; i++) {
            savedVibe = vibeRepository.save(VibeFactory.getVibe(author.getMemberId(), i));
            expectedPosts.add(postsRepository.save(PostFactory.getPost(author, savedVibe, i)));
        }
        // when
    }

    // 회원 1명 생성
    Member createMember() {
        Member dummy = MemberFactory.getMember(1);
        return memberRepository.save(dummy);
    }
    // 회원 n명 생성
    void createMember(int n) {
        IntStream.rangeClosed(1, n).forEach(i->{
            Member dummy = MemberFactory.getMember(i);
            memberRepository.save(dummy);
        });
    }

    // memberId 회원이 n개의 vibe 생성
    void createVibe(Long memberId, int n) {
        IntStream.rangeClosed(1, n).forEach(i->{
            Vibe dummy = VibeFactory.getVibe(memberId, i);
            vibeRepository.save(dummy);
        });
    }

    boolean comparePostResponseDto(PostResponseDto actual, PostResponseDto expected) {
        return ((actual.getPostId() == expected.getPostId()) &&
                (actual.getMemberId() == expected.getMemberId()) &&
                (actual.getTitle().equals(expected.getTitle())) &&
                (actual.getBody().equals(expected.getBody())));
    }
}

