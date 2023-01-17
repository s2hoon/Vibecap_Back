package com.example.vibecap_back.domain.vibe;

import com.example.vibecap_back.domain.vibe.application.ImageAnalyzer;
import com.example.vibecap_back.domain.vibe.application.Impl.LabelDetectionClient;
import com.example.vibecap_back.domain.vibe.application.Impl.YouTubeClient;
import com.example.vibecap_back.domain.vibe.application.PlaylistRecommender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VibeConfig {

    @Bean
    public ImageAnalyzer imageAnalyzer() {
        return new LabelDetectionClient();
    }

    @Bean
    public PlaylistRecommender playlistRecommender() {
        return new YouTubeClient();
    }

}
