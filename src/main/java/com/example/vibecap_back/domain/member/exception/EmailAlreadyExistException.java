package com.example.vibecap_back.domain.member.exception;

import static com.example.vibecap_back.global.common.response.BaseResponseStatus.EMAIL_ALREADY_EXIST;

public class EmailAlreadyExistException extends Exception {

    public EmailAlreadyExistException() {
        super(EMAIL_ALREADY_EXIST.getMessage());
    }

}
