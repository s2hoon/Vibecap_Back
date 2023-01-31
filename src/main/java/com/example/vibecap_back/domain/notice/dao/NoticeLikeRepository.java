package com.example.vibecap_back.domain.notice.dao;

import com.example.vibecap_back.domain.notice.domain.NoticeLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoticeLikeRepository extends JpaRepository<NoticeLike, Long> {

    @Query(value = "SELECT n from NoticeLike  n " +
            "WHERE n.receiverId = ?1 and n.isRead = false " +
            "ORDER BY n.noticeLikeId DESC")
    List<NoticeLike> selectAllLikeNotices(Long memberId);
}
