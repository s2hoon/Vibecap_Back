package com.example.vibecap_back.domain.post.application;

import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.post.dao.PostsLikeRepository;
import com.example.vibecap_back.domain.post.dao.PostsRepository;
import com.example.vibecap_back.domain.post.domain.Like.Likes;
import com.example.vibecap_back.domain.post.domain.Posts;
import com.example.vibecap_back.domain.post.dto.Response.PostListResponseDto;
import com.example.vibecap_back.domain.post.dto.Response.PostResponseDto;
import com.example.vibecap_back.domain.post.dto.Request.PostSaveRequestDto;
import com.example.vibecap_back.domain.post.dto.Request.PostUpdateRequestDto;
import com.example.vibecap_back.domain.post.exception.PostNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final MemberRepository memberRepository;
    private final PostsRepository postsRepository;

    private final PostsLikeRepository postsLikeRepository;

    /** 게시물 작성 API - 저장 **/
    @Transactional
    public Long save(PostSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    /** 게시물 수정 API **/
    @Transactional
    public Long update(Long PostId, PostUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(PostId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. post-id=" + PostId));

        posts.update(requestDto.getTitle(), requestDto.getBody());

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
    public List<PostListResponseDto> findByTag_name(String tagName) {
        return postsRepository.findByTag_name(tagName).stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    /** 게시물 좋아요 API **/
    public void postLike(Long postId, Long memberId) {
        Posts post = getPostInService(postId);
        Member member = getMemberInService(memberId);
        Optional<Likes> byPostAndUser = postsLikeRepository.findByPostAndMember(post, member);

        byPostAndUser.ifPresentOrElse(
                // 좋아요 있을경우 삭제
                postLike -> {
                    postsLikeRepository.delete(postLike);
                    post.discountLike(postLike);
                },
                // 좋아요가 없을 경우 좋아요 추가
                () -> {
                    Likes postLike = Likes.builder().build();

                    postLike.mappingPost(post);
                    postLike.mappingMember(member);
                    post.updateLikeCount();

                    postsLikeRepository.save(postLike);
                }
        );
    }

    private Posts getPostInService(Long postId) {
        Optional<Posts> byId = postsRepository.findById(postId);
        return byId.orElseThrow(() -> new PostNotFound("해당 게시글이 존재하지 않습니다."));
    }

    private Member getMemberInService(Long memberId) {
        Optional<Member> byMemberId = memberRepository.findByMemberId(memberId);
        Member member = byMemberId.orElseThrow(() -> new UsernameNotFoundException("게시글 작성 권한이 없습니다."));
        return member;
    }
}
