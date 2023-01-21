package com.example.vibecap_back.domain.vibe.application;

import com.example.vibecap_back.domain.model.ExtraInfo;
import com.example.vibecap_back.domain.vibe.api.VibeCapture;
import com.example.vibecap_back.domain.vibe.dao.VibeRepository;
import com.example.vibecap_back.domain.vibe.domain.Vibe;
import com.example.vibecap_back.domain.vibe.dto.CaptureResult;
import com.example.vibecap_back.domain.vibe.exception.ExternalApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Service
public class VibeService {

    private final Logger LOGGER = LoggerFactory.getLogger(VibeCapture.class);
    private final VibeRepository vibeRepository;
    private ImageAnalyzer imageAnalyzer;
    private PlaylistSearchEngine playlistSearchEngine;
    private final QueryMaker queryMaker;

    @Autowired
    public VibeService(ImageAnalyzer imageAnalyzer, PlaylistSearchEngine playlistSearchEngine,
                       QueryMaker queryMaker,
                       VibeRepository vibeRepository) {
        this.imageAnalyzer = imageAnalyzer;
        this.playlistSearchEngine = playlistSearchEngine;
        this.queryMaker = queryMaker;
        this.vibeRepository = vibeRepository;
    }

    /**
     * 사진과 추가 정보를 조합하여 vibe를 생성, 저장하고 결과 반환
     * @param memberId
     * @param imageFile
     * @param extraInfo
     * @return
     * @throws ExternalApiException
     * @throws IOException
     */
    public CaptureResult capture(Long memberId, MultipartFile imageFile, ExtraInfo extraInfo)
            throws ExternalApiException, IOException {

        byte[] data = imageFile.getBytes();
        String label;
        String query;
        String videoId;
        String videoLink;
        String keywords;

        // 이미지를 설명하는 라벨 추출
        label = imageAnalyzer.detectLabelsByWebReference(data);
        // label로부터 youtube query 생성
        query = queryMaker.assemble(extraInfo, label);
        // query 결과 획득
        videoId = playlistSearchEngine.search(query);
        videoLink = getFullUrl(videoId);
        // 생성한 vibe를 DB에 저장
        keywords = label + extraInfo.toString();
        saveVibe(memberId, data, videoLink, keywords);

        CaptureResult result = CaptureResult.builder()
                .keywords(keywords.split(" "))
                .youtubeLink(videoLink)
                .videoId(videoId)
                .build();

        return result;
    }

    /**
     * 사진만 사용하여 vibe 생성, 저장하고 결과 반환
     * @param memberId
     * @param imageFile
     * @return
     * @throws ExternalApiException
     * @throws IOException
     */
    public CaptureResult capture(Long memberId, MultipartFile imageFile)
            throws ExternalApiException, IOException, NullPointerException {

        if (memberId == null || imageFile == null)
            throw new NullPointerException("empty request");

        byte[] data = imageFile.getBytes();
        String label;
        String videoId;
        String videoLink;
        String[] keywords = new String[1];

        // 이미지를 설명하는 라벨 추출
        label = imageAnalyzer.detectLabelsByWebReference(data);
        keywords[0] = label;
        // label로 바로 검색
        videoId = playlistSearchEngine.search(label);
        videoLink = getFullUrl(videoId);
        // vibe를 DB에 저장
        saveVibe(memberId, data, videoLink, label);

        CaptureResult result = CaptureResult.builder()
                .keywords(keywords)
                .youtubeLink(videoLink)
                .videoId(videoId)
                .build();

        return result;
    }

    /**
     * 날씨, 시간, 기분 정보만 사용하여 음악 추천.
     * vibe는 생성하지 않는다.
     * @param extraInfo
     * @return
     */
    public CaptureResult capture(ExtraInfo extraInfo) throws ExternalApiException {
        String query;
        String videoId;
        String videoLink;
        CaptureResult result;
        String[] keywords = new String[1];

        query = queryMaker.assemble(extraInfo);
        keywords[0] = query;
        videoId = playlistSearchEngine.search(query);
        videoLink = getFullUrl(videoId);

        result = CaptureResult.builder()
                .keywords(keywords)
                .youtubeLink(videoLink)
                .videoId(videoId)
                .build();


        return result;
    }

    /**
     * 사진을 사용해서 생성된 vibe만 저장 가능.
     * @param memberId
     * @param image
     * @param link
     * @param keywords
     * @return
     * 생성된 vibe의 id값
     */
    private Long saveVibe(Long memberId, byte[] image, String link, String keywords) {
        Vibe vibe = Vibe.builder()
                .memberId(memberId)
                .vibeImage(image)
                .youtubeLink(link)
                .vibeKeywords(keywords)
                .build();
        return vibeRepository.save(vibe).getVibeId();
    }

    /**
     * 해당 비디오에 접근하는 링크 생성
     * @param videoId
     * @return
     */
    private String getFullUrl(String videoId) {
        return  String.format("https://www.youtube.com/watch?v=%s", videoId);
    }
}
