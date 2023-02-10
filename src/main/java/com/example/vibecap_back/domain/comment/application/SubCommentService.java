package com.example.vibecap_back.domain.comment.application;

import com.example.vibecap_back.domain.comment.dao.CommentRepository;
import com.example.vibecap_back.domain.comment.dao.SubCommentRepository;
import com.example.vibecap_back.domain.comment.domain.Comments;
import com.example.vibecap_back.domain.comment.domain.SubComment;
import com.example.vibecap_back.domain.comment.dto.SubCommentDto;
import com.example.vibecap_back.domain.comment.dto.SubCommentSaveRequestDto;
import com.example.vibecap_back.domain.comment.exception.NotFoundSubCommentException;
import com.example.vibecap_back.domain.comment.exception.NotFoundCommentException;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.notice.application.NoticeManager;
import com.example.vibecap_back.domain.post.dao.PostsRepository;
import com.example.vibecap_back.domain.post.domain.Post;
import com.example.vibecap_back.global.common.response.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubCommentService {
    private final SubCommentRepository subCommentRepository;
    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;
    private final NoticeManager noticeManager;

    @Autowired
    public SubCommentService(SubCommentRepository subCommentRepository,
                             CommentRepository commentRepository,
                             PostsRepository postsRepository,
                             NoticeManager noticeManager) {
        this.subCommentRepository = subCommentRepository;
        this.commentRepository = commentRepository;
        this.postsRepository = postsRepository;
        this.noticeManager = noticeManager;
    }

    // 대댓글 작성
    @Transactional
    public SubCommentDto writeSubComment(Long commentId, SubCommentSaveRequestDto subCommentSaveRequestDto, Member member) throws BaseException, NotFoundCommentException {
        SubComment subComment = new SubComment();
        subComment.setSubCommentBody(subCommentSaveRequestDto.getSubCommentBody());

        Comments comments = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);

        // 대댓글이 달릴 댓글의 게시물 찾기
        Post post = postsRepository.findById(comments.getPost().getPostId()).get();

        subComment.setComments(comments);
        subComment.setMember(member);
        subComment.setPost(post);

        subComment.mappingComment(comments);

        subCommentRepository.save(subComment);

        /* 대댓글 알림 전송 (본인을 제외한 댓글 작성자에게만 전송)
         * member: 대댓글을 작성하는 회원
         */
        if (member.getMemberId() != comments.getMember().getMemberId())
            noticeManager.sendNotice(subComment);

        return SubCommentDto.toDto(subComment);
    }

    // 대댓글 수정
    @Transactional
    public SubCommentDto updateSubComment(Long subCommentId, SubCommentSaveRequestDto subCommentSaveRequestDto) throws BaseException, NotFoundSubCommentException {
        SubComment subComment = subCommentRepository.findById(subCommentId).orElseThrow(NotFoundSubCommentException::new);

        subComment.setSubCommentBody(subCommentSaveRequestDto.getSubCommentBody());
        subCommentRepository.save(subComment);

        return SubCommentDto.toDto(subComment);
    }

    // 대댓글 삭제
    @Transactional
    public void deleteSubComment(Long subCommentId) throws BaseException, NotFoundSubCommentException {
        SubComment subComment = subCommentRepository.findById(subCommentId).orElseThrow(NotFoundSubCommentException::new);
        subCommentRepository.deleteById(subComment.getSubCommentId());
    }
}
