package com.example.vibecap_back.domain.post.dto.Request;

import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.post.domain.Post;
import com.example.vibecap_back.domain.vibe.domain.Vibe;
import lombok.*;

// 게시물 작성 Dto
@Getter
@Setter
@NoArgsConstructor
public class PostSaveRequestDto {

    private Member member;
    private String title;
    private String body;
    private Vibe vibe;
    //@Column(table = "tag")
    private String tagName;

    public Post toEntity(){
        return Post.builder()
                .member(member)
                .title(title)
                .body(body)
                .vibe(vibe)
                .tagName(tagName)
                .build();
    }

}
