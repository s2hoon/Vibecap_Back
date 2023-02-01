package com.example.vibecap_back.factory;

import com.example.vibecap_back.domain.comment.domain.Comments;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.post.domain.Post;

public class CommentFactory {

    private static String BODY = "test comment body ";

    public static Comments getComment(Member writer, Post targetPost, int i) {
        return Comments.builder()
                .commentBody(BODY + String.valueOf(i))
                .member(writer)
                .post(targetPost)
                .build();
    }

    public static Comments selectComment(Member writer, Post targetPost, int i) {
        return Comments.builder()
                .commentId((long) i)
                .commentBody(BODY + String.valueOf(i))
                .member(writer)
                .post(targetPost)
                .build();

    }
}
