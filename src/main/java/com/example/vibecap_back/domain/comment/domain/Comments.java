package com.example.vibecap_back.domain.comment.domain;

import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.post.domain.Post;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="comment")
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name="comment_body",length = 255, nullable = false)
    private String commentBody;

    /** Member 가 탈퇴하면 Member 가 작성한 모든 댓글 삭제 **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    /** Post 가 삭제되면 Post 에 작성된 모든 댓글 삭제 **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

}
