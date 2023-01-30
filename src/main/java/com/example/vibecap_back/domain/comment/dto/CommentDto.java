package com.example.vibecap_back.domain.comment.dto;

import com.example.vibecap_back.domain.comment.domain.Comments;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    @JsonProperty("comment_id")
    private Long commentId;

    @JsonProperty("comment_body")
    private String commentBody;

    @JsonProperty("member_id")
    private Long memberId;

    private String nickname;

    private LocalDateTime createdDate;

    @Lob
    @Column(table = "member")
    @JsonProperty("profile_image")
    private String profileImage;

    public static CommentDto toDto(Comments comment) {
        return new CommentDto(
                comment.getCommentId(),
                comment.getCommentBody(),
                comment.getMember().getMemberId(),
                comment.getMember().getNickname(),
                comment.getCreatedDate(),
                comment.getMember().getProfileImage()
        );
    }
}
