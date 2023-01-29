package com.example.vibecap_back.domain.comment.domain;

import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.post.domain.Post;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "sub_comment")
public class SubComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "sub_comment_id")
    private Long subCommentId;

    /**
     * Comment 가 삭제되면 Comment 에 작성된 모든 대댓글 삭제
     **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comments comments;

    @Column(name = "sub_comment_body", length = 255, nullable = false)
    private String subCommentBody;

    /**
     * Member 가 탈퇴하면 Member 가 작성한 모든 대댓글 삭제
     **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    /**
     * Post 가 삭제되면 Post 에 작성된 모든 대댓글 삭제
     **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    public void mappingComment(Comments comments) {
        this.comments = comments;
        comments.mappingSubComment(this);
    }
}
