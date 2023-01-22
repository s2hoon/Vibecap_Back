package com.example.vibecap_back.domain.album.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Album {
    @JsonProperty("vibe_id")
    private Long vibeId;
    @JsonProperty("vibe_image")
    private byte[] vibeImage;
}
