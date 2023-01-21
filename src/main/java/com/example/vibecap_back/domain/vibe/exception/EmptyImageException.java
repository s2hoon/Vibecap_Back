package com.example.vibecap_back.domain.vibe.exception;

import static com.example.vibecap_back.global.common.response.BaseResponseStatus.EMPTY_IMAGE;

public class EmptyImageException extends Exception {
    public EmptyImageException() {
        super(EMPTY_IMAGE.getMessage());
    }
}
