package com.example.vibecap_back.domain.notice;

import com.example.vibecap_back.domain.comment.domain.Comments;
import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.notice.application.NoticeManager;
import com.example.vibecap_back.domain.notice.dao.NoticeCommentRepository;
import com.example.vibecap_back.domain.notice.dao.NoticeLikeRepository;
import com.example.vibecap_back.domain.notice.dao.NoticeSubCommentRepository;
import com.example.vibecap_back.domain.notice.domain.NoticeLike;
import com.example.vibecap_back.domain.post.dao.PostsRepository;
import com.example.vibecap_back.domain.post.domain.Like.Likes;
import com.example.vibecap_back.domain.post.domain.Post;
import com.example.vibecap_back.domain.vibe.dao.VibeRepository;
import com.example.vibecap_back.domain.vibe.domain.Vibe;
import com.example.vibecap_back.factory.LikeFactory;
import com.example.vibecap_back.factory.MemberFactory;
import com.example.vibecap_back.factory.PostFactory;
import com.example.vibecap_back.factory.VibeFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository layer test
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Commit
public class NoticeManagerTest {

    @Autowired
    private NoticeLikeRepository likeRepository;
    @Autowired
    private NoticeCommentRepository commentRepository;
    @Autowired
    private NoticeSubCommentRepository subCommentRepository;
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private VibeRepository vibeRepository;
    @Autowired NoticeLikeRepository noticeLikeRepository;
    private NoticeManager noticeManager;

    // test members (id : 1~TEST_MEMBER_NUM)
    static final int TEST_MEMBER_NUM = 10;
    List<Member> testMembers = new ArrayList<>(TEST_MEMBER_NUM);
    // test vibes
    static final int TEST_VIBE_PER_MEMBER = 5;
    List<Vibe> testVibes = new ArrayList<>(TEST_VIBE_PER_MEMBER);
    // test posts
    Map<Long, List<Post>> testPosts = new HashMap<>(TEST_MEMBER_NUM);

    @PostConstruct
    void init() {
        noticeManager = new NoticeManager(commentRepository,
                subCommentRepository, likeRepository);
        initMembers();
        initVibesAndPosts();
    }

    @Test
    void 좋아요_알림_전송_조회_테스트() {
        // given
        List<Likes> testLikes;
        Likes like;
        int index;          // 좋아요를 누를 member의 index (0~TEST_MEMBER_NUM-1)
        int targetIdx;      // 좋아요를 누를 게시글 작성자 index (1~TEST_MEMBER_NUM-1, 0)
        List<Post> targetPosts;     // targetId의 모든 게시글
        // when : memberId가 (memberId+1)의 모든 게시글에 좋아요를 누른다.
        for (int i=0; i<TEST_MEMBER_NUM; i++) {
            targetIdx = ((i+1) % TEST_MEMBER_NUM);
            targetPosts = testPosts.get(testMembers.get(targetIdx).getMemberId());
            for (Post post : targetPosts) {
                like = LikeFactory.getLike(testMembers.get(i), post);
                noticeManager.sendNotice(like);
            }
        }
        // then
        List<NoticeLike> actualNotices;
        Member receiver, expectedSender;
        for (int i=0; i<TEST_MEMBER_NUM; i++) {
            receiver = testMembers.get((i+1)%TEST_MEMBER_NUM);
            expectedSender = testMembers.get(i);
            actualNotices = noticeLikeRepository.selectAllLikeNotices(receiver.getMemberId());
            for (NoticeLike actualNotice : actualNotices) {
                if (!actualNotice.getSenderNickname().equals(expectedSender.getNickname()))
                    Assertions.fail("failed");
            }
        }
    }

    @Test
    void 댓글_알림_전송_조회_테스트() {
        // given
        List<Comments> testComments;
        //
    }

    // id가 1~TEST_MEMBER_NUM인 회원 생성
    void initMembers() {
        this.testMembers = new ArrayList<>();
        Member newMember;
        for (int i=1;i<=TEST_MEMBER_NUM;i++) {
            newMember = MemberFactory.getMember(i);
            testMembers.add(newMember);
            memberRepository.save(newMember);
        }
    }

    // TEST_MEMBER_NUM 명의 회원이 각각 TEST_VIBE_PER_MEMBER 개의 vibe 생성하고
    // 게시글까지 작성
    void initVibesAndPosts() {
        this.testVibes = new ArrayList<>();
        Vibe vibe;

        for (Member member : testMembers) {
            createVibeAndPost(member, TEST_VIBE_PER_MEMBER);
        }
    }

    // 각 회원이 n개의 vibe를 생성하고 게시글 작성
    void createVibeAndPost(Member member, int n) {
        Vibe vibe;
        Post post;
        List<Post> posts = new ArrayList<>(n);
        for (int i=0;i<n;i++) {
            vibe = VibeFactory.getVibe(member.getMemberId(), i);
            vibeRepository.save(vibe);
            post = PostFactory.getPost(member, vibe, i);
            postsRepository.save(post);

            posts.add(post);
        }
        testPosts.put(member.getMemberId(), posts);
    }
}
