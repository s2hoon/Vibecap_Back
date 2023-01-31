package com.example.vibecap_back.domain.notice.dao;

import com.example.vibecap_back.domain.notice.domain.NoticeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoticeCommentRepository extends JpaRepository<NoticeComment, Long> {

    @Query(value = "SELECT n from NoticeComment n " +
            "WHERE n.receiverId = ?1 AND n.isRead = false " +
            "ORDER BY n.noticeCommentId DESC ")
    List<NoticeComment> selectAllCommentNotices(Long memberId);
}
