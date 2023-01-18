package com.example.vibecap_back.domain.comment.dto.Response;

import com.example.vibecap_back.domain.comment.domain.Comments;
import com.example.vibecap_back.domain.post.domain.Posts;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentListDto {
    private Long comment_id;
    private String content;
    private String nickname;

    public CommentListDto(Comments entity)
    {
        this.comment_id = entity.getComment_id();
        this.content = entity.getContent();
        this.nickname = entity.getMember().getNickname();
    }
}
