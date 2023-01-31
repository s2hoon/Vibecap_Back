package com.example.vibecap_back.factory;

import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.post.domain.Like.Likes;
import com.example.vibecap_back.domain.post.domain.Post;

public class LikeFactory {

    /**
     * @param member
     * 좋아요를 누르는 회원
     * @param post
     * 좋아요가 추가되는 게시글
     * @return
     */
    public static Likes getLike(Member member, Post post) {
        return Likes.builder()
                .member(member)
                .post(post)
                .build();
    }

    public static Likes selectLike(Member member, Post post, int i) {
        return Likes.builder()
                .id((long) i)
                .member(member)
                .post(post)
                .build();
    }

}
