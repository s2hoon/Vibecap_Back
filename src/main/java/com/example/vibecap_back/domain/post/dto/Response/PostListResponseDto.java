package com.example.vibecap_back.domain.post.dto.Response;

import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.post.domain.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 게시물 조회 Dto - 전체
@Getter
@Setter
@NoArgsConstructor
public class PostListResponseDto {

    private Long id;
    private Long Member_id;
    private Long vibe_id;

    public PostListResponseDto(Posts entity)
    {
        this.id = entity.getId();
        this.Member_id = entity.getMember_id();
        this.vibe_id = entity.getVibe_id();
    }
}
