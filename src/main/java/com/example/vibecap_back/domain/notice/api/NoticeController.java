package com.example.vibecap_back.domain.notice.api;

import com.example.vibecap_back.domain.model.NoticeEvent;
import com.example.vibecap_back.domain.notice.application.NoticeService;
import com.example.vibecap_back.domain.notice.domain.NoticeComment;
import com.example.vibecap_back.domain.notice.dto.SingleNoticeDto;
import com.example.vibecap_back.global.common.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    /**
     * 특정 회원에게 전달된 모든 알림 조회
     */
    @GetMapping("/{memberId}")
    public BaseResponse<List<SingleNoticeDto>> loadNotices(
            @PathVariable(name = "memberId") Long memberId) {

        List<SingleNoticeDto> result;
        result = noticeService.getNoticesFor(memberId);

        return new BaseResponse<>(result);
    }

    /**
     * 특정 알림을 읽음 처리 한다.
     */
    @PatchMapping("")
    public BaseResponse<Long> checkNotice(@RequestParam(name = "type") String type,
                                            @RequestParam(name = "noticeId") Long noticeId) {
        Long result;
        NoticeEvent event;

        if (type.equals(NoticeEvent.COMMENT.toString())) {
            event = NoticeEvent.COMMENT;
        } else if (type.equals(NoticeEvent.SUB_COMMENT.toString())) {
            event = NoticeEvent.SUB_COMMENT;
        } else {
            event = NoticeEvent.LIKE;
        }

        result = noticeService.checkRead(event, noticeId);
        return new BaseResponse<>(result);
    }
}
