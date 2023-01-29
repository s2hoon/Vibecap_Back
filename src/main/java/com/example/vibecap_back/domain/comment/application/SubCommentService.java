package com.example.vibecap_back.domain.comment.application;

import com.example.vibecap_back.domain.comment.dao.CommentRepository;
import com.example.vibecap_back.domain.comment.dao.SubCommentRepository;
import com.example.vibecap_back.domain.comment.domain.Comments;
import com.example.vibecap_back.domain.comment.domain.SubComment;
import com.example.vibecap_back.domain.comment.dto.SubCommentDto;
import com.example.vibecap_back.domain.comment.dto.SubCommentSaveRequestDto;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.post.dao.PostsRepository;
import com.example.vibecap_back.domain.post.domain.Post;
import com.example.vibecap_back.global.common.response.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubCommentService {
    private final SubCommentRepository subCommentRepository;
    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;

    @Autowired
    public SubCommentService(SubCommentRepository subCommentRepository, CommentRepository commentRepository, PostsRepository postsRepository) {
        this.subCommentRepository = subCommentRepository;
        this.commentRepository = commentRepository;
        this.postsRepository = postsRepository;
    }

    // 대댓글 작성
    public SubCommentDto writeSubComment(Long commentId, SubCommentSaveRequestDto subCommentSaveRequestDto, Member member) throws BaseException {
        SubComment subComment = new SubComment();
        subComment.setSubCommentBody(subCommentSaveRequestDto.getSubCommentBody());

        Optional<Comments> optionalComments = commentRepository.findById(commentId);
        Comments comments = optionalComments.get();

        // 대댓글이 달릴 댓글의 게시물 찾기
        Post post = postsRepository.findById(comments.getPost().getPostId()).get();

        subComment.setComments(comments);
        subComment.setMember(member);
        subComment.setPost(post);
        subCommentRepository.save(subComment);

        return SubCommentDto.toDto(subComment);
    }


}
