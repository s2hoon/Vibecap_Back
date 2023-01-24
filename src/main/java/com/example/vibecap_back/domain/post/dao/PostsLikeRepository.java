package com.example.vibecap_back.domain.post.dao;

import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.post.domain.Like.Likes;
import com.example.vibecap_back.domain.post.domain.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostsLikeRepository  extends JpaRepository<Likes, Long> {
    Optional<Likes> findByPostAndMember(Posts post, Member member);

    /*@Query(value ="Update Posts p set p.likeNumber = ?1 where p.id = ?2")
    Optional<Posts> findByPostLikeAndId(Long likeNumber, Long postId);*/
}
