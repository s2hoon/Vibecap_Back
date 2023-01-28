package com.example.vibecap_back.domain.post.dto.Response;

import com.example.vibecap_back.domain.post.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 게시물 조회 Dto - 전체
@Getter
@Setter
@NoArgsConstructor
public class PostListResponseDto {

    private Long postId;
    private Long memberId;
    private Long vibeId;
    private String vibeImage;

    public PostListResponseDto(Post entity)
    {
        this.postId = entity.getPostId();
        this.memberId = entity.getMember().getMemberId();
        this.vibeId = entity.getVibe().getVibeId();
        this.vibeImage = entity.getVibe().getVibeImage();
    }
}
