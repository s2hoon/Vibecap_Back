package com.example.vibecap_back.domain.post.domain;

import com.example.vibecap_back.domain.member.domain.Member;
<<<<<<< HEAD
import com.example.vibecap_back.domain.post.domain.Like.Likes;
import com.example.vibecap_back.domain.post.domain.Tag.Tags;
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
=======
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
>>>>>>> 060ce3cd008dc68dac7600e7929aa49278d35c2d
@Entity
@Table(name="Post")
@SecondaryTable(name = "Tag",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "TAG_ID"))
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long id;

<<<<<<< HEAD
=======
    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")*/
    private Long member_id;

>>>>>>> 060ce3cd008dc68dac7600e7929aa49278d35c2d
    @Column(length = 32, nullable = false)
    private String title;

    @Column(length = 140, nullable = false)
    private String body;

    private Long vibe_id;

    private Long like_number;

<<<<<<< HEAD
=======
    //@ColumnDefault("0")
    //@Column(columnDefinition = "Long default '0'")
>>>>>>> 060ce3cd008dc68dac7600e7929aa49278d35c2d
    private Long scrap_number;

    private Long comment_number;

    @PrePersist
    public void prePersist(){
        this.like_number = this.like_number == null ? 0 : this.like_number;
        this.scrap_number = this.scrap_number == null ? 0 : this.scrap_number;
        this.comment_number = this.comment_number == null ? 0 : this.comment_number;
    }

    @Column(table = "Tag")
    private String tag_name;

    //post 테이블이랑 tag 테이블 조인
    @ManyToMany
    @JoinTable(name = "Post_Tag",
            joinColumns = @JoinColumn(name = "POST_ID"),
            inverseJoinColumns = @JoinColumn(name = "TAG_ID")
    )
<<<<<<< HEAD
    private List<Tags> tagsList = new ArrayList<>();

    /** Member 가 탈퇴하면 Member 가 작성한 모든 게시글 삭제 **/
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    /*@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "like_number")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;*/

    @Builder
    public Posts(Long id
                 ,String title, String body,
                 Long vibe_id,
                 Long like_number, Long scrap_number, Long comment_number, String tag_name,
                 Member member)
    {
        this.id = id;
=======
    private List<Tags> tags = new ArrayList<Tags>();

    @Builder
    public Posts(Long id, Long member_id,
                 String title, String body,
                 Long vibe_id, Long like_number,
                 Long scrap_number, Long comment_number, String tag_name)
    {
        this.id = id;
        this.member_id = member_id;
>>>>>>> 060ce3cd008dc68dac7600e7929aa49278d35c2d
        this.title = title;
        this.body = body;
        this.vibe_id = vibe_id;
        this.like_number = like_number;
        this.scrap_number = scrap_number;
        this.comment_number = comment_number;
        this.tag_name = tag_name;
<<<<<<< HEAD
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
        this.like_number = (long) this.postLikeList.size();
    }

    public void discountLike(Likes postLike) {
        this.postLikeList.remove(postLike);

    }
}
=======
    }

    public void update(String title, String body, String tag_name){
        this.title = title;
        this.body = body;
        this.tag_name = tag_name;
    }

}
>>>>>>> 060ce3cd008dc68dac7600e7929aa49278d35c2d
