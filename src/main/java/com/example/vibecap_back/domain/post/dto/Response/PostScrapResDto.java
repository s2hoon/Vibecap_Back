package com.example.vibecap_back.domain.post.dto.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostScrapResDto {
    @JsonProperty("scrap_or_else")
    private String ScrapOrElse;
}
