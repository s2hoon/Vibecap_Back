package com.example.vibecap_back.domain.post.dto.Response;

import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.post.domain.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// 게시물 조회 Dto - 전체
@Getter
@Setter
@NoArgsConstructor
public class PostListResponseDto {

    private Long postId;
    private Long memberId;
    private Long vibeId;
    private byte[] vibeImage;

    public PostListResponseDto(Posts entity)
    {
        this.postId = entity.getPostId();
        this.memberId = entity.getMember().getMemberId();
        this.vibeId = entity.getVibe().getVibeId();
        this.vibeImage = entity.getVibe().getVibeImage();
    }
}
