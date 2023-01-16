package com.example.vibecap_back.domain.post.application;

import com.example.vibecap_back.domain.post.dao.PostsRepository;
import com.example.vibecap_back.domain.post.domain.Posts;
import com.example.vibecap_back.domain.post.dto.Response.PostListResponseDto;
import com.example.vibecap_back.domain.post.dto.Response.PostResponseDto;
import com.example.vibecap_back.domain.post.dto.Request.PostSaveRequestDto;
import com.example.vibecap_back.domain.post.dto.Request.PostUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostsRepository postsRepository;

    /** 게시물 작성 API - 저장 **/
    @Transactional
    public Long save(PostSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getPost_id();
    }

    /** 게시물 수정 API **/
    @Transactional
    public Long update(Long PostId, PostUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(PostId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. post-id=" + PostId));

        posts.update(requestDto.getTitle(), requestDto.getBody(), requestDto.getTag_name());

        return PostId;
    }

    /** 게시물 삭제 API **/
    @Transactional
    public void delete (Long postId) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. post-id=" + postId));

        postsRepository.delete(posts);
    }

    /** 게시물 조회 API - 특정 게시물 **/
    @Transactional(readOnly = true)
    public PostResponseDto findById(Long postId) {
        Posts entity = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. post-id=" + postId));

        return new PostResponseDto(entity);
    }

    /** 게시물 조회 API - 전체 **/
    @Transactional(readOnly = true)
    public List<PostListResponseDto> findAllDesc(String tag_name) {
        return postsRepository.findAllDesc(tag_name).stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }
}
