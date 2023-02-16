package com.example.vibecap_back;

import com.example.vibecap_back.domain.album.api.Album;
import com.example.vibecap_back.domain.album.application.AlbumService;
import com.example.vibecap_back.domain.comment.dao.CommentRepository;
import com.example.vibecap_back.domain.comment.domain.Comments;
import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.notice.application.NoticeManager;
import com.example.vibecap_back.domain.notice.dao.NoticeCommentRepository;
import com.example.vibecap_back.domain.notice.dao.NoticeLikeRepository;
import com.example.vibecap_back.domain.notice.dao.NoticeSubCommentRepository;
import com.example.vibecap_back.domain.post.dao.PostsRepository;
import com.example.vibecap_back.domain.post.domain.Post;
import com.example.vibecap_back.domain.vibe.application.VibeService;
import com.example.vibecap_back.domain.vibe.dao.VibeRepository;
import com.example.vibecap_back.domain.vibe.domain.Vibe;
import com.example.vibecap_back.factory.CommentFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class VibecapBackApplicationTests {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private VibeRepository vibeRepository;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private NoticeCommentRepository commentNoticeRepository;
    @Autowired
    private NoticeSubCommentRepository subCommentNoticeRepository;
    @Autowired
    private NoticeLikeRepository likeNoticeRepository;
    private NoticeManager noticeManager;

    private static final int TEST_MEMBER_NUM =10;
    private static final int VIBE_AND_POST_PER_MEMBER = 5;
    private List<Member> members;
    private Map<Long, List<Vibe>> vibes;
    private Map<Long, List<Post>> posts;

    @PostConstruct
    void initNoticeManager() {
        noticeManager = new NoticeManager(commentNoticeRepository,
                subCommentNoticeRepository, likeNoticeRepository);
    }

    @Test
    void insertDemoData() throws Exception {
        // prepare data
        BoilerPlate boilerPlate = new BoilerPlate(TEST_MEMBER_NUM, VIBE_AND_POST_PER_MEMBER);
        members = boilerPlate.getMembers();
        vibes = boilerPlate.getVibes();
        posts = boilerPlate.getPosts();
        // insert members, vibes, posts
        boilerPlate.persist(memberRepository, vibeRepository, postsRepository, VIBE_AND_POST_PER_MEMBER, true);
        // insert comments and notices
        Comments comment;
        int index, targetIndex;         // 댓글을 작성할 회원, 댓글이 추가될 게시글 작성자
        List<Post> targetPosts;
        // when : testMember[index]가 testMember[targetIndex]의 게시글에 댓글을 작성한다.
        for (int i=0; i<TEST_MEMBER_NUM; i++) {
            targetIndex = ((i+1)%TEST_MEMBER_NUM);
            targetPosts = posts.get(members.get(targetIndex).getMemberId());
            for (Post post : targetPosts) {
                comment = CommentFactory.getComment(members.get(i), post, i);
                commentRepository.save(comment);
                noticeManager.sendNotice(comment);
            }
        }
        // insert likes
    }
}
