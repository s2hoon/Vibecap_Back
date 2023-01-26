package com.example.vibecap_back.domain.comment.application;

import com.example.vibecap_back.domain.comment.dao.CommentRepository;
import com.example.vibecap_back.domain.comment.dto.CommentDto;
import com.example.vibecap_back.domain.comment.domain.Comments;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.post.dao.PostsRepository;
import com.example.vibecap_back.domain.post.domain.Posts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;

    /** 댓글 작성 **/
    @Transactional
    public CommentDto writeComment(Long PostId, CommentDto commentDto, Member member) {
        Comments comment = new Comments();
        comment.setCommentBody(commentDto.getCommentBody());

        // 게시판 번호로 게시글 찾기
        Posts post = postsRepository.findById(PostId).orElseThrow(() -> {
            return new IllegalArgumentException("게시판을 찾을 수 없습니다.");
        });

        comment.setMember(member);
        comment.setPost(post);
        commentRepository.save(comment);

        return CommentDto.toDto(comment);
    }


    /** 댓글 조회 **/
    @Transactional(readOnly = true)
    public List<CommentDto> getComments(Long PostId) {
        List<Comments> comments = commentRepository.findAllByPost_PostId(PostId);
        List<CommentDto> commentDto = new ArrayList<>();

        comments.forEach(s -> commentDto.add(CommentDto.toDto(s)));
        return commentDto;
    }


    /** 댓글 삭제 **/
    @Transactional
    public String deleteComment(Long commentId) {
        Comments comment = commentRepository.findById(commentId).orElseThrow(()-> {
            return new IllegalArgumentException("댓글 Id를 찾을 수 없습니다.");
        });
        commentRepository.deleteById(commentId);
        return "삭제 완료";
    }
}
