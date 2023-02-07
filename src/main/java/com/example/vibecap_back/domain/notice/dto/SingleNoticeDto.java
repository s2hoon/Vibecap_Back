package com.example.vibecap_back.domain.notice.dto;

import com.example.vibecap_back.domain.notice.domain.NoticeComment;
import com.example.vibecap_back.domain.notice.domain.NoticeLike;
import com.example.vibecap_back.domain.notice.domain.NoticeSubComment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleNoticeDto implements Comparable<SingleNoticeDto> {
    @JsonProperty("notice_id")
    private Long noticeId;

    /**
     * 알리려는 내용
     * NoticeEvent.toString() 결과
     */
    private String event;

    /**
     * 게시글 id
     */
    @JsonProperty("post_id")
    private Long postId;

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
        this.postId = noticeComment.getPost().getPostId();
        this.time = noticeComment.getCreatedTime().toString().substring(0,15);
        this.sender = noticeComment.getSenderNickname();
        // comment, subComment
        this.summary = noticeComment.getSummary();
    }

    public SingleNoticeDto(NoticeSubComment noticeSubComment) {
        this.noticeId = noticeSubComment.getSubCommentNoticeId();
        this.event = noticeSubComment.getEventType().toString();
        this.postId = noticeSubComment.getComment().getPost().getPostId();
        this.time = noticeSubComment.getCreatedTime().toString().substring(0,15);
        this.sender = noticeSubComment.getSenderNickname();
        // comment, subComment
        this.summary = noticeSubComment.getSummary();
    }

    public SingleNoticeDto(NoticeLike noticeLike) {
        this.noticeId = noticeLike.getNoticeLikeId();
        this.event = noticeLike.getEventType().toString();
        this.postId = noticeLike.getPost().getPostId();
        this.time = noticeLike.getCreatedTime().toString().substring(0,15);
        this.sender = noticeLike.getSenderNickname();
        this.summary = null;
    }


    @Override
    public int compareTo(SingleNoticeDto dto) {
        return this.time.compareTo(dto.time);
    }
}
