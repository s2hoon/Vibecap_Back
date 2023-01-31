package com.example.vibecap_back.domain.notice.exception;

import static com.example.vibecap_back.global.common.response.BaseResponseStatus.NO_SUCH_NOTICE;

public class NoSuchNoticeExistException extends RuntimeException {
    public NoSuchNoticeExistException() {
        super(NO_SUCH_NOTICE.getMessage());
    }
}
