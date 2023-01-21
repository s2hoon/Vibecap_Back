package com.example.vibecap_back.domain.vibe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class CaptureResult {
    /* vibe 생성에 사용된 키워드들.
     * label, weather, time, feeling 순서
     */
    String[] keywords;
    @JsonProperty("youtube_link")
    String youtubeLink;
    @JsonProperty("video_id")
    String videoId;
}
