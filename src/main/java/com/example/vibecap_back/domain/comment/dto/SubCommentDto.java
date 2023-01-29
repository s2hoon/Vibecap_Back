package com.example.vibecap_back.domain.comment.dto;

import com.example.vibecap_back.domain.comment.domain.SubComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubCommentDto {
    private Long subCommentId;
    private Long commentId;
    private String subCommentBody;
    private String nickname;
    private String profileImage;

    public static SubCommentDto toDto(SubComment subComment) {
        return new SubCommentDto(
                subComment.getSubCommentId(),
                subComment.getComments().getCommentId(),
                subComment.getSubCommentBody(),
                subComment.getMember().getNickname(),
                subComment.getMember().getProfileImage()
        );
    }
}
