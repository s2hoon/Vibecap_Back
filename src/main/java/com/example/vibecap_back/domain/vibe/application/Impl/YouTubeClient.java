package com.example.vibecap_back.domain.vibe.application.Impl;

import com.example.vibecap_back.domain.vibe.application.PlaylistSearchEngine;
import com.example.vibecap_back.domain.vibe.exception.ExternalApiException;
import com.example.vibecap_back.global.config.security.Secret;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * sample code
 * https://developers.google.com/youtube/v3/docs/search/list
 * Bean 설정 VibeConfig.java 확인
 */
@NoArgsConstructor
public class YouTubeClient implements PlaylistSearchEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(YouTubeClient.class);

    // 모든 API 요청을 생성할 객체
    private YouTube youtube;
    // HTTP 통신을 위한 객체 (
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    // 반환받을 영상 개수 제한
    private final static Long NUMBER_OF_VIDEOS_RETURNED = 10L;
    // JSON factory
    private JsonFactory JSON_FACTORY = new GsonFactory();

    /**
     * 음악 카테고리 아이디
     * https://gist.github.com/dgp/1b24bf2961521bd75d6c
     */
    private final static String CATEGORY_ID_MUSIC = "10";

    @Override
    public String search(String query) throws ExternalApiException {

        try {
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) throws IOException {
                    // no-op
                }}).setApplicationName("youtube-search-client")
                    .build();

            YouTube.Search.List search = youtube.search().list("id, snippet");

            // Google 계정으로 인증받지 못한 요청을 위한 API key 설정
            search.setKey(Secret.YOUTUBE_DATA_API_KEY);
            search.setQ(query);

            // playlist 검색
            search.setType("video");
            search.setVideoCategoryId(CATEGORY_ID_MUSIC);
            /**
             * 반환된 응답으로부터 원하는 필드만 추출 (type/subtype 구성)
             * video id, 컨텐츠 제목
             * */
            search.setFields("items(id/kind, id/videoId, snippet/title)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
            // 검색 결과 획득 (비디오 NUMBER_OF_VIDEOS_RETURNED개)
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();

            // prettyPrint(searchResultList.iterator(), query);

            // 무작위로 1개의 비디오 전송
            // return selectRandomVideo(searchResultList);
            return selectTheFirstVideo(searchResultList);

        } catch (GoogleJsonResponseException e) {
            LOGGER.warn(e.getDetails().getMessage());
            throw new ExternalApiException();
        } catch (IOException e) {
            LOGGER.warn(e.getMessage());
            throw new ExternalApiException();
        } catch (Throwable t) {
            t.printStackTrace();
            throw new ExternalApiException();
        }

    }


    /**
     * NUMBER_OF_VIDEOS_RETURNED 개의 비디오 중 하나를 임의로 선택해 추천해준다.
     * @param searchResultList
     * @return
     * key: "link", "videoId"
     */
    private String selectRandomVideo(List<SearchResult> searchResultList) {
        // 현재 시간을 시드 값으로 사용하는 난수 생성기 초기화
        Random random = new Random(new Date().getTime());
        int randomIdx = random.nextInt(searchResultList.size());

        // 임의의 컨텐츠 1개 획득
        SearchResult singleVideo = searchResultList.get(randomIdx);
        ResourceId rId = singleVideo.getId();

        return rId.getVideoId();
    }

    /**
     * 검색된 영상 중 첫 번째 비디오 아이디 반환.
     * @param searchResultList
     * @return
     */
    private String selectTheFirstVideo(List<SearchResult> searchResultList) {
        return searchResultList.get(0).getId().getVideoId();
    }

    private void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {
        System.out.println("\n=============================================================");
        System.out.println(
                "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
        System.out.println("=============================================================\n");

        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }

        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Double checks the kind is video.
            if (rId.getKind().equals("youtube#video")) {
                System.out.println(" Video Id" + rId.getVideoId());
                System.out.println(String.format(" Video Link : https://www.youtube.com/watch?v=%s\n",
                        rId.getVideoId()));
                System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
                System.out.println("\n-------------------------------------------------------------\n");
            }
        }
    }

}
