package com.example.vibecap_back.domain.post.dto.Response;

import com.example.vibecap_back.domain.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// 게시물 조회 Dto - 특정 게시물
@Getter
@Setter
@AllArgsConstructor
public class PostResponseDto {

    @JsonProperty("post_id")
    private Long postId;
    @JsonProperty("member_id")
    private Long memberId;
    private String title;
    private String body;
    @JsonProperty("vibe_id")
    private Long vibeId;
    @JsonProperty("vibe_image")
    private String vibeImage;
    @JsonProperty("like_number")
    private Long likeNumber;
    @JsonProperty("scrap_number")
    private Long scrapNumber;
    @JsonProperty("comment_number")
    private Long commentNumber;
    @JsonProperty("tag_name")
    private String tagName;
    //private List<Tags> tag_name;
    @JsonProperty("profile_image")
    private String profileImage;
    private String nickname;

    @JsonProperty("modified_date")
    private LocalDateTime modifiedDate;

    @Builder
    public PostResponseDto(Post entity)
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
        this.tagName = entity.getTagName();
        this.modifiedDate = entity.getModifiedDate();
    }

}
