package com.example.vibecap_back.domain.vibe;

import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.vibe.application.ImageAnalyzer;
import com.example.vibecap_back.domain.vibe.application.Impl.LabelDetectionClient;
import com.example.vibecap_back.domain.vibe.application.Impl.YouTubeClient;
import com.example.vibecap_back.domain.vibe.application.PlaylistSearchEngine;
import com.example.vibecap_back.domain.vibe.application.QueryMaker;
import com.example.vibecap_back.domain.vibe.application.VibeService;
import com.example.vibecap_back.domain.vibe.dao.VibeRepository;
import com.example.vibecap_back.domain.vibe.exception.ExternalApiException;
import com.example.vibecap_back.util.FileWorker;
import com.google.api.services.youtube.model.Playlist;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 * Google vision api, Youtube data api 호출 테스트
 * /assets/sample-image/ 에 저장된 이미지로 테스트
 */

public class ExternalAPITest {

    private ImageAnalyzer imageAnalyzer;
    private PlaylistSearchEngine playlistSearchEngine;
    private byte[] data;
    // private static final String SAMPLE_IMAGE = "attack-on-titan.jpeg";
    // private static final String SAMPLE_IMAGE = "pokemon.jpeg";
    // private static final String SAMPLE_IMAGE = "flags.jpeg";
    // private static final String SAMPLE_IMAGE = "food.jpeg";
     private static final String SAMPLE_IMAGE = "sea.jpeg";

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    @BeforeEach
    void init() throws Exception {
        imageAnalyzer = new LabelDetectionClient();
        playlistSearchEngine = new YouTubeClient();
        data = FileWorker.loadFile(SAMPLE_IMAGE);
    }

    @Test
    void 라벨_탐지() throws Exception {
        Assertions.assertDoesNotThrow(() -> {
             System.out.println(imageAnalyzer.detectLabels(data));
            // imageAnalyzer.detectLabels(data);
        });
    }

    @Test
    void 웹_개체_탐지() throws Exception {
        Assertions.assertDoesNotThrow(() -> {
            System.out.println(ANSI_GREEN +
                    imageAnalyzer.detectLabelsByWebReference(data) +
                    ANSI_RESET);
            // imageAnalyzer.detectLabelsByWebReference(data);
        });
    }

    @Test
    void 플레이리스트_추천_추가정보_이용() {
        Assertions.assertDoesNotThrow(() -> {
            System.out.println("www.youtube.com/watch?v=" +
                    playlistSearchEngine.search("여름 아침 신나는"));
        });
    }

    @Test
    void 플레이리스트_추천_사진_이용() {
        ImageAnalyzer imageAnalyzer = new LabelDetectionClient();
        PlaylistSearchEngine playlistSearchEngine = new YouTubeClient();
        String label;
        String videoId;

        try {
            label = imageAnalyzer.detectLabelsByWebReference(data);
            videoId = playlistSearchEngine.search(label);
            printFullURL(videoId);
        } catch (ExternalApiException e) {
            Assertions.fail(e.getMessage());
        }
    }

    void printFullURL(String videoId) {
        System.out.println("https://www.youtube.com/watch?v=" + videoId);
    }

}
