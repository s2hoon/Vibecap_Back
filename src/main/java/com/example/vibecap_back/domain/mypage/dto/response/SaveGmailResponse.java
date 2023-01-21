package com.example.vibecap_back.domain.mypage.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SaveGmailResponse {
    @JsonProperty("google_email")
    private String gmail;
}
