package com.example.vibecap_back.domain.comment.api;

import com.example.vibecap_back.domain.comment.application.SubCommentService;
import com.example.vibecap_back.domain.comment.dao.SubCommentRepository;
import com.example.vibecap_back.domain.comment.dto.SubCommentDto;
import com.example.vibecap_back.domain.comment.dto.SubCommentSaveRequestDto;
import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.global.common.response.BaseException;
import com.example.vibecap_back.global.common.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/sub/comments")
public class SubCommentApiController {
    private final SubCommentService subCommentService;
    private final SubCommentRepository subCommentRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public SubCommentApiController(SubCommentService subCommentService, SubCommentRepository subCommentRepository, MemberRepository memberRepository) {
        this.subCommentService = subCommentService;
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
            // 토큰으로 회원 권한 검사 추가 필요
            Long memberId = subCommentSaveRequestDto.getMemberId();
            Member member = memberRepository.findById(memberId).get();
            SubCommentDto result = subCommentService.writeSubComment(commentId, subCommentSaveRequestDto, member);

            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
