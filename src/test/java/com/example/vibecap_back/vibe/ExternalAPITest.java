package com.example.vibecap_back.vibe;

import com.example.vibecap_back.domain.vibe.application.ImageAnalyzer;
import com.example.vibecap_back.domain.vibe.application.Impl.LabelDetectionClient;
import com.example.vibecap_back.domain.vibe.application.Impl.YouTubeClient;
import com.example.vibecap_back.domain.vibe.application.PlaylistRecommender;
import com.example.vibecap_back.util.FileWorker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Google vision api, Youtube data api 호출 테스트
 * /assets/sample-image/ 에 저장된 이미지로 테스트
 */

public class ExternalAPITest {

    private ImageAnalyzer imageAnalyzer;
    private PlaylistRecommender playlistRecommender;
    private byte[] data;
    private static final String SAMPLE_IMAGE = "flags.jpeg";

    @BeforeEach
    void init() throws Exception {
        imageAnalyzer = new LabelDetectionClient();
        playlistRecommender = new YouTubeClient();
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
            System.out.println(imageAnalyzer.detectLabelsByWebReference(data));
            // imageAnalyzer.detectLabelsByWebReference(data);
        });
    }
}
