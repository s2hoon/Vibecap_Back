package com.example.vibecap_back.domain.notice.domain;

import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.model.NoticeEvent;
import com.example.vibecap_back.domain.post.domain.Post;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 게시글에 좋아요가 추가되었음을 알림
 */
@Getter
@Setter
@Entity
@Table(name = "notice_like")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class NoticeLike {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_like_id")
    Long noticeLikeId;

    @ManyToOne  // 게시글 하나에 대해 여러 개의 좋아요 알림이 연관됨
    @JoinColumn(name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Post post;  // 좋아요가 추가된 게시글

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
    String senderNickname;  // 좋아요 누른 회원 닉네임

    @Column(name = "is_read")
    @ColumnDefault("false")
    boolean isRead;

    public NoticeEvent getEventType() {
        return NoticeEvent.LIKE;
    }
}
