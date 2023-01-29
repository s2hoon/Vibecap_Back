package com.example.vibecap_back.domain.comment.exception;

import static com.example.vibecap_back.global.common.response.BaseResponseStatus.NOT_FOUND_SUB_COMMENT;

public class NotFoundSubCommentException extends Exception{
    public NotFoundSubCommentException() {
        super(NOT_FOUND_SUB_COMMENT.getMessage());
    }

}
