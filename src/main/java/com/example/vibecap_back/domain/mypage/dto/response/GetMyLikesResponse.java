package com.example.vibecap_back.domain.mypage.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetMyLikesResponse {
    @JsonProperty("like_id")
    private Long likeId;
    @JsonProperty("post_id")
    private Long postId;
    @JsonProperty("vibe_id")
    private Long vibeId;
    @JsonProperty("vibe_image")
    private String vibeImage;

    public GetMyLikesResponse(GetMyLikesResponse getMyLikesResponse) {
        this.likeId = getMyLikesResponse.getLikeId();
        this.postId = getMyLikesResponse.getPostId();
        this.vibeId = getMyLikesResponse.getVibeId();
        this.vibeImage = getMyLikesResponse.getVibeImage();
    }
}
