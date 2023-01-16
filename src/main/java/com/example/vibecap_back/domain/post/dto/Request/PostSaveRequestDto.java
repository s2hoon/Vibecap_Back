package com.example.vibecap_back.domain.post.dto.Request;

import com.example.vibecap_back.domain.post.dto.domain.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 게시물 작성 Dto
@Getter
@NoArgsConstructor
public class PostSaveRequestDto {

    private String title;
    private String body;
    private Long vibe_id;
    private String tag_name;

    @Builder
    public PostSaveRequestDto(String title, String body, Long vibe_id, String tag_name){
        this.title = title;
        this.body = body;
        this.vibe_id = vibe_id;
        this.tag_name = tag_name;
    }

    public Posts toEntity(){
        return Posts.builder()
                .title(title)
                .body(body)
                .vibe_id(vibe_id)
                .tag_name(tag_name)
                .build();
    }
}
