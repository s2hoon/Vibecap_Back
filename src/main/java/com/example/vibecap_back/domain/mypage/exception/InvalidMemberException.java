package com.example.vibecap_back.domain.mypage.exception;

import static com.example.vibecap_back.global.common.response.BaseResponseStatus.INVALID_MEMBER_JWT;

public class InvalidMemberException extends Exception{
    public InvalidMemberException() {
        super(INVALID_MEMBER_JWT.getMessage());
    }
}
