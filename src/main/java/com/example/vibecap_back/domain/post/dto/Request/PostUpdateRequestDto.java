package com.example.vibecap_back.domain.post.dto.Request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class PostUpdateRequestDto {

    private String title;
    private String body;

    @Builder
    public PostUpdateRequestDto(String title, String body) {
        this.title = title;
        this.body = body;
    }

}
