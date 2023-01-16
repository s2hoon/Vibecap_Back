package com.example.vibecap_back.domain.comment.api;

import com.example.vibecap_back.domain.comment.application.CommentService;
import com.example.vibecap_back.domain.comment.dto.CommentDto;
import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.global.common.response.BaseResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/app/comments")
public class CommentApiController {

    private final CommentService commentService;
    private final MemberRepository memberRepository;

    /** 댓글 작성 API **/
    @ApiOperation(value = "댓글 작성", notes = "게시글에 댓글 작성")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/comments/{PostId}")
    public BaseResponse<CommentDto> writeComment(@PathVariable("PostId") Long PostId,
                                                 @RequestBody CommentDto commentDto)
    {
        BaseResponse<CommentDto> response;
        Member member = memberRepository.findById(1L).get();
        CommentDto result = commentService.writeComment(PostId, commentDto, member);
        response = new BaseResponse<>(result);

        return response;
    }


    /** 댓글 조회 API **/
    @ApiOperation(value = "댓글 불러오기", notes = "게시글에 달린 모든 댓글 불러오기.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/comments/{PostId}")
    public BaseResponse<List<CommentDto>> getComments(@PathVariable("PostId") Long PostId)
    {
        List<CommentDto> result = commentService.getComments(PostId);

        return new BaseResponse<>(result);
    }


    /** 댓글 삭제 API **/
    @ApiOperation(value = "댓글 삭제", notes = "게시글에 달린 댓글 삭제")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/comments/{PostId}/{commentId}")
    public BaseResponse<String> deleteComment(@PathVariable("PostId") Long PostId,
                                                  @PathVariable("commentId") Long commentId)
    {
        String result = commentService.deleteComment(commentId);

        return new BaseResponse<>(result);
    }
}
