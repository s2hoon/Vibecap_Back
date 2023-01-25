package com.example.vibecap_back.domain.vibe.api;

import com.example.vibecap_back.domain.model.ExtraInfo;
import com.example.vibecap_back.domain.vibe.application.ImageAnalyzer;
import com.example.vibecap_back.domain.vibe.application.PlaylistSearchEngine;
import com.example.vibecap_back.domain.vibe.application.QueryMaker;
import com.example.vibecap_back.domain.vibe.application.VibeService;
import com.example.vibecap_back.domain.vibe.dto.CaptureResult;
import com.example.vibecap_back.domain.vibe.dto.CaptureWithExtraInfoRequest;
import com.example.vibecap_back.domain.vibe.exception.ExternalApiException;
import com.example.vibecap_back.domain.vibe.exception.NoProperVideoException;
import com.example.vibecap_back.global.common.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.example.vibecap_back.global.common.response.BaseResponseStatus.*;

@RestController
@RequestMapping(value = "/app/vibe")
public class VibeCapture {

    private final Logger LOGGER = LoggerFactory.getLogger(VibeCapture.class);
    private ImageAnalyzer imageAnalyzer;
    private PlaylistSearchEngine playlistRecommender;
    private final QueryMaker queryMaker;
    private final VibeService vibeService;

    @Autowired
    public VibeCapture(ImageAnalyzer imageAnalyzer, PlaylistSearchEngine playlistRecommender,
                       QueryMaker queryMaker, VibeService vibeService) {
        this.imageAnalyzer = imageAnalyzer;
        this.playlistRecommender = playlistRecommender;
        this.queryMaker = queryMaker;
        this.vibeService = vibeService;
    }

    /**
     * 사진과 추가 정보를 모두 사용해서 음악 추천
     * @param memberId
     * @param extraInfo
     * @param imageFile
     * @return
     */
    @PostMapping(value = "/capture", consumes = {"multipart/form-data"})
    public BaseResponse<CaptureResult> capture(@RequestParam("member_id") Long memberId,
                                               @RequestParam("extra_info") String extraInfo,
                                               @RequestPart("image_file") MultipartFile imageFile) {
        CaptureResult result;

        try {
            result = vibeService.capture(memberId, imageFile, new ExtraInfo(extraInfo));

            return new BaseResponse<>(result);

        } catch (ExternalApiException e) {
            return new BaseResponse<>(EXTERNAL_API_FAILED);
        } catch (IOException e) {
            return new BaseResponse<>(SAVE_TEMPORARY_FILE_FAILED);
        } catch (NoProperVideoException e) {
            return new BaseResponse<>(NO_PROPER_VIDEO);
        }
    }

    /**
     * 사진으로만 음악 추천
     * @return
     */
    @PostMapping(value = "/capture-from-gallery", consumes = {"multipart/form-data"})
    public BaseResponse<CaptureResult> capture(@RequestParam("member_id") Long memberId,
                                               @RequestParam("image_file") MultipartFile imageFile) {
            CaptureResult result;

        try {
             result = vibeService.capture(memberId, imageFile);
            // result = vibeService.capture(request.getMemberId(), request.getImageFile());
            return new BaseResponse<>(result);
        } catch (ExternalApiException e) {
            return new BaseResponse(EXTERNAL_API_FAILED);
        } catch (IOException e) {
            return new BaseResponse(SAVE_TEMPORARY_FILE_FAILED);
        } catch (NullPointerException e) {
            return new BaseResponse(EMPTY_IMAGE);
        } catch (NoProperVideoException e) {
            return new BaseResponse<>(NO_PROPER_VIDEO);
        }

    }

    /**
     * 추가정보로만 음악 추천
     * @return
     */
    @PostMapping (value = "/capture-without-image")
    public BaseResponse<CaptureResult> capture(@RequestBody CaptureWithExtraInfoRequest request) {
        CaptureResult result;

        try {
            result = vibeService.capture(request.getExtraInfo());
            return new BaseResponse<>(result);
        } catch (ExternalApiException e) {
            return new BaseResponse(EXTERNAL_API_FAILED);
        } catch (NoProperVideoException e) {
            return new BaseResponse<>(NO_PROPER_VIDEO);
        }

    }
}
