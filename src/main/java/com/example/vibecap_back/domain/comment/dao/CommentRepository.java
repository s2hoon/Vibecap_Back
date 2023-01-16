package com.example.vibecap_back.domain.comment.dao;

import com.example.vibecap_back.domain.comment.domain.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {
    List<Comments> findAllByPost(Long PostId);
}
