package com.example.vibecap_back.domain.comment.api;

import com.example.vibecap_back.domain.comment.application.SubCommentService;
import com.example.vibecap_back.domain.comment.dao.SubCommentRepository;
import com.example.vibecap_back.domain.comment.dto.SubCommentDto;
import com.example.vibecap_back.domain.comment.dto.SubCommentSaveRequestDto;
import com.example.vibecap_back.domain.comment.exception.NotFoundCommentException;
import com.example.vibecap_back.domain.comment.exception.NotFoundSubCommentException;
import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.member.exception.NoSuchMemberExistException;
import com.example.vibecap_back.domain.mypage.application.MyPageService;
import com.example.vibecap_back.domain.mypage.exception.InvalidMemberException;
import com.example.vibecap_back.global.common.response.BaseException;
import com.example.vibecap_back.global.common.response.BaseResponse;
import com.example.vibecap_back.global.common.response.BaseResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/sub/comments")
public class SubCommentApiController {
    private final SubCommentService subCommentService;
    private final MyPageService myPageService;
    private final SubCommentRepository subCommentRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public SubCommentApiController(SubCommentService subCommentService, MyPageService myPageService, SubCommentRepository subCommentRepository, MemberRepository memberRepository) {
        this.subCommentService = subCommentService;
        this.myPageService = myPageService;
        this.subCommentRepository = subCommentRepository;
        this.memberRepository = memberRepository;
    }


    /**
     * 대댓글 작성
     * [POST] /app/sub/comments/:comment_id
     */
    @ResponseBody
    @PostMapping("/{comment_id}")
    public BaseResponse<SubCommentDto> writeSubComment(@PathVariable("comment_id") Long commentId,
                                                       @RequestBody SubCommentSaveRequestDto subCommentSaveRequestDto) {
        try {
            myPageService.checkMemberValid(subCommentSaveRequestDto.getMemberId());

            Long memberId = subCommentSaveRequestDto.getMemberId();
            Member member = memberRepository.findById(memberId).orElseThrow(NoSuchMemberExistException::new);
            SubCommentDto result = subCommentService.writeSubComment(commentId, subCommentSaveRequestDto, member);

            return new BaseResponse<>(result);

        } catch (NoSuchMemberExistException e) {
            return new BaseResponse<>(BaseResponseStatus.NO_SUCH_MEMBER_EXIST);
        } catch (InvalidMemberException e) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_MEMBER_JWT);
        } catch (NotFoundCommentException e) {
            return new BaseResponse<>(BaseResponseStatus.NOT_FOUND_COMMENT);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 대댓글 수정
     * [PATCH] /app/sub/comments/:sub_comment_id
     */
    @ResponseBody
    @PatchMapping("/{sub_comment_id}")
    public BaseResponse<SubCommentDto> updateSubComment(@PathVariable("sub_comment_id") Long subCommentId,
                                                        @RequestBody SubCommentSaveRequestDto subCommentSaveRequestDto) {
        try {
            myPageService.checkMemberValid(subCommentSaveRequestDto.getMemberId());

            Long memberId = subCommentSaveRequestDto.getMemberId();
            memberRepository.findById(memberId).orElseThrow(NoSuchMemberExistException::new);
            SubCommentDto result = subCommentService.updateSubComment(subCommentId, subCommentSaveRequestDto);

            return new BaseResponse<>(result);

        } catch (NoSuchMemberExistException e) {
            return new BaseResponse<>(BaseResponseStatus.NO_SUCH_MEMBER_EXIST);
        } catch (InvalidMemberException e) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_MEMBER_JWT);
        } catch (NotFoundSubCommentException e) {
            return new BaseResponse<>(BaseResponseStatus.NOT_FOUND_SUB_COMMENT);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 대댓글 삭제
     * [DELETE] /app/sub/comments/:sub_comment_id
     */
    @ResponseBody
    @DeleteMapping("/{sub_comment_id}")
    public BaseResponse<String> deleteSubComment(@PathVariable("sub_comment_id") Long subCommentId) {
        try {
            subCommentService.deleteSubComment(subCommentId);
            String result = "해당 대댓글 삭제에 성공했습니다.";

            return new BaseResponse<>(result);

        } catch (NotFoundSubCommentException e) {
            return new BaseResponse<>(BaseResponseStatus.NOT_FOUND_SUB_COMMENT);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
