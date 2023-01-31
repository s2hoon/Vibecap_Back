package com.example.vibecap_back.domain.notice.api;

import com.example.vibecap_back.domain.model.NoticeEvent;
import com.example.vibecap_back.domain.notice.application.NoticeService;
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

    @GetMapping("/{memberId}")
    public BaseResponse<List<SingleNoticeDto>> loadNotices(
            @PathVariable(name = "memberId") Long memberId) {

        List<SingleNoticeDto> result;
        result = noticeService.getCommentNoticesFor(memberId);

        return new BaseResponse<>(result);
    }

    @PatchMapping("")
    public BaseResponse<Long> checkNotice(@RequestParam(name = "type") String type,
                                            @RequestParam(name = "noticeId") Long noticeId) {
        Long result;
        // noticeService.checkRead(noticeId);
        return null;
    }
}
