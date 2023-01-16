package com.example.vibecap_back.domain.post.dao;

import com.example.vibecap_back.domain.post.dto.domain.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    @Query("SELECT p FROM Posts p ORDER BY p.post_id DESC")
    List<Posts> findAllDesc(String tag_name);
}
