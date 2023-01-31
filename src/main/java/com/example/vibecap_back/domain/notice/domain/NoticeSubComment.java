package com.example.vibecap_back.domain.notice.domain;

import com.example.vibecap_back.domain.comment.domain.Comments;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.model.NoticeEvent;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "notice_sub_comment")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class NoticeSubComment {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_comment_notice_id")
    Long subCommentNoticeId;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Comments comment;       // 대댓글이 추가된 댓글

    /**
     * receiver, sender를 member 테이블을 참조하는 외래 키로 지정하지 않은 이유
     * 1. join할 일이 없다.
     * 2. 해당 member가 삭제되어도 상관 없다. 그냥 알림만 보내면 된다.
     */

    @CreatedDate
    @Column(name = "created_time")
    LocalDateTime createdTime;

    @Column(name = "receiver_id")
    Long receiverId;    // 게시글 작성자

    @Column(name = "sender_nickname", length = 16)
    String senderNickname;      // 대댓글 작성자 닉네임

    @Column(name = "is_read")
    @ColumnDefault("false")
    boolean isRead;

    @Column(name = "summary", length = 16)
    String summary;

    public NoticeEvent getEventType() {
        return NoticeEvent.SUB_COMMENT;
    }
}
