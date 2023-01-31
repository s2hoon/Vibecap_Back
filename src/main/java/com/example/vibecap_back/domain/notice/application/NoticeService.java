package com.example.vibecap_back.domain.notice.application;

import com.example.vibecap_back.domain.notice.dao.NoticeCommentRepository;
import com.example.vibecap_back.domain.notice.dao.NoticeLikeRepository;
import com.example.vibecap_back.domain.notice.dao.NoticeSubCommentRepository;
import com.example.vibecap_back.domain.notice.domain.NoticeComment;
import com.example.vibecap_back.domain.notice.dto.SingleNoticeDto;
import com.example.vibecap_back.domain.notice.exception.NoSuchNoticeExistException;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {

    private final NoticeCommentRepository noticeCommentRepository;
    private final NoticeSubCommentRepository noticeSubCommentRepository;
    private final NoticeLikeRepository noticeLikeRepository;

    @Autowired
    public NoticeService(NoticeCommentRepository noticeCommentRepository, NoticeSubCommentRepository noticeSubCommentRepository,
                         NoticeLikeRepository noticeLikeRepository) {
        this.noticeCommentRepository = noticeCommentRepository;
        this.noticeSubCommentRepository = noticeSubCommentRepository;
        this.noticeLikeRepository = noticeLikeRepository;
    }

    public List<SingleNoticeDto> getCommentNoticesFor(Long memberId) {
        List<SingleNoticeDto> result = new ArrayList<>();
        List<NoticeComment> commentNotices;
        commentNotices = noticeCommentRepository.selectAllCommentNotices(memberId);

        for (NoticeComment notice : commentNotices)
            result.add(new SingleNoticeDto(notice));

        return result;
    }

    public NoticeComment checkRead(Long noticeId) throws NoSuchNoticeExistException {
        Optional<NoticeComment> notice;
        notice = noticeCommentRepository.findById(noticeId);
        notice.orElseThrow(()->new NoSuchNoticeExistException());
        notice.get().setRead(true);
        return notice.get();
    }
}
