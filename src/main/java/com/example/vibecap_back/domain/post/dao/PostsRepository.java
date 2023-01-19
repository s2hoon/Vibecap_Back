package com.example.vibecap_back.domain.post.dao;

import com.example.vibecap_back.domain.post.domain.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    @Query(value = "SELECT p FROM Posts p where p.tag_name = ?1 order by p.id DESC")
    //List<Posts> findAllDesc();

    List<Posts> findByTag_name(String tag_name);

}
