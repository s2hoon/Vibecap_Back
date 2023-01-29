package com.example.vibecap_back.domain.comment.exception;

import static com.example.vibecap_back.global.common.response.BaseResponseStatus.NOT_FOUND_COMMENT;

public class NotFoundCommentException extends Exception {
    public NotFoundCommentException() {super(NOT_FOUND_COMMENT.getMessage());}
}
