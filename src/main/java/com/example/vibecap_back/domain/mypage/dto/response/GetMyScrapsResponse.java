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
public class GetMyScrapsResponse {
    @JsonProperty("scrap_id")
    private Long scrapId;
    @JsonProperty("post_id")
    private Long postId;
    @JsonProperty("vibe_id")
    private Long vibeId;
    @JsonProperty("vibe_image")
    private String vibeImage;

    public GetMyScrapsResponse(GetMyScrapsResponse getMyScrapsResponse) {
        this.scrapId = getMyScrapsResponse.getScrapId();
        this.postId = getMyScrapsResponse.getPostId();
        this.vibeId = getMyScrapsResponse.getVibeId();
        this.vibeImage = getMyScrapsResponse.getVibeImage();
    }
}
