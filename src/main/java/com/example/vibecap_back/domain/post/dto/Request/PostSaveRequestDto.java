package com.example.vibecap_back.domain.post.dto.Request;

import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.post.domain.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

// 게시물 작성 Dto
@Getter
@Setter
@NoArgsConstructor
public class PostSaveRequestDto {

    private Member member_id;
    private String title;
    private String body;
    private Long vibe_id;
    @Column(table = "tag")
    private String tag_name;;

    @Builder
    public PostSaveRequestDto(Member member_id, String title, String body, Long vibe_id, String tag_name){
        this.member_id = member_id;
        this.title = title;
        this.body = body;
        this.vibe_id = vibe_id;
        this.tag_name = tag_name;
    }

    public Posts toEntity(){
        return Posts.builder()
                .member(member_id)
                .title(title)
                .body(body)
                .vibe_id(vibe_id)
                .tag_name(tag_name)
                .build();
    }
}
