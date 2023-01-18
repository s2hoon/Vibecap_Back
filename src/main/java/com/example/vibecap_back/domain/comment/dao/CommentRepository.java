package com.example.vibecap_back.domain.comment.dao;

import com.example.vibecap_back.domain.comment.domain.Comments;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {

    List<Comments> findAllByPostId(Long PostId);

    //List<Comments> findAllByPost_post_id(Long PostId);

    //List<Comments> findCommentsByPostPost_id(Long PostId);
}
