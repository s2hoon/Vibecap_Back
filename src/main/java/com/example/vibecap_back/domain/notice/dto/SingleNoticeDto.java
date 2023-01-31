package com.example.vibecap_back.domain.notice.dto;

import com.example.vibecap_back.domain.model.NoticeEvent;
import com.example.vibecap_back.domain.notice.domain.NoticeComment;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SingleNoticeDto {
    @JsonProperty("notice_id")
    private Long noticeId;

    /**
     * 알리려는 내용
     * NoticeEvent.toString() 결과
     */
    private String event;

    /**
     * event 발생 시각
     * "yyyy-MM-dd'T'HH:mm:ss" 형태의 문자열
     * T 앞은 날짜(년, 월, 일), 뒤는 시각(시, 분, 초)
     * Z는 timezone (한국 : +0900)
     */
    private String time;

    /**
     * event를 만든 사람 닉네임
     */
    private String sender;

    /**
     * 댓글 내용 요약
     * event = "LIKE"인 경우 null
     */
    private String summary;

    public SingleNoticeDto(NoticeComment noticeComment) {
        this.noticeId = noticeComment.getNoticeCommentId();
        this.event = noticeComment.getEventType().toString();
        this.time = noticeComment.getCreatedTime().toString().substring(0,15);
        this.sender = noticeComment.getSenderNickname();
        // like인 경우
        if (this.event.equals(NoticeEvent.LIKE.toString()))
            this.summary = null;
        // comment, subComment
        this.summary = noticeComment.getSummary();
    }
}
