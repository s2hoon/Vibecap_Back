package com.example.vibecap_back.factory;

import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.post.domain.Post;
import com.example.vibecap_back.domain.vibe.domain.Vibe;

public class PostFactory {

    private final static String BASE_TITLE = "test title ";
    private final static String BASE_BODY = "test body ";


    public static Post getPost(Member member, Vibe vibe, Integer i) {
        Post dummy = Post.builder()
                .member(member)
                .title(BASE_TITLE + i.toString())
                .body(BASE_BODY + i.toString())
                .vibe(vibe)
                .build();

        return dummy;
    }

    public static Post selectPost(Member member, Vibe vibe, Integer i) {
        Post post = Post.builder()
                .postId(i.longValue())
                .member(member)
                .title(BASE_TITLE + i.toString())
                .body(BASE_BODY + i.toString())
                .vibe(vibe)
                .likeNumber(0L)
                .scrapNumber(0L)
                .commentNumber(0L)
                .build();

        return post;
    }
}
