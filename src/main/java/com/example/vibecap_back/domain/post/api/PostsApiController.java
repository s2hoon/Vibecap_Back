package com.example.vibecap_back.domain.post.api;

import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.mypage.exception.InvalidMemberException;
import com.example.vibecap_back.domain.post.application.PostService;
import com.example.vibecap_back.domain.post.dto.Response.PostDeleteResDto;
import com.example.vibecap_back.domain.post.dto.Response.PostListResponseDto;
import com.example.vibecap_back.domain.post.dto.Response.PostResponseDto;
import com.example.vibecap_back.domain.post.dto.Request.PostSaveRequestDto;
import com.example.vibecap_back.domain.post.dto.Request.PostUpdateRequestDto;
import com.example.vibecap_back.global.common.response.BaseException;
import com.example.vibecap_back.global.common.response.BaseResponse;
import com.example.vibecap_back.global.common.response.SuccessResponse;
import com.example.vibecap_back.global.config.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.vibecap_back.global.common.response.BaseResponseStatus.*;


//모든 내용에 필요
//현재 로그인한 유저의 정보가 PathVariable 로 들어오는 BoardID 의 작성자인 user 정보와 일치하는지 확인하고
//맞으면 아래 로직 수행, 틀리면 다른 로직(ResponseFail 등 커스텀으로 만들어서) 수행
/**
 * 게시글 -> 생성/수정/삭제/조회 API
 */
@RestController
@RequestMapping("/app/posts")
public class PostsApiController{
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    private final PostService postService;
    private final MemberRepository memberRepository;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public PostsApiController(PostService postService, MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.postService = postService;
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /** 게시물 작성 API **/
    @PostMapping("")
    public BaseResponse<Long> save(@RequestBody PostSaveRequestDto requestDto) {
        try {
            postService.checkMemberValid(requestDto.getMember().getMemberId());
            /*int userIdxByJwt = jwtTokenProvider.extractMemberId();
            if(requestDto.getMember().getMemberId() !=userIdxByJwt){
                return new BaseResponse<>(BaseResponseStatus.INVALID_MEMBER_JWT);
            }*/
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
    @DeleteMapping("/{postId}") ///{postId}/status 고민
    public BaseResponse<String> delete(@PathVariable Long postId, @RequestBody PostDeleteResDto postDeleteResDto) {
        // 삭제하려는 사용자의 ID와 해당 글 작성자의 ID가 동일한지 확인 필요
        try {
            postService.checkMemberValid(postDeleteResDto.getMemberId());
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

    /** 게시물 조회 API - 해시태그별 게시물(전체) **/
    @GetMapping("")
    public BaseResponse<List<PostListResponseDto>> findAll(@RequestParam String tagName) throws BaseException {

        try{
            if(postService.findByTag_Name(tagName).size() == 0){
                //System.out.println(postService.findByTag_Name(tagName));
                return new BaseResponse<>(NOT_EXISTS_TAG_NAME_POST);
            }
            List<PostListResponseDto> postListResponseDto = postService.findByTag_Name(tagName);
            return new BaseResponse<>(postListResponseDto);
        } catch (BaseException exception) {
            System.out.println(exception);
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /** 게시물 좋아요 API **/
    @PostMapping("/{postId}/like")
    public SuccessResponse<String> postLike(@PathVariable(name = "postId") Long postId, Long memberId) {

        postService.postLike(postId, memberId);

        return SuccessResponse.success(null);
    }

    /** 게시물 스크랩 API **/
    @PostMapping("/{postId}/scrap")
    public SuccessResponse<String> postScrap(@PathVariable(name = "postId") Long postId, Long memberId) {

        postService.postScrap(postId, memberId);

        return SuccessResponse.success(null);
    }
}
