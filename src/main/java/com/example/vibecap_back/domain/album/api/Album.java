package com.example.vibecap_back.domain.album.api;

import com.example.vibecap_back.domain.album.application.AlbumService;
import com.example.vibecap_back.domain.album.dao.AlbumRepository;
import com.example.vibecap_back.domain.album.dto.GetAlbumResponse;
import com.example.vibecap_back.domain.album.dto.GetVibeResponse;
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
     * [GET] /app/album/:member_id
     */
    @ResponseBody
    @GetMapping("{member_id}")
    public BaseResponse<GetAlbumResponse> getAlbum(@PathVariable("member_id") Long memberId) {
        try {
            myPageService.checkMemberValid(memberId);
            GetAlbumResponse getAlbumResponse = albumService.getAlbum(memberId);

            return new BaseResponse<>(getAlbumResponse);
        } catch (InvalidMemberException e) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_MEMBER_JWT);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

//    /**
//     * 앨범에서 개별 Vibe 조회
//     * [GET] /app/album/vibe/:vibe_id
//     */
//    @ResponseBody
//    @GetMapping("/{vibe_id}")
//    public BaseResponse<GetVibeResponse> getVibe(@PathVariable("vibe_id") Long vibeId) {
//        try {
//            GetVibeResponse getVibeResponse = albumService.getVibe(vibeId);
//
//            return new BaseResponse<>(getVibeResponse);
//        } catch (InvalidMemberException e) {
//            return new BaseResponse<>(BaseResponseStatus.INVALID_MEMBER_JWT);
//        } catch (BaseException exception) {
//            return new BaseResponse<>(exception.getStatus());
//        }
//    }

//    /**
//     * 앨범에서 개별 Vibe 삭제
//     * [PATCH] /app/album/{vibe_id}
//     */
//    @ResponseBody
//    @DeleteMapping("/{vibe_id}")
//    public BaseResponse<String> deleteVibe(@PathVariable("vibe_id") Long vibeId) {
//        try {
//            albumService.deleteVibe(vibeId);
//
//            String result = "해당 Vibe 삭제에 성공했습니다.";
//            return new BaseResponse<>(result);
//        } catch (InvalidMemberException e) {
//            return new BaseResponse<>(BaseResponseStatus.INVALID_MEMBER_JWT);
//        } catch (BaseException exception) {
//            return new BaseResponse<>(exception.getStatus());
//        }
//    }
}

