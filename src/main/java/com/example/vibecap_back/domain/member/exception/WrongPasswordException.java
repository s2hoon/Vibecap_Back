package com.example.vibecap_back.domain.member.exception;

import com.example.vibecap_back.global.common.response.BaseResponseStatus;

public class WrongPasswordException extends Exception {

    public WrongPasswordException() {
        super(BaseResponseStatus.WRONG_PASSWORD.getMessage());
    }
}