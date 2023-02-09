package com.example.vibecap_back.domain.vibe.application;

import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.model.ExtraInfo;
import com.example.vibecap_back.domain.vibe.api.VibeCapture;
import com.example.vibecap_back.domain.vibe.dao.VibeRepository;
import com.example.vibecap_back.domain.vibe.domain.Vibe;
import com.example.vibecap_back.domain.vibe.dto.CaptureResult;
import com.example.vibecap_back.domain.vibe.exception.ExternalApiException;
import com.example.vibecap_back.domain.vibe.exception.NoProperVideoException;
import com.example.vibecap_back.global.config.storage.FileSaveErrorException;
import com.example.vibecap_back.global.config.storage.FireBaseService;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class VibeService {

    private final Logger LOGGER = LoggerFactory.getLogger(VibeCapture.class);
    private final VibeRepository vibeRepository;
    private ImageAnalyzer imageAnalyzer;
    private PlaylistSearchEngine playlistSearchEngine;
    private TextTranslator textTranslator;
    private final VideoQuery videoQuery;
    private final FireBaseService fireBaseService;
    private final MemberRepository memberRepository;

    @Autowired
    public VibeService(ImageAnalyzer imageAnalyzer, PlaylistSearchEngine playlistSearchEngine,
                       VideoQuery videoQuery, VibeRepository vibeRepository,
                       TextTranslator textTranslator, FireBaseService fireBaseService,
                       MemberRepository memberRepository) {
        this.imageAnalyzer = imageAnalyzer;
        this.playlistSearchEngine = playlistSearchEngine;
        this.videoQuery = videoQuery;
        this.vibeRepository = vibeRepository;
        this.textTranslator = textTranslator;
        this.fireBaseService = fireBaseService;
        this.memberRepository = memberRepository;
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
    @Transactional
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
        // label로부터 youtube query 생성
        query = videoQuery.assemble(extraInfo, label);
        // query 결과 획득
        videoId = selectTheFirstVideo(playlistSearchEngine.searchVideos(query));
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
    @Transactional
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
        keywords[0] = label;
        // query 생성
        query = videoQuery.assemble(label);
        videoId = selectTheFirstVideo(playlistSearchEngine.searchVideos(query));
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
    @Transactional
    public CaptureResult capture(ExtraInfo extraInfo) throws ExternalApiException, NoProperVideoException {
        String query;
        String videoId;
        String videoLink;
        CaptureResult result;
        String[] keywords = new String[1];

        query = videoQuery.assemble(extraInfo);
        keywords[0] = query;
        videoId = selectTheFirstVideo(playlistSearchEngine.searchVideos(query));
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
        Optional<Member> author;
        author = this.memberRepository.findById(memberId);
        Vibe vibe = Vibe.builder()
                .member(author.get())
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

    /**
     * tester client가 사용하는 기능들
     */
    // 추가정보 + 사진
    public List<String> getEveryVideos(ExtraInfo extraInfo, MultipartFile image)
            throws ExternalApiException, IOException, NoProperVideoException {
        String query;
        String label;
        List<SearchResult> videos;
        List<String> results = new ArrayList<>();
        // 검색어 생성
        label = imageAnalyzer.detectLabelsByWebReference(image.getBytes());
        query = videoQuery.assemble(extraInfo, label);
        // 검색
        videos = playlistSearchEngine.searchVideos(query);
        for (SearchResult videoInfo : videos) {
            String link = getFullUrl(videoInfo.getId().getVideoId());
            results.add(link);
        }

        return results;
    }

    // 추가정보
    public List<String> getEveryVideos(ExtraInfo extraInfo)
            throws ExternalApiException, IOException, NoProperVideoException {
        String query;
        List<SearchResult> videos;
        List<String> results = new ArrayList<>();
        // 검색어 생성
        query = videoQuery.assemble(extraInfo);
        // 검색
        videos = playlistSearchEngine.searchVideos(query);
        for (SearchResult videoInfo : videos) {
            String link = getFullUrl(videoInfo.getId().getVideoId());
            results.add(link);
        }

        return results;
    }

    // 사진
    public List<String> getEveryVideos(MultipartFile image)
            throws ExternalApiException, IOException, NoProperVideoException {
        String query;
        String label;
        List<SearchResult> videos;
        List<String> results = new ArrayList<>();
        // 검색어 생성
        label = imageAnalyzer.detectLabelsByWebReference(image.getBytes());
        query = videoQuery.assemble(label);
        // 검색
        videos = playlistSearchEngine.searchVideos(query);
        for (SearchResult videoInfo : videos) {
            String link = getFullUrl(videoInfo.getId().getVideoId());
            results.add(link);
        }

        return results;
    }
}
