package com.example.vibecap_back.domain.post.domain;

import com.example.vibecap_back.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name="Post")
@SecondaryTable(name = "Tag",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "TAG_ID"))
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long id;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")*/
    private Long member_id;

    @Column(length = 32, nullable = false)
    private String title;

    @Column(length = 140, nullable = false)
    private String body;

    private Long vibe_id;

    private Long like_number;

    //@ColumnDefault("0")
    //@Column(columnDefinition = "Long default '0'")
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
    private List<Tags> tags = new ArrayList<Tags>();

    @Builder
    public Posts(Long id, Long member_id,
                 String title, String body,
                 Long vibe_id, Long like_number,
                 Long scrap_number, Long comment_number, String tag_name)
    {
        this.id = id;
        this.member_id = member_id;
        this.title = title;
        this.body = body;
        this.vibe_id = vibe_id;
        this.like_number = like_number;
        this.scrap_number = scrap_number;
        this.comment_number = comment_number;
        this.tag_name = tag_name;
    }

    public void update(String title, String body, String tag_name){
        this.title = title;
        this.body = body;
        this.tag_name = tag_name;
    }

}