package com.example.vibecap_back.domain.post.domain;

import com.example.vibecap_back.domain.comment.domain.Comments;
import com.example.vibecap_back.domain.comment.dto.CommentDto;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.post.domain.Like.Likes;
import com.example.vibecap_back.domain.post.domain.Scrap.Scrap;
import com.example.vibecap_back.domain.post.domain.Tag.Tags;
import com.example.vibecap_back.domain.vibe.domain.Vibe;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="post")
@SecondaryTable(name = "tag",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "tag_id"))
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(length = 32, nullable = false)
    private String title;

    @Column(length = 140, nullable = false)
    private String body;

    @OneToOne
    @JoinColumn(name = "vibe_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Vibe vibe;

    private Long likeNumber;

    private Long scrapNumber;

    private Long commentNumber;

    @PrePersist
    public void prePersist(){
        this.likeNumber = this.likeNumber == null ? 0 : this.likeNumber;
        this.scrapNumber = this.scrapNumber == null ? 0 : this.scrapNumber;
        this.commentNumber = this.commentNumber == null ? 0 : this.commentNumber;
    }

    @Column(table = "tag")
    private String tagName;

    //post 테이블이랑 tag 테이블 조인
    @ManyToMany
    @JoinTable(name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tags> tagList = new ArrayList<>();

    /** Member 가 탈퇴하면 Member 가 작성한 모든 게시글 삭제 **/
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Builder
    public Posts(Long id,String title, String body, Vibe vibe,
                 Long likeNumber, Long scrapNumber, Long commentNumber, String tagName,
                 Member member)
    {
        this.id = id;
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
}