package com.example.vibecap_back.domain.mypage.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveGmailRequest {
    @JsonProperty("member_id")
    private Long memberId;
    @JsonProperty("google_email")
    private String gmail;
}
