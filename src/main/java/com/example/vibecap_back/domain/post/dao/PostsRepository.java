package com.example.vibecap_back.domain.post.dao;

import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.post.domain.Like.Likes;
import com.example.vibecap_back.domain.post.domain.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    @Query(value = "SELECT p FROM Posts p where p.tagName like %?1% order by p.id DESC")
    //List<Posts> findAllDesc();

    List<Posts> findByTag_name(String tagName);

    //Optional<Member> findByEmail(String email);



}
