package com.example.vibecap_back.domain.post.api;

import com.example.vibecap_back.domain.comment.dto.CommentDto;
import com.example.vibecap_back.domain.member.application.MemberDetailsService;
import com.example.vibecap_back.domain.post.application.PostService;
import com.example.vibecap_back.domain.post.domain.Posts;
import com.example.vibecap_back.domain.post.dto.Response.PostListResponseDto;
import com.example.vibecap_back.domain.post.dto.Response.PostResponseDto;
import com.example.vibecap_back.domain.post.dto.Request.PostSaveRequestDto;
import com.example.vibecap_back.domain.post.dto.Request.PostUpdateRequestDto;
import com.example.vibecap_back.global.common.response.BaseResponse;
import com.example.vibecap_back.global.common.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;
import java.util.List;


//모든 내용에 필요
//현재 로그인한 유저의 정보가 PathVariable 로 들어오는 BoardID 의 작성자인 user 정보와 일치하는지 확인하고
//맞으면 아래 로직 수행, 틀리면 다른 로직(ResponseFail 등 커스텀으로 만들어서) 수행
/**
 * 게시글 -> 생성/수정/삭제/조회 API
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/posts")
public class PostsApiController{

    @Autowired
    private final PostService postService;

    /** 게시물 작성 API **/
    @PostMapping("")
    public Long save(@RequestBody PostSaveRequestDto requestDto) {

        return postService.save(requestDto);
    }

    /** 게시물 수정 API **/
    @PatchMapping("/{postId}")
    public Long update(@PathVariable Long postId, @RequestBody PostUpdateRequestDto requestDto) {
        return postService.update(postId, requestDto);
    }

    /** 게시물 삭제 API **/
    @DeleteMapping("/{postId}") ///{postId}/status 고민
    public Long delete(@PathVariable Long postId) {
        postService.delete(postId);
        return postId;
    }

    /** 게시물 조회 API - 특정 게시물 **/
    @GetMapping("/{postId}")
    public PostResponseDto findById(@PathVariable Long postId) {

        return postService.findById(postId);
    }

    /** 게시물 조회 API - 해시태그별 게시물(전체) **/
    @GetMapping("")
    public List<PostListResponseDto> findAll(@RequestParam String tagName) {

        return postService.findByTag_name(tagName);
    }

    /** 게시물 좋아요 API **/
    @PostMapping("/{postId}/like")
    public SuccessResponse<String> postLike(@PathVariable(name = "postId") Long postId, Long memberId) {

        postService.postLike(postId, memberId);

        return SuccessResponse.success(null);
    }
}
