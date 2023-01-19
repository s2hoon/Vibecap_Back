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

    private Long id;
    private Long Member_id;
    private Long vibe_id;
    //private LocalDateTime modifiedDate;

    public PostListResponseDto(Posts entity)
    {
        this.id = entity.getId();
        this.Member_id = entity.getMember().getMemberId();
        this.vibe_id = entity.getVibe_id();
    }
}
