package com.example.vibecap_back.domain.post.dto.Response;

import com.example.vibecap_back.domain.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 게시물 조회 Dto - 전체
@Getter
@Setter
@NoArgsConstructor
public class PostListResponseDto {

    @JsonProperty("post_id")
    private Long postId;
    @JsonProperty("member_id")
    private Long memberId;
    @JsonProperty("vibe_id")
    private Long vibeId;
    @JsonProperty("vibe_image")
    private String vibeImage;

    public PostListResponseDto(Post entity)
    {
        this.postId = entity.getPostId();
        this.memberId = entity.getMember().getMemberId();
        this.vibeId = entity.getVibe().getVibeId();
        this.vibeImage = entity.getVibe().getVibeImage();
    }
}
