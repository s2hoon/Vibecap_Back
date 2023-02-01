package com.example.vibecap_back.domain.comment.dto;

import com.example.vibecap_back.domain.comment.domain.SubComment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubCommentDto {
    @JsonProperty("sub_comment_id")
    private Long subCommentId;

    @JsonProperty("comment_id")
    private Long commentId;

    @JsonProperty("sub_comment_body")
    private String subCommentBody;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("profile_image")
    private String profileImage;

    private LocalDateTime createdDate;

    public static SubCommentDto toDto(SubComment subComment) {
        return new SubCommentDto(
                subComment.getSubCommentId(),
                subComment.getComments().getCommentId(),
                subComment.getSubCommentBody(),
                subComment.getMember().getNickname(),
                subComment.getMember().getProfileImage(),
                subComment.getCreatedDate()
        );
    }
}
