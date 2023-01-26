package com.example.vibecap_back.domain.member.exception;

import static com.example.vibecap_back.global.common.response.BaseResponseStatus.DISABLED_MEMBER;

public class DisabledMemberException extends Exception {

    public DisabledMemberException() {
        super(DISABLED_MEMBER.getMessage());
    }
}
