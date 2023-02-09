package com.example.vibecap_back.domain.vibe.application;

import com.example.vibecap_back.domain.vibe.exception.ExternalApiException;
import com.example.vibecap_back.domain.vibe.exception.NoProperVideoException;
import com.google.api.services.youtube.model.SearchResult;

import java.util.List;

public interface PlaylistSearchEngine {
    /**
     * @param query
     * youtube 검색어
     * @return
     */

    public List<SearchResult> searchVideos(String query) throws ExternalApiException, NoProperVideoException;
}
