package com.example.vibecap_back.domain.vibe.application;

import com.example.vibecap_back.domain.model.ExtraInfo;
import com.example.vibecap_back.domain.vibe.api.VibeCapture;
import com.example.vibecap_back.domain.vibe.dao.VibeRepository;
import com.example.vibecap_back.domain.vibe.domain.Vibe;
import com.example.vibecap_back.domain.vibe.dto.CaptureResult;
import com.example.vibecap_back.domain.vibe.exception.ExternalApiException;
import com.example.vibecap_back.domain.vibe.exception.NoProperVideoException;
import com.example.vibecap_back.global.config.storage.FileSaveErrorException;
import com.example.vibecap_back.global.config.storage.FireBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class VibeService {

    private final Logger LOGGER = LoggerFactory.getLogger(VibeCapture.class);
    private final VibeRepository vibeRepository;
    private ImageAnalyzer imageAnalyzer;
    private PlaylistSearchEngine playlistSearchEngine;
    private TextTranslator textTranslator;
    private final VideoQuery videoQuery;
    private final FireBaseService fireBaseService;

    @Autowired
    public VibeService(ImageAnalyzer imageAnalyzer, PlaylistSearchEngine playlistSearchEngine,
                       VideoQuery videoQuery,
                       VibeRepository vibeRepository,
                       TextTranslator textTranslator, FireBaseService fireBaseService) {
        this.imageAnalyzer = imageAnalyzer;
        this.playlistSearchEngine = playlistSearchEngine;
        this.videoQuery = videoQuery;
        this.vibeRepository = vibeRepository;
        this.textTranslator = textTranslator;
        this.fireBaseService = fireBaseService;
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
            throws ExternalApiException, IOException, NoProperVideoException, FileSaveErrorException {

        byte[] data = imageFile.getBytes();
        String label;
        String query;
        String videoId;
        String videoLink;
        String keywords;
        Long vibeId;

        // 이미지를 설명하는 라벨 추출
        label = imageAnalyzer.detectLabelsByWebReference(data);
        label = textTranslator.translate(label);
        // label로부터 youtube query 생성
        query = videoQuery.assemble(extraInfo, label);
        // query 결과 획득
        videoId = playlistSearchEngine.search(query);
        videoLink = getFullUrl(videoId);
        // 이미지 파일을 firebase storage에 저장
        String imgUrl = fireBaseService.uploadFiles(imageFile);
        // 생성한 vibe를 DB에 저장
        keywords = label + extraInfo.toString();
        vibeId = saveVibe(memberId, imgUrl, videoLink, keywords);

        CaptureResult result = CaptureResult.builder()
                .keywords(keywords.split(" "))
                .youtubeLink(videoLink)
                .videoId(videoId)
                .vibeId(vibeId)
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
            throws ExternalApiException, IOException, NullPointerException, NoProperVideoException, FileSaveErrorException {

        if (memberId == null || imageFile == null)
            throw new NullPointerException("empty request");

        byte[] data = imageFile.getBytes();
        String label;
        String query;
        String videoId;
        String videoLink;
        String[] keywords = new String[1];
        Long vibeId;

        // 이미지를 설명하는 라벨 추출
        label = imageAnalyzer.detectLabelsByWebReference(data);
        label = textTranslator.translate(label);
        keywords[0] = label;
        // query 생성
        query = videoQuery.assemble(label);
        videoId = playlistSearchEngine.search(query);
        videoLink = getFullUrl(videoId);
        // 이미지 파일을 firebase storage에 저장
        String imgUrl = fireBaseService.uploadFiles(imageFile);
        // vibe를 DB에 저장
        vibeId = saveVibe(memberId, imgUrl, videoLink, label);

        CaptureResult result = CaptureResult.builder()
                .keywords(keywords)
                .youtubeLink(videoLink)
                .videoId(videoId)
                .vibeId(vibeId)
                .build();

        return result;
    }

    /**
     * 추가정보만 사용하여 음악 추천.
     * vibe는 생성하지 않는다.
     * @param extraInfo
     * @return
     */
    public CaptureResult capture(ExtraInfo extraInfo) throws ExternalApiException, NoProperVideoException {
        String query;
        String videoId;
        String videoLink;
        CaptureResult result;
        String[] keywords = new String[1];

        query = videoQuery.assemble(extraInfo);
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
    private Long saveVibe(Long memberId, String image, String link, String keywords) {
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
