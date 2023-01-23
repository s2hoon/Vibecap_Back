package com.example.vibecap_back.domain.vibe.exception;

import static com.example.vibecap_back.global.common.response.BaseResponseStatus.EXTERNAL_API_FAILED;

public class ExternalApiException extends Exception {
    public ExternalApiException() {
        super(EXTERNAL_API_FAILED.getMessage());
    }
}
