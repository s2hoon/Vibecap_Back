package com.example.vibecap_back.domain.post.domain;

import com.example.vibecap_back.domain.comment.dao.CommentRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.post.domain.Like.Likes;
import com.example.vibecap_back.domain.post.domain.Scrap.Scrap;
import com.example.vibecap_back.domain.vibe.domain.Vibe;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="post")
@SecondaryTable(name = "tag",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "tag_id"))
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    @JsonProperty("post_id")
    private Long postId;

    @Column(name = "post_title", length = 32, nullable = false)
    private String title;

    @Column(name="post_body", length = 140, nullable = false)
    private String body;

    @OneToOne
    @JoinColumn(name = "vibe_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Vibe vibe;
    @Column(name="like_number")
    @JsonProperty("like_number")
    private Long likeNumber;
    @Column(name="scrap_number")
    @JsonProperty("scrap_number")
    private Long scrapNumber;

    @Column(name="comment_number")
    @JsonProperty("comment_number")
    private Long commentNumber;

    @PrePersist
    public void prePersist(){
        this.likeNumber = this.likeNumber == null ? 0 : this.likeNumber;
        this.scrapNumber = this.scrapNumber == null ? 0 : this.scrapNumber;
        this.commentNumber = this.commentNumber == null ? 0 : this.commentNumber;
    }

    @Column(table = "tag", name = "tag_name")
    @JsonProperty("tag_name")
    private String tagName;

    /** Member 가 탈퇴하면 Member 가 작성한 모든 게시글 삭제 **/
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Builder
    public Post(Long postId, String title, String body, Vibe vibe,
                Long likeNumber, Long scrapNumber, Long commentNumber, String tagName,
                Member member)
    {
        this.postId = postId;
        this.title = title;
        this.body = body;
        this.vibe = vibe;
        this.likeNumber = likeNumber;
        this.scrapNumber = scrapNumber;
        this.commentNumber = commentNumber;
        this.tagName = tagName;
        this.member = member;
    }

    public void update(String title, String body){
        this.title = title;
        this.body = body;
    }

    @OneToMany(fetch = LAZY, mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Likes> postLikeList = new ArrayList<>();

    public void mappingPostLike(Likes postLike) {
        this.postLikeList.add(postLike);
    }

    public void updateLikeCount() {
        this.likeNumber = (long) this.postLikeList.size();
    }

    public void discountLike(Likes postLike) {
        this.postLikeList.remove(postLike);
    }

    @OneToMany(fetch = LAZY, mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Scrap> postScrapList = new ArrayList<>();

    public void mappingPostScrap(Scrap postScrap) {
        this.postScrapList.add(postScrap);
    }

    public void updateScrapCount() {
        this.scrapNumber = (long) this.postScrapList.size();
    }

    public void discountScrap(Scrap postScrap) {
        this.postScrapList.remove(postScrap);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof Post)
            return ((this.postId == ((Post) obj).getPostId())
                && (this.title.equals(((Post) obj).getTitle()))
                && (this.body.equals(((Post) obj).getBody())));
        else
            return false;
    }
}