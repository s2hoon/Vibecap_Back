package com.example.vibecap_back.domain.album.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetVibeResponse {
    @JsonProperty("vibe_id")
    private Long vibeId;
    @JsonProperty("member_id")
    private Long memberId;
    @JsonProperty("vibe_image")
    private String vibeImage;
    @JsonProperty("youtube_link")
    private String youtubeLink;
    @JsonProperty("vibe_keywords")
    private String vibeKeywords;
}
