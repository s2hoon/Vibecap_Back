package com.example.vibecap_back.domain.post.dto.Response;

import com.example.vibecap_back.domain.post.domain.Posts;
import com.example.vibecap_back.domain.post.domain.Tag.Tags;
import com.example.vibecap_back.domain.vibe.domain.Vibe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;

// 게시물 조회 Dto - 특정 게시물
@Getter
@Setter
@AllArgsConstructor
public class PostResponseDto {

    private Long postId;
    private Long memberId;
    private String title;
    private String body;
    private Long vibeId;
    private String vibeImage;
    private Long likeNumber;
    private Long scrapNumber;
    private Long commentNumber;
    private String tagName;
    //private List<Tags> tag_name;
    private byte[] profileImage;
    private String nickname;

    @Builder
    public PostResponseDto(Posts entity)
    {
        this.postId = entity.getPostId();
        this.memberId = entity.getMember().getMemberId();
        this.profileImage = entity.getMember().getProfileImage();
        this.nickname = entity.getMember().getNickname();
        this.title = entity.getTitle();
        this.body = entity.getBody();
        this.vibeId = entity.getVibe().getVibeId();
        this.vibeImage = entity.getVibe().getVibeImage();
        this.likeNumber = entity.getLikeNumber();
        this.scrapNumber = entity.getScrapNumber();
        this.commentNumber = entity.getCommentNumber();
        this.tagName = entity.getTagName() + " " + entity.getVibe().getVibeKeywords();
    }

}
