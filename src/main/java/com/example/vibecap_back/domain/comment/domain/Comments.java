package com.example.vibecap_back.domain.comment.domain;

import com.example.vibecap_back.domain.comment.dto.CommentDto;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.post.domain.Posts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Comment")
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long comment_id;

    @Column(length = 255, nullable = false)
    private String content;

    /** Member 가 탈퇴하면 Member 가 작성한 모든 댓글 삭제 **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    /** Post 가 삭제되면 Post 에 작성된 모든 댓글 삭제 **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Posts post;

}
