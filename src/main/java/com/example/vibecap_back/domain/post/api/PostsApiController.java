package com.example.vibecap_back.domain.post.api;

import com.example.vibecap_back.domain.mypage.exception.InvalidMemberException;
import com.example.vibecap_back.domain.post.application.PostService;
import com.example.vibecap_back.domain.post.dto.Request.*;
import com.example.vibecap_back.domain.post.dto.Response.PostLikeResDto;
import com.example.vibecap_back.domain.post.dto.Response.PostListResponseDto;
import com.example.vibecap_back.domain.post.dto.Response.PostResponseDto;
import com.example.vibecap_back.domain.post.dto.Response.PostScrapResDto;
import com.example.vibecap_back.global.common.response.BaseException;
import com.example.vibecap_back.global.common.response.BaseResponse;
import com.example.vibecap_back.global.config.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.vibecap_back.global.common.response.BaseResponseStatus.*;


/**
 * 게시글 -> 생성/수정/삭제/조회 API
 */
@RestController
@RequestMapping("/app/posts")
public class PostsApiController{
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final PostService postService;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public PostsApiController(PostService postService, JwtTokenProvider jwtTokenProvider) {
        this.postService = postService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /** 게시물 작성 API **/
    @PostMapping("")
    public BaseResponse<Long> save(@RequestBody PostSaveRequestDto requestDto) {
        try {
            postService.checkMemberValid(requestDto.getMember().getMemberId());
            if(requestDto.getTitle().length() > 32)
            {
                return new BaseResponse<>(POST_POSTS_INVALID_TITLE);
            }
            if(requestDto.getBody().length() > 140)
            {
                return new BaseResponse<>(POST_POSTS_INVALID_BODY);
            }
        } catch (InvalidMemberException e) {
            throw new RuntimeException(e);
        }
        Long result = postService.save(requestDto);
        return new BaseResponse<>(result);
    }

    /** 게시물 수정 API **/
    @PatchMapping("/{postId}")
    public BaseResponse<String> update(@PathVariable Long postId, @RequestBody PostUpdateRequestDto requestDto) {
        try {
            postService.checkMemberValid(requestDto.getMember().getMemberId());

            if(requestDto.getTitle().length() > 32)
            {
                return new BaseResponse<>(POST_POSTS_INVALID_TITLE);
            }
            if(requestDto.getBody().length() > 140)
            {
                return new BaseResponse<>(POST_POSTS_INVALID_BODY);
            }
            postService.update(postId, requestDto);
            String result = "게시물 수정을 완료했습니다";
            return new BaseResponse<>(result);
        }catch (InvalidMemberException e) {
            throw new RuntimeException(e);
        }
    }

    /** 게시물 삭제 API **/
    @DeleteMapping("/{postId}")
    public BaseResponse<String> delete(@PathVariable Long postId, @RequestBody PostDeleteDto postDeleteDto) {
        try {
            postService.checkMemberValid(postDeleteDto.getMemberId());
            postService.delete(postId);
            String result = "삭제를 완료했습니다";
            return new BaseResponse<>(result);
        } catch (InvalidMemberException e) {
            throw new RuntimeException(e);
        }
    }

    /** 게시물 조회 API - 특정 게시물 **/
    @GetMapping("/{postId}")
    public BaseResponse<List<PostResponseDto>> retrievePosts(@PathVariable Long postId) throws BaseException {

        try {
            if(postService.checkPostExist(postId)==null){
                return new BaseResponse<>(NOT_EXISTS_POST);
            }
            List<PostResponseDto> postResponseDto = postService.retrievePosts(postId);
            return new BaseResponse<>(postResponseDto);
        } catch (BaseException exception) {
            System.out.println(exception);
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 게시물 조회 API - 해시태그별 게시물 **/
    @GetMapping("")
    public BaseResponse<List<PostListResponseDto>> findAll(
            @RequestParam(required = false) String tagName) throws BaseException {

        if (tagName == null)
            return findAll();

        try{
            if(postService.findByTag_Name(tagName).size() == 0){
                return new BaseResponse<>(NOT_EXISTS_TAG_NAME_POST);
            }
            List<PostListResponseDto> postListResponseDto = postService.findByTag_Name(tagName);
            return new BaseResponse<>(postListResponseDto);
        } catch (BaseException exception) {
            System.out.println(exception);
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    public BaseResponse<List<PostListResponseDto>> findAll() {
        try {
            if (postService.findEveryPost().size() == 0)
                return new BaseResponse<>(NOT_EXISTS_POST);
            List<PostListResponseDto> postListResponseDto = postService.findEveryPost();
            return new BaseResponse<>(postListResponseDto);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /** 게시물 좋아요 API **/
    @PostMapping("/{postId}/like")
    public BaseResponse<PostLikeResDto> postLike(@PathVariable(name = "postId") Long postId, @RequestBody PostLikeDto postLikeDto) {
        try {
            postService.checkMemberValid(postLikeDto.getMemberId());
            PostLikeResDto postLikeResDto = postService.postLike(postId, postLikeDto.getMemberId());
            return new BaseResponse<>(postLikeResDto);
        } catch (InvalidMemberException e) {
            throw new RuntimeException(e);
        }
    }

    /** 게시물 스크랩 API **/
    @PostMapping("/{postId}/scrap")
    public BaseResponse<PostScrapResDto> postScrap(@PathVariable(name = "postId") Long postId, @RequestBody PostScrapDto postScrapDto) {
        try {
            postService.checkMemberValid(postScrapDto.getMemberId());
            PostScrapResDto postScrapResDto = postService.postScrap(postId, postScrapDto.getMemberId());;
            return new BaseResponse<>(postScrapResDto);
        } catch (InvalidMemberException e) {
            throw new RuntimeException(e);
        }
    }
}
