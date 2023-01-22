package com.example.vibecap_back.domain.album.api;

import com.example.vibecap_back.domain.album.application.AlbumService;
import com.example.vibecap_back.domain.album.dao.AlbumRepository;
import com.example.vibecap_back.domain.album.dto.GetAlbumResponse;
import com.example.vibecap_back.domain.album.dto.request.GetAlbumRequest;
import com.example.vibecap_back.domain.mypage.application.MyPageService;
import com.example.vibecap_back.domain.mypage.exception.InvalidMemberException;
import com.example.vibecap_back.global.common.response.BaseException;
import com.example.vibecap_back.global.common.response.BaseResponse;
import com.example.vibecap_back.global.common.response.BaseResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 앨범
 */
@RestController
@RequestMapping("/app/album")
public class Album {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final AlbumRepository albumRepository;
    private final AlbumService albumService;
    private final MyPageService myPageService;

    @Autowired
    public Album(AlbumRepository albumRepository, AlbumService albumService, MyPageService myPageService) {
        this.albumRepository = albumRepository;
        this.albumService = albumService;
        this.myPageService = myPageService;
    }


    /**
     * 앨범 조회
     * [GET] /app/album
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetAlbumResponse> getAlbum(@RequestBody GetAlbumRequest request) {
        try {
            myPageService.checkMemberValid(request.getMemberId());
            GetAlbumResponse getAlbumResponse = albumService.getAlbum(request);

            return new BaseResponse<>(getAlbumResponse);
        } catch (InvalidMemberException e) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_MEMBER_JWT);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


}

