package com.example.vibecap_back.domain.post.api;

import com.example.vibecap_back.domain.post.application.PostService;
import com.example.vibecap_back.domain.post.dto.Response.PostListResponseDto;
import com.example.vibecap_back.domain.post.dto.Response.PostResponseDto;
import com.example.vibecap_back.domain.post.dto.Request.PostSaveRequestDto;
import com.example.vibecap_back.domain.post.dto.Request.PostUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<PostListResponseDto> findAll(@PathVariable String tag_name) {
        return postService.findAllDesc(tag_name);
    }

    /** 언젠간 참고할지도...
     * 모든 게시글 조회
    // Param으로 tag_name을 받아와야하나?
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<PostResponseDto>> getPosts(@RequestParam int member_id) {
        try {
            List<PostResponseDto> getAllPostsRes = postProvider.retrievePosts(member_id);
            return new BaseResponse<>(getAllPostsRes);
        } catch (BaseException exception) {
            System.out.println(exception);
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 특정 게시글 조회
    @ResponseBody
    @GetMapping("/{postId}")
    public BaseResponse<List<PostResponseDto>> getPost(@PathVariable("post_id") int post_id) {
        try {
            List<PostResponseDto> postResponseDto = postProvider.getPost(post_id);
            return new BaseResponse<>(postResponseDto);
        } catch (BaseException exception) {
            System.out.println(exception);
            return new BaseResponse<>((exception.getStatus()));
        }

    }**/
}
