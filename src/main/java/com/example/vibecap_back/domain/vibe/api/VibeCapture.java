package com.example.vibecap_back.domain.vibe.api;

import com.example.vibecap_back.domain.model.ExtraInfo;
import com.example.vibecap_back.domain.vibe.application.ImageAnalyzer;
import com.example.vibecap_back.domain.vibe.application.PlaylistSearchEngine;
import com.example.vibecap_back.domain.vibe.application.VideoQuery;
import com.example.vibecap_back.domain.vibe.application.VibeService;
import com.example.vibecap_back.domain.vibe.dto.CaptureResult;
import com.example.vibecap_back.domain.vibe.dto.CaptureWithExtraInfoRequest;
import com.example.vibecap_back.domain.vibe.exception.ExternalApiException;
import com.example.vibecap_back.domain.vibe.exception.NoProperVideoException;
import com.example.vibecap_back.global.common.response.BaseResponse;
import com.example.vibecap_back.global.config.storage.FileSaveErrorException;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
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
    private final VideoQuery videoQuery;
    private final VibeService vibeService;

    @Autowired
    public VibeCapture(ImageAnalyzer imageAnalyzer, PlaylistSearchEngine playlistRecommender,
                       VideoQuery videoQuery, VibeService vibeService) {
        this.imageAnalyzer = imageAnalyzer;
        this.playlistRecommender = playlistRecommender;
        this.videoQuery = videoQuery;
        this.vibeService = vibeService;
    }


    /**
     * 사진 + 감정 으로만 음악 추천
     * @return
     */
    @PostMapping(value = "/capture_google",consumes = {"multipart/form-data"})
    public BaseResponse<CaptureResult> capture(@RequestParam("member_id") Long memberId,
                                               @RequestParam("image_file") MultipartFile imageFile,
                                               @RequestParam("extra_info") String extraInfo){
        CaptureResult result;
        LOGGER.warn(extraInfo);

        try {
            result = vibeService.capture_google(memberId, imageFile,extraInfo);
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
        } catch (FileSaveErrorException e) {
            return new BaseResponse<>(FILE_SAVE_ERROR);
        }

    }

    /**
     * 사진과 추가 정보를 모두 사용해서 음악 추천(azure computer vision)
     * @param memberId
     * @param extraInfo
     * @param imageFile
     * @return
     */
    @PostMapping(value = "/capture_azure", consumes = {"multipart/form-data"})
    public BaseResponse<CaptureResult> capture(@RequestParam(value = "member_id") Long memberId,
                                               @RequestParam(value = "extra_info" ,required = false) String extraInfo,
                                               @RequestPart("image_file") MultipartFile imageFile) {
        CaptureResult result;
        LOGGER.warn(extraInfo);

        try {

            result = vibeService.capture_azure(memberId, imageFile, extraInfo);

            return new BaseResponse<>(result);
        } catch (ExternalApiException e) {
            return new BaseResponse<>(EXTERNAL_API_FAILED);
        } catch (IOException e) {
            return new BaseResponse<>(SAVE_TEMPORARY_FILE_FAILED);
        } catch (NoProperVideoException e) {
            return new BaseResponse<>(NO_PROPER_VIDEO);
        } catch (FileSaveErrorException e) {
            return new BaseResponse<>(FILE_SAVE_ERROR);
        }
    }



    /**
     * 사진과 추가 정보를 모두 사용해서 음악 추천(이미지 유사도)
     * @param memberId
     * @param extraInfo
     * @param imageFile
     * @return
     */
    @PostMapping(value = "/capture_similarity", consumes = {"multipart/form-data"})
    public BaseResponse<CaptureResult> capture_similarity(@RequestParam(value = "member_id") Long memberId,
                                               @RequestParam(value = "extra_info") String extraInfo,
                                               @RequestPart("image_file") MultipartFile imageFile) {
        CaptureResult result;

        try {
            result = vibeService.capture_similarity(memberId, imageFile, new ExtraInfo(extraInfo));

            return new BaseResponse<>(result);

        } catch (ExternalApiException e) {
            return new BaseResponse<>(EXTERNAL_API_FAILED);
        } catch (IOException e) {
            return new BaseResponse<>(SAVE_TEMPORARY_FILE_FAILED);
        } catch (NoProperVideoException e) {
            return new BaseResponse<>(NO_PROPER_VIDEO);
        } catch (FileSaveErrorException e) {
            return new BaseResponse<>(FILE_SAVE_ERROR);
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
        } catch (FileSaveErrorException e) {
            return new BaseResponse<>(FILE_SAVE_ERROR);
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
