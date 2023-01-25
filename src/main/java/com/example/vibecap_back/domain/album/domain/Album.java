package com.example.vibecap_back.domain.album.domain;

import com.example.vibecap_back.util.ByteArraySerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    @JsonSerialize(using= ByteArraySerializer.class)
    @JsonProperty("vibe_image")
    private byte[] vibeImage;
}
