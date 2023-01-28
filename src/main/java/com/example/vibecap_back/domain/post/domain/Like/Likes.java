package com.example.vibecap_back.domain.post.domain.Like;

import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.post.domain.Post;
import lombok.*;

import javax.persistence.*;
import java.util.Optional;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="post_like")
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_PostLike_Member"))
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "FK_PostLike_Post"))
    private Post post;

    public static boolean isVotedPost(Optional<Likes> optionalPostLike) {
        return optionalPostLike.isPresent();
    }

    public void mappingMember(Member member) {
        this.member = member;
        member.mappingPostLike(this);
    }

    public void mappingPost(Post post) {
        this.post = post;
        post.mappingPostLike(this);
    }
}
