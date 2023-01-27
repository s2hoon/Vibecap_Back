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
public class GetMyPostsResponse {
    @JsonProperty("post_id")
    private Long postId;
    @JsonProperty("vibe_id")
    private Long vibeId;
    @JsonProperty("vibe_image")
    private String vibeImage;

    public GetMyPostsResponse(GetMyPostsResponse getMyPostsResponse) {
        this.postId = getMyPostsResponse.getPostId();
        this.vibeId = getMyPostsResponse.getVibeId();
        this.vibeImage = getMyPostsResponse.getVibeImage();
    }

}
