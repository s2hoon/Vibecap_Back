package com.example.vibecap_back.domain.album.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetAlbumRequest {
    @JsonProperty("member_id")
    private Long memberId;
}
