package com.example.vibecap_back.domain.post.dto.Response;

import com.example.vibecap_back.domain.post.dto.domain.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostListResponseDto {

    private Long post_id;
    private Long member_id;
    private String title;
    private String body;
    private Long vibe_id;
    private Long like_number;
    private Long scrap_number;
    private Long tag_number;

    public PostListResponseDto(Posts entity)
    {
        this.post_id = entity.getPost_id();
        this.member_id = entity.getMember_id();
        this.title = entity.getTitle();
        this.body = entity.getBody();
        this.vibe_id = entity.getVibe_id();
        this.like_number = entity.getLike_number();
        this.scrap_number = entity.getScrap_number();
        this.tag_number = entity.getTag_number();
    }
}
