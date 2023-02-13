package com.example.vibecap_back.domain.comment.api;

import com.example.vibecap_back.domain.comment.application.CommentService;
import com.example.vibecap_back.domain.comment.dao.CommentRepository;
import com.example.vibecap_back.domain.comment.dao.SubCommentRepository;
import com.example.vibecap_back.domain.comment.dto.CommentDeleteReqDto;
import com.example.vibecap_back.domain.comment.dto.CommentDto;
import com.example.vibecap_back.domain.comment.dto.CommentReadDto;
import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.member.exception.NoSuchMemberExistException;
import com.example.vibecap_back.domain.mypage.exception.InvalidMemberException;
import com.example.vibecap_back.domain.post.application.PostService;
import com.example.vibecap_back.global.common.response.BaseResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.vibecap_back.global.common.response.BaseResponseStatus.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/app/comments")
public class CommentApiController {

    private final CommentService commentService;

    private  final PostService postService;
    private final MemberRepository memberRepository;

    /** 댓글 작성 API **/
    @ApiOperation(value = "댓글 작성", notes = "게시글에 댓글 작성")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{PostId}")
    public BaseResponse<CommentDto> writeComment(@PathVariable("PostId") Long PostId,
                                                 @RequestBody CommentDto commentDto)
    {
        try {
            postService.checkMemberValid(commentDto.getMemberId());

            if(commentDto.getCommentBody().length() > 255)
            {
                return new BaseResponse<>(POST_COMMENT_INVALID_BODY);
            }

            Member member = memberRepository.findById(commentDto.getMemberId()).get();
            CommentDto result = commentService.writeComment(PostId, commentDto, member);
            return new BaseResponse<>(result);

        } catch (InvalidMemberException e) {
            throw new RuntimeException(e);
        }
    }


    /** 댓글 조회 API **/
    @ApiOperation(value = "댓글 불러오기", notes = "게시글에 달린 모든 댓글 불러오기.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{PostId}")
    public BaseResponse<List<CommentReadDto>> getComments(@PathVariable("PostId") Long PostId)
    {
        List<CommentReadDto> result = commentService.getComments(PostId);

        return new BaseResponse<>(result);
    }


    /** 댓글 삭제 API **/
    @ApiOperation(value = "댓글 삭제", notes = "게시글에 달린 댓글 삭제")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{PostId}/{commentId}")
    public BaseResponse<String> deleteComment(@PathVariable("PostId") Long PostId,
                                                  @PathVariable("commentId") Long commentId, @RequestBody CommentDeleteReqDto commentDeleteReqDto)
    {
        try {
            postService.checkMemberValid(commentDeleteReqDto.getMemberId());

            String result = commentService.deleteComment(commentId, PostId);

            return new BaseResponse<>(result);

        } catch (InvalidMemberException e) {
            throw new RuntimeException(e);
        }
    }
}
