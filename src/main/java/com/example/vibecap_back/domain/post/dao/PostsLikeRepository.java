package com.example.vibecap_back.domain.post.dao;

import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.post.domain.Like.Likes;
import com.example.vibecap_back.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostsLikeRepository  extends JpaRepository<Likes, Long> {
    Optional<Likes> findByPostAndMember(Post post, Member member);

}
