package com.example.vibecap_back.domain.vibe.application;

import com.example.vibecap_back.domain.vibe.exception.ExternalApiException;

import java.util.Map;

public interface PlaylistSearchEngine {
    /**
     * @param query
     * youtube 검색어
     * @return
     */

    public String search(String query) throws ExternalApiException;
}
