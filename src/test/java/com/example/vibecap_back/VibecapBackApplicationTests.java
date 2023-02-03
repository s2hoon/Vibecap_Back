package com.example.vibecap_back;

import com.example.vibecap_back.domain.comment.application.CommentService;
import com.example.vibecap_back.domain.comment.application.SubCommentService;
import com.example.vibecap_back.domain.comment.dao.CommentRepository;
import com.example.vibecap_back.domain.comment.domain.Comments;
import com.example.vibecap_back.domain.comment.domain.SubComment;
import com.example.vibecap_back.domain.member.application.SignService;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.member.dto.MemberDto;
import com.example.vibecap_back.domain.member.exception.EmailAlreadyExistException;
import com.example.vibecap_back.domain.model.Authority;
import com.example.vibecap_back.domain.model.MemberStatus;
import com.example.vibecap_back.domain.post.application.PostService;
import com.example.vibecap_back.domain.post.dao.PostsRepository;
import com.example.vibecap_back.domain.post.domain.Post;
import com.example.vibecap_back.domain.post.dto.Response.PostResponseDto;
import com.example.vibecap_back.domain.vibe.application.VibeService;
import com.example.vibecap_back.domain.vibe.dao.VibeRepository;
import com.example.vibecap_back.domain.vibe.domain.Vibe;
import com.example.vibecap_back.factory.MemberFactory;
import com.example.vibecap_back.factory.PostFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class VibecapBackApplicationTests {

    @Autowired private SignService signService;
    @Autowired private VibeRepository vibeRepository;
    @Autowired private PostsRepository postsRepository;
    @Autowired private CommentRepository commentRepository;

    private static final String IMAGE_URL = "https://cloud.google.com/static/vision/docs/images/setagaya_small.jpeg";
    private static final String IMAGE_LABEL ="꽃";
    private static final String VIDEO_LINK ="https://www.youtube.com/watch?v=7b2Nbr34BW4";
    private static final String[] seasons = {"봄", "여름", "가을", "겨울"};
    private static final String[] times = {"아침", "점심", "저녁"};
    private static final String[] feelings ={"신나는", "포근한", "신선한", "낭만적인", "잔잔한", "우울한", "공허한"};
    private static final int LABEL_IDX = 0;
    private static final int SEASON_IDX = 1;
    private static final int TIME_IDX = 2;
    private static final int FEELING_IDX = 3;

    private List<Member> members = new ArrayList<>();   // (배열 인덱스) + 1 = 회원 id
    private Map<Long, List<Post>> posts = new HashMap<>();

    @Test
    void insertDummyData() {
        try {
            insertDummyMembers(10);
        } catch (EmailAlreadyExistException e) {
            Assertions.fail(e.getMessage());
        }
        insertDummyVibesAndPosts(5);
    }

    /**
     * n명의 dummy member 회원가입
     */
    void insertDummyMembers(int n) throws EmailAlreadyExistException {
        Member dummy;
        MemberDto memberDto;
        for (int i=1; i<=n; i++) {
            dummy = MemberFactory.selectMember(i);
            members.add(dummy);
            memberDto = MemberDto.builder()
                    .email(dummy.getEmail())
                    .password(dummy.getPassword())
                    .role(Authority.ROLE_MEMBER.toString())
                    .nickname(dummy.getNickname())
                    .status(MemberStatus.ACTIVE.toString())
                    .build();
            signService.signUp(memberDto);
        }
    }

    /**
     * 회원 1명이 n개의 dummy vibe, post 생성
     * @param n
     */
    void insertDummyVibesAndPosts(int n) {
        Vibe dummyVibe;
        Post dummyPost;
        // "{label} {season} {time} {feeling}"
        String[] keywords = new String[4];
        keywords[LABEL_IDX] = IMAGE_LABEL;
        for (Member member : members) {
            List<Post> postOfMember = new ArrayList<>();
            for (int i=1; i<=n; i++) {
                keywords[SEASON_IDX] = seasons[(i-1) % (seasons.length)];
                keywords[TIME_IDX] = times[(i-1) % (times.length)];
                keywords[FEELING_IDX] = feelings[(i-1) % (feelings.length)];
                dummyVibe = Vibe.builder()
                        .memberId((long) i)
                        .vibeImage(IMAGE_URL)
                        .youtubeLink(VIDEO_LINK)
                        .vibeKeywords(String.join(" ", keywords))
                        .build();
                vibeRepository.save(dummyVibe);

                dummyPost = PostFactory.getPost(member, dummyVibe, i);
                postOfMember.add(dummyPost);
                postsRepository.save(dummyPost);
            }
            posts.put(member.getMemberId(), postOfMember);

        }
    }

    void insertDummyComments(int n) {
        Comments comment;
        //
    }

}
