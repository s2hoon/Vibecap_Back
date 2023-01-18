package com.example.vibecap_back.domain.vibe.application;

import com.example.vibecap_back.domain.vibe.exception.ExternalApiException;

import java.util.List;

public interface ImageAnalyzer {
    /**
     * @param data
     * 바이너리 형태의 이미지 파일
     * @return
     * 키워드 배열
     */
    public List<String> detectLabels(byte[] data) throws ExternalApiException;

    /**
     * 해당 이미지를 웹에서 검색하여 label을 추측한다.
     *
     * @param data
     * @return
     * @throws ExternalApiException https://cloud.google.com/vision/docs/detecting-web
     */
    public String detectLabelsByWebReference(byte[] data) throws ExternalApiException;
}
