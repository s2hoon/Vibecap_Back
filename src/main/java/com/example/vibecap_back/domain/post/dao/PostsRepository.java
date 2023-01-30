package com.example.vibecap_back.domain.post.dao;

import com.example.vibecap_back.domain.post.domain.Post;
import com.example.vibecap_back.domain.post.dto.Response.PostResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostsRepository extends JpaRepository<Post, Long> {
    //@Query(value = "SELECT p FROM Posts p where p.tagName like %?1% order by p.postId DESC")
    //List<Posts> findAllDesc();

    @Query(value = "SELECT p FROM Post p Inner Join Tag t on t.tagId = p.postId where t.tagName like %?1% order by p.postId DESC")
    List<Post> findByTagName(String tagName);

    @Query(value ="select p.postId from Post p where p.postId = ?1")
    Long findByPostId(Long postId);

    @Query(value ="select p from Post p where p.postId = ?1")
    List<PostResponseDto> findByPost(Long postId);

    @Query(value="select p from Post p order by p.postId desc")
    List<Post> selectAllPost();

    List<Post> findTop3ByOrderByLikeNumberDesc();

    @Transactional
    @Modifying
    @Query(value = "update Post p set p.commentNumber = ?1 where p.postId = ?2")
    void updateCount(Long commentNumber, Long PostId);

}
