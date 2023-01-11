package com.example.vibecap_back.domain.member.exception;

import com.example.vibecap_back.global.common.response.BaseResponseStatus;

public class WrongEmailException extends Exception {
    public WrongEmailException() {
        super(BaseResponseStatus.WRONG_EMAIL.getMessage());
    }
}
