package com.example.vibecap_back.domain.vibe.application;

import com.example.vibecap_back.domain.vibe.exception.ExternalApiException;

public interface TextTranslator {

    /**
     * 외국어를 한국어로 번역
     * @param foreignString
     * @return
     * @throws ExternalApiException
     */
    public String translate(String foreignString) throws ExternalApiException;
}
