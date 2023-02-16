package com.example.vibecap_back.wireframe;

import com.example.vibecap_back.domain.model.ExtraInfo;
import com.example.vibecap_back.domain.vibe.application.VibeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/wireframe/vibe")
public class WireFrameAPI {

    private VibeService vibeService;

    @Autowired
    public WireFrameAPI(VibeService vibeService) {
        this.vibeService = vibeService;
    }

    // 사진
    @PostMapping(value = "/capture-from-gallery", consumes = {"multipart/form-data"})
    public ResponseDto capture(@RequestParam("image_file") MultipartFile imageFile) {
        try {
            return new ResponseDto(vibeService.getEveryVideos(imageFile));
        } catch (Exception e) {
            List<String> response = new ArrayList<>();
            response.add(e.getMessage());
            return new ResponseDto(response);
        }
    }

    // 사진 + 추가정보
    @PostMapping(value = "/capture")
    public ResponseDto capture(@RequestParam("image_file") MultipartFile imageFile,
                                @RequestParam("extra_info") String extraInfo) {
        try {
            return new ResponseDto(vibeService.getEveryVideos(new ExtraInfo(extraInfo), imageFile));
        } catch (Exception e) {
            List<String> response = new ArrayList<>();
            response.add(e.getMessage());
            return new ResponseDto(response);
        }
    }

    // 추가정보
    @PostMapping("/capture-without-image")
    public ResponseDto capture(@RequestParam("extra_info") String extraInfo) {
        try {
            return new ResponseDto(vibeService.getEveryVideos(new ExtraInfo(extraInfo)));
        } catch (Exception e) {
            List<String> response = new ArrayList<>();
            response.add(e.getMessage());
            return new ResponseDto(response);
        }
    }
}
