package com.example.vibecap_back.domain.comment.dto;

import com.example.vibecap_back.domain.comment.domain.Comments;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Lob;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long comment_id;
    private String content;
    private String nickname;

    @Lob
    @Column(table = "member")
    private byte[] profileImage;


    public static CommentDto toDto(Comments comment) {
        return new CommentDto(
                comment.getComment_id(),
                comment.getContent(),
                comment.getMember().getNickname(),
                comment.getMember().getProfileImage()
        );
    }
}
