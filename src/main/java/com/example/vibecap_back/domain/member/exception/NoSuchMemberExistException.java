package com.example.vibecap_back.domain.member.exception;

import static com.example.vibecap_back.global.common.response.BaseResponseStatus.NO_SUCH_MEMBER_EXIST;

public class NoSuchMemberExistException extends Exception{
    public NoSuchMemberExistException() {
        super(NO_SUCH_MEMBER_EXIST.getMessage());
    }
}
