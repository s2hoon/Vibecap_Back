package com.example.vibecap_back.domain.notice.dao;

import com.example.vibecap_back.domain.notice.domain.NoticeSubComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoticeSubCommentRepository extends JpaRepository<NoticeSubComment, Long> {

    @Query(value = "SELECT n from NoticeSubComment n " +
            "WHERE n.receiverId = ?1 AND n.isRead = false " +
            "ORDER BY n.subCommentNoticeId DESC ")
    List<NoticeSubComment> selectAllSubCommentNotices(Long memberId);
}
