package com.example.vibecap_back.domain.vibe.exception;

import static com.example.vibecap_back.global.common.response.BaseResponseStatus.NO_PROPER_VIDEO;

public class NoProperVideoException extends Exception {

    public NoProperVideoException() {
        super(NO_PROPER_VIDEO.getMessage());
    }
}
