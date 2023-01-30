package com.example.vibecap_back.domain.comment.dao;

import com.example.vibecap_back.domain.comment.domain.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCommentRepository extends JpaRepository<SubComment, Long> {

    Long countSubCommentsByPost_PostId(Long PostId);
}
