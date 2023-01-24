package com.example.vibecap_back.domain.post.application;

import com.example.vibecap_back.domain.comment.domain.Comments;
import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.mypage.exception.InvalidMemberException;
import com.example.vibecap_back.domain.post.dao.PostsLikeRepository;
import com.example.vibecap_back.domain.post.dao.PostsRepository;
import com.example.vibecap_back.domain.post.dao.PostsScrapRepository;
import com.example.vibecap_back.domain.post.domain.Like.Likes;
import com.example.vibecap_back.domain.post.domain.Posts;
import com.example.vibecap_back.domain.post.domain.Scrap.Scrap;
import com.example.vibecap_back.domain.post.dto.Response.PostListResponseDto;
import com.example.vibecap_back.domain.post.dto.Response.PostResponseDto;
import com.example.vibecap_back.domain.post.dto.Request.PostSaveRequestDto;
import com.example.vibecap_back.domain.post.dto.Request.PostUpdateRequestDto;
import com.example.vibecap_back.domain.post.exception.PostNotFound;
import com.example.vibecap_back.global.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final MemberRepository memberRepository;
    private final PostsRepository postsRepository;

    private final PostsLikeRepository postsLikeRepository;
    private final PostsScrapRepository postsScrapRepository;

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
        Member member = getMemberInService(1l);

        Optional<Likes> byPostAndMember = postsLikeRepository.findByPostAndMember(post, member);

        byPostAndMember.ifPresentOrElse(
                // 좋아요 있을 경우 삭제
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

    /** 게시물 스크랩 API **/
    public void postScrap(Long postId, Long memberId) {
        Posts post = getPostInService(postId);
        Member member = getMemberInService(1l);
        Optional<Scrap> byPostAndMember = postsScrapRepository.findByPostAndMember(post, member);

        byPostAndMember.ifPresentOrElse(
                // 스크랩 있을경우 삭제
                postScrap -> {
                    postsScrapRepository.delete(postScrap);
                    post.discountScrap(postScrap);
                },
                // 스크랩이 없을 경우 좋아요 추가
                () -> {
                    Scrap postScrap = Scrap.builder().build();

                    postScrap.mappingPost(post);
                    postScrap.mappingMember(member);
                    post.updateScrapCount();

                    postsScrapRepository.save(postScrap);
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

    // 토큰으로 회원 권한 검사
    /*public void checkMemberValid(Member memberId) throws InvalidMemberException {
        // JWT 에서 email 추출
        String email = jwtTokenProvider.extractEmail();
        Optional<Member> member = postsRepository.findByEmail(email);

        // memberId와 접근한 회원이 같은지 확인
        if (!Objects.equals(memberId, member.get().getMemberId())) {
            throw new InvalidMemberException();
        }
    }*/
}
