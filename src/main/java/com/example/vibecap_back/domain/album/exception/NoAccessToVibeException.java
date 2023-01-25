package com.example.vibecap_back.domain.album.exception;

import static com.example.vibecap_back.global.common.response.BaseResponseStatus.NO_ACCESS_TO_VIBE;

public class NoAccessToVibeException extends Exception {
    public NoAccessToVibeException() {
        super(NO_ACCESS_TO_VIBE.getMessage());
    }
}
