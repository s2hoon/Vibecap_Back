package com.example.vibecap_back.domain.vibe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CaptureFromGalleryRequest {
    @JsonProperty("member_id")
    private Long memberId;
    @JsonProperty("image_file")
    private MultipartFile imageFile;

}
