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

    private Long post_id;
    private Long Member_id;
    private Long vibe_id;

    private String tag_name;

    public PostListResponseDto(Posts entity)
    {
        this.post_id = entity.getPost_id();
        this.Member_id = entity.getMember_id();
        this.vibe_id = entity.getVibe_id();
        this.tag_name = entity.getTag_name();
    }
}
