package com.example.vibecap_back.domain.notice.application;

import com.example.vibecap_back.domain.comment.domain.Comments;
import com.example.vibecap_back.domain.comment.domain.SubComment;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.notice.dao.NoticeCommentRepository;
import com.example.vibecap_back.domain.notice.dao.NoticeLikeRepository;
import com.example.vibecap_back.domain.notice.dao.NoticeSubCommentRepository;
import com.example.vibecap_back.domain.notice.domain.NoticeComment;
import com.example.vibecap_back.domain.notice.domain.NoticeLike;
import com.example.vibecap_back.domain.notice.domain.NoticeSubComment;
import com.example.vibecap_back.domain.post.domain.Like.Likes;
import com.example.vibecap_back.domain.post.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 댓글, 대댓글, 좋아요가 추가될 때 알림을 전송 (table에 저장)
 */
@Component
public class NoticeManager {

    private static final int SUMMARY_MAX_LENGTH = 16;

    private NoticeCommentRepository noticeCommentRepository;
    private NoticeSubCommentRepository noticeSubCommentRepository;
    private NoticeLikeRepository noticeLikeRepository;

    @Autowired
    public NoticeManager(NoticeCommentRepository noticeCommentRepository,
                         NoticeSubCommentRepository noticeSubCommentRepository,
                         NoticeLikeRepository noticeLikeRepository) {
        this.noticeCommentRepository = noticeCommentRepository;
        this.noticeSubCommentRepository = noticeSubCommentRepository;
        this.noticeLikeRepository = noticeLikeRepository;
    }

    /**
     * TODO NoticeComment, NoticeSubComment, NoticeLike가 Notice 클래스를 상속받도록 리팩토링
     * TODO sendNotice(<T extends Notice> notice) 로 오버로딩 단순화
     */

    /**
     * 댓글을 작성하는 메서드 내부에서 작동
     * @param comment
     * 내 게시글에 추가된 댓글
     * @return
     */
    public NoticeComment sendNotice(Comments comment) {
        Post targetPost;
        Member receiver;
        Member sender;
        NoticeComment notice;
        String summary;

        targetPost = comment.getPost();     // 댓글이 추가된 게시글
        receiver = targetPost.getMember();  // 게시글 작성자
        sender = comment.getMember();       // 댓글 작성자
        if (comment.getCommentBody().length() > SUMMARY_MAX_LENGTH)
            summary = comment.getCommentBody().substring(0, SUMMARY_MAX_LENGTH) + "...";
        else
            summary = comment.getCommentBody();

        notice = NoticeComment.builder()
                .post(comment.getPost())
                .receiverId(receiver.getMemberId())
                .senderNickname(sender.getNickname())
                .summary(summary)
                .build();

        return noticeCommentRepository.save(notice);
    }

    /**
     * 대댓글을 작성하는 메서드 내부에서 작동
     * 댓글 작성자에게만 알림 전송 (게시글 작성자에게는 알림 전송하지 않음)
     */
    public NoticeSubComment sendNotice(SubComment subComment) {
        Comments targetComment;
        Member receiver;
        Member sender;
        NoticeSubComment notice;

        targetComment = subComment.getComments();     // 대댓글이 추가된 댓글
        receiver = targetComment.getMember();         // 댓글 작성자
        sender = subComment.getMember();              // 대댓글 작성자

        notice = NoticeSubComment.builder()
                .comment(subComment.getComments())
                .receiverId(receiver.getMemberId())
                .senderNickname(sender.getNickname())
                .summary(subComment.getSubCommentBody().substring(0, 12) + "...")
                .build();

        return noticeSubCommentRepository.save(notice);
    }

    /**
     * receiver의 게시글에 sender가 좋아요를 눌렀음을 알림
     * @param like
     * @return
     */
    public NoticeLike sendNotice(Likes like) {
        Post targetPost;
        Member receiver;
        Member sender;
        NoticeLike notice;

        targetPost = like.getPost();        // 좋아요가 추가된 게시글
        receiver = targetPost.getMember();  // 게시글 작성자
        sender = like.getMember();          // 좋아요 누른 사람

        notice = NoticeLike.builder()
                .post(like.getPost())
                .receiverId(receiver.getMemberId())
                .senderNickname(sender.getNickname())
                .build();

        return noticeLikeRepository.save(notice);
    }

    /**
     * 좋아요를 취소한 경우 해당 알림을 아직 읽지 않은 경우 알림 취소
     * @param like
     */
    public void cancelNotice(Likes like) {
        //
    }
}
