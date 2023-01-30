package com.example.vibecap_back.domain.post.dto.Response;

import com.example.vibecap_back.domain.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostWeeklyReqDto {

    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("tag_name")
    private String tagName;

    @JsonProperty("vibe_image")
    private String vibeImage;

    @Builder
    public PostWeeklyReqDto(Post entity){
        this.postId = entity.getPostId();
        this.tagName = entity.getTagName();
        this.vibeImage = entity.getVibe().getVibeImage();
    }
}
