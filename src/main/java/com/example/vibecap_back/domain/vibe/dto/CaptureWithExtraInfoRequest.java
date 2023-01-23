package com.example.vibecap_back.domain.vibe.dto;

import com.example.vibecap_back.domain.model.ExtraInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;

/**
 * 추가정보만 이용한 음악 추천.
 * {
 *     "member_id": "51",
 *     "extra_info": {
 *         "weather": "흐린 날",
 *         "time": "아침",
 *         "feeling": "잔잔한"
 *     }
 * }
 */
@Getter
public class CaptureWithExtraInfoRequest {
    @JsonProperty("member_id")
    private Long memberId;

    @JsonProperty("extra_info")
    private ExtraInfo extraInfo;
}
