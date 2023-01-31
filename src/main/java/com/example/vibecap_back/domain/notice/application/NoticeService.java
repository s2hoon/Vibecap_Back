package com.example.vibecap_back.domain.notice.application;

import com.example.vibecap_back.domain.model.NoticeEvent;
import com.example.vibecap_back.domain.notice.dao.NoticeCommentRepository;
import com.example.vibecap_back.domain.notice.dao.NoticeLikeRepository;
import com.example.vibecap_back.domain.notice.dao.NoticeSubCommentRepository;
import com.example.vibecap_back.domain.notice.domain.NoticeComment;
import com.example.vibecap_back.domain.notice.domain.NoticeLike;
import com.example.vibecap_back.domain.notice.domain.NoticeSubComment;
import com.example.vibecap_back.domain.notice.dto.SingleNoticeDto;
import com.example.vibecap_back.domain.notice.exception.NoSuchNoticeExistException;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {

    private final NoticeCommentRepository noticeCommentRepository;
    private final NoticeSubCommentRepository noticeSubCommentRepository;
    private final NoticeLikeRepository noticeLikeRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(NoticeService.class);

    @Autowired
    public NoticeService(NoticeCommentRepository noticeCommentRepository, NoticeSubCommentRepository noticeSubCommentRepository,
                         NoticeLikeRepository noticeLikeRepository) {
        this.noticeCommentRepository = noticeCommentRepository;
        this.noticeSubCommentRepository = noticeSubCommentRepository;
        this.noticeLikeRepository = noticeLikeRepository;
    }

    /**
     * 특정 회원에게 전달된 모든 종류의 알림을 반환한다.
     * @param memberId
     * @return
     */
    @Transactional
    public List<SingleNoticeDto> getNoticesFor(Long memberId) {
        List<SingleNoticeDto> result = new ArrayList<>();
        List<NoticeComment> commentNotices;
        List<NoticeSubComment> subCommentNotices;
        List<NoticeLike> likeNotices;

        commentNotices = noticeCommentRepository.selectAllCommentNotices(memberId);
        subCommentNotices = noticeSubCommentRepository.selectAllSubCommentNotices(memberId);
        likeNotices = noticeLikeRepository.selectAllLikeNotices(memberId);

        for (NoticeComment notice : commentNotices)
            result.add(new SingleNoticeDto(notice));
        for (NoticeLike notice : likeNotices)
            result.add(new SingleNoticeDto(notice));
        for (NoticeSubComment notice : subCommentNotices)
            result.add(new SingleNoticeDto(notice));

        Collections.sort(result);

        return result;
    }


    /**
     * 특정 알림(comment)을 읽음 처리 한다.
     * @param noticeId
     * @return
     * @throws NoSuchNoticeExistException
     */
    @Transactional
    public Long checkRead(NoticeEvent type, Long noticeId) throws NoSuchNoticeExistException {

        // comment notice
        if (type == NoticeEvent.COMMENT) {
            Optional<NoticeComment> notice;
            notice = noticeCommentRepository.findById(noticeId);
            notice.orElseThrow(()->new NoSuchNoticeExistException());
            notice.get().setRead(true);

            return notice.get().getNoticeCommentId();
        // sub comment notice
        } else if (type == NoticeEvent.SUB_COMMENT) {
            Optional<NoticeSubComment> notice;
            notice = noticeSubCommentRepository.findById(noticeId);
            notice.orElseThrow(()->new NoSuchNoticeExistException());
            notice.get().setRead(true);

            return notice.get().getSubCommentNoticeId();
        // like notice
        } else {
            Optional<NoticeLike> notice;
            notice = noticeLikeRepository.findById(noticeId);
            notice.orElseThrow(()->new NoSuchNoticeExistException());
            notice.get().setRead(true);

            return notice.get().getNoticeLikeId();
        }
    }
}
