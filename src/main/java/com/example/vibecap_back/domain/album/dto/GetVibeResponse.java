package com.example.vibecap_back.domain.album.dto;

import com.example.vibecap_back.util.ByteArraySerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    @JsonSerialize(using= ByteArraySerializer.class)
    @JsonProperty("vibe_image")
    private byte[] vibeImage;
    @JsonProperty("youtube_link")
    private String youtubeLink;
    @JsonProperty("vibe_keywords")
    private String vibeKeywords;
}
