package com.example.vibecap_back.domain.post.dto.Response;

import com.example.vibecap_back.domain.post.domain.Posts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// 게시물 조회 Dto - 특정 게시물
@Getter
@Setter
@AllArgsConstructor
public class PostResponseDto {

    private Long post_id;
    private Long member_id;
    private String title;
    private String body;
    private Long vibe_id;
    private Long like_number;
    private Long scrap_number;
    private Long comment_number;
    private String tag_name;

    @Builder
    public PostResponseDto(Posts entity)
    {
        this.post_id = entity.getPost_id();
        this.member_id = entity.getMember_id();
        this.title = entity.getTitle();
        this.body = entity.getBody();
        this.vibe_id = entity.getVibe_id();
        this.like_number = entity.getLike_number();
        this.scrap_number = entity.getScrap_number();
        this.comment_number = entity.getComment_number();
        this.tag_name = entity.getTag_name();
    }

}
