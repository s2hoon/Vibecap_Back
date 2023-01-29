package com.example.vibecap_back.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubCommentSaveRequestDto {
    @JsonProperty("member_id")
    private Long memberId;
    @JsonProperty("sub_comment_body")
    private String subCommentBody;
}
