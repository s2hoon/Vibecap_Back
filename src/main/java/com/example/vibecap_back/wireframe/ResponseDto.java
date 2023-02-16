package com.example.vibecap_back.wireframe;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

import java.util.List;

@Setter
public class ResponseDto {
    @JsonProperty("video_list")
    List<String> results;

    public ResponseDto(List<String> results) {
        this.results = results;
    }
}
