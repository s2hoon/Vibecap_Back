package com.example.vibecap_back.domain.post.dto.Request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostUpdateRequestDto {

    private String title;
    private String body;
    private String tag_name;

    @Builder
    public PostUpdateRequestDto(String title, String body, String tag_name) {
        this.title = title;
        this.body = body;
        this.tag_name = tag_name;
    }

}
