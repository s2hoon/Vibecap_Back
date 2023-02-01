package com.example.vibecap_back.domain.comment.dto;

import com.example.vibecap_back.domain.comment.domain.Comments;
import com.example.vibecap_back.domain.comment.domain.SubComment;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"commentId", "commentBody", "nickname", "profileImage", "subComment"})
public class CommentReadDto {

    @JsonProperty("comment_id")
    private Long commentId;

    @JsonProperty("comment_body")
    private String commentBody;

    private String nickname;

    @JsonProperty("profile_image")
    private String profileImage;

    private LocalDateTime createdDate;

    @JsonProperty("sub_comment")
    private List<SubCommentDto> subComment;



    /* SubCommentDto
     *  - sub_comment_id
     *  - sub_comment_body
     *  - nickname
     *  - profile_image
     */

    public static CommentReadDto toDto(Comments comment, List<SubComment> subCommentList) {
        return new CommentReadDto(
                comment.getCommentId(),
                comment.getCommentBody(),
                comment.getMember().getNickname(),
                comment.getMember().getProfileImage(),
                comment.getCreatedDate(),
                subCommentList.stream().map(subComment -> toDto(subComment)).collect(Collectors.toList())
        );
    }

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
