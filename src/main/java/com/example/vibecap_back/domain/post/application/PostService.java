package com.example.vibecap_back.domain.post.application;

import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.mypage.exception.InvalidMemberException;
import com.example.vibecap_back.domain.post.dao.PostsLikeRepository;
import com.example.vibecap_back.domain.post.dao.PostsRepository;
import com.example.vibecap_back.domain.post.dao.PostsScrapRepository;
import com.example.vibecap_back.domain.post.domain.Like.Likes;
import com.example.vibecap_back.domain.post.domain.Post;
import com.example.vibecap_back.domain.post.domain.Scrap.Scrap;
import com.example.vibecap_back.domain.post.dto.Response.PostLikeResDto;
import com.example.vibecap_back.domain.post.dto.Response.PostListResponseDto;
import com.example.vibecap_back.domain.post.dto.Response.PostResponseDto;
import com.example.vibecap_back.domain.post.dto.Request.PostSaveRequestDto;
import com.example.vibecap_back.domain.post.dto.Request.PostUpdateRequestDto;
import com.example.vibecap_back.domain.post.dto.Response.PostScrapResDto;
import com.example.vibecap_back.domain.post.exception.PostNotFound;
import com.example.vibecap_back.global.common.response.BaseException;
import com.example.vibecap_back.global.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.example.vibecap_back.global.common.response.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class PostService {

    private final MemberRepository memberRepository;
    private final PostsRepository postsRepository;
    private final PostsLikeRepository postsLikeRepository;
    private final PostsScrapRepository postsScrapRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public PostService(MemberRepository memberRepository,PostsLikeRepository postsLikeRepository,
                       PostsRepository postsRepository, PostsScrapRepository postsScrapRepository, JwtTokenProvider jwtTokenProvider) throws InvalidMemberException {
        this.memberRepository = memberRepository;
        this.postsLikeRepository = postsLikeRepository;
        this.postsRepository = postsRepository;
        this.postsScrapRepository = postsScrapRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    /** 게시물 작성 API - 저장 **/
    @Transactional
    public Long save(PostSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getPostId();
    }

    /** 게시물 수정 API **/
    @Transactional
    public Long update(Long PostId, PostUpdateRequestDto requestDto) {

        Post posts = postsRepository.findById(PostId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. post-id=" + PostId));

        posts.update(requestDto.getTitle(), requestDto.getBody());

        return PostId;
    }

    /** 게시물 삭제 API **/
    @Transactional
    public void delete (Long postId) {
        Post posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. post-id=" + postId));

        postsRepository.delete(posts);
    }

    /** 게시물 조회 API - 특정 게시물 **/
    @Transactional(readOnly = true)
    public List<PostResponseDto> retrievePosts(Long postId) throws BaseException {
        try{
            List<PostResponseDto> getPosts = postsRepository.findByPost(postId);
            return getPosts;
        }
        catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(SERVER_ERROR);
        }
    }

    /**
     * 게시글이 존재하는지 확인
     **/
    public Long checkPostExist(Long postId) throws BaseException{
        return postsRepository.findByPostId(postId);
    }

    /** 게시물 조회 API - 태그별 게시물 **/
    @Transactional(readOnly = true)
    public List<PostListResponseDto> findByTag_Name(String tagName) throws BaseException {
        return postsRepository.findByTagName(tagName).stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    /** 게시물 조회 API - 전체 **/
    @Transactional(readOnly = true)
    public List<PostListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 게시물 좋아요 API
     **/
    public PostLikeResDto postLike(Long postId, Long memberId) {
        Post post = getPostInService(postId);
        Member member = getMemberInService(memberId);

        PostLikeResDto postLikeResDto = new PostLikeResDto();

        Optional<Likes> byPostAndMember = postsLikeRepository.findByPostAndMember(post, member);

        byPostAndMember.ifPresentOrElse(
                // 좋아요 있을 경우 삭제
                postLike -> {
                    post.discountLike(postLike);
                    post.updateLikeCount();

                    postsLikeRepository.save(postLike);
                    postsLikeRepository.delete(postLike);
                    //result.set("해당 게시물의 좋아요를 취소하였습니다.");
                    postLikeResDto.setLikeOrElse("해당 게시물의 좋아요를 취소하였습니다.");

                },
                // 좋아요가 없을 경우 좋아요 추가
                () -> {
                    Likes postLike = Likes.builder().build();

                    postLike.mappingPost(post);
                    postLike.mappingMember(member);
                    post.updateLikeCount();

                    postsLikeRepository.save(postLike);
                    //result.set("해당 게시물에 좋아요를 눌렀습니다.");
                    postLikeResDto.setLikeOrElse("해당 게시물에 좋아요를 눌렀습니다.");
                }
        );
        return postLikeResDto;
    }

    /** 게시물 스크랩 API **/
    public PostScrapResDto postScrap(Long postId, Long memberId) {
        Post post = getPostInService(postId);
        Member member = getMemberInService(memberId);

        PostScrapResDto postScrapResDto = new PostScrapResDto();

        Optional<Scrap> byPostAndMember = postsScrapRepository.findByPostAndMember(post, member);

        byPostAndMember.ifPresentOrElse(
                // 스크랩 있을경우 삭제
                postScrap -> {
                    post.discountScrap(postScrap);
                    post.updateScrapCount();

                    postsScrapRepository.save(postScrap);
                    postsScrapRepository.delete(postScrap);
                    postScrapResDto.setScrapOrElse("해당 게시물의 스크랩을 취소하였습니다.");
                },
                // 스크랩이 없을 경우 좋아요 추가
                () -> {
                    Scrap postScrap = Scrap.builder().build();

                    postScrap.mappingPost(post);
                    postScrap.mappingMember(member);
                    post.updateScrapCount();

                    postsScrapRepository.save(postScrap);
                    postScrapResDto.setScrapOrElse("해당 게시물을 스크랩하였습니다.");
                }
        );
        return  postScrapResDto;
    }

    public Post getPostInService(Long postId) {
        Optional<Post> byId = postsRepository.findById(postId);
        return byId.orElseThrow(() -> new PostNotFound("해당 게시글이 존재하지 않습니다."));
    }

    public Member getMemberInService(Long memberId) {
        Optional<Member> byMemberId = memberRepository.findByMemberId(memberId);
        Member member = byMemberId.orElseThrow(() -> new UsernameNotFoundException("게시글 작성 권한이 없습니다."));
        return member;
    }

    /** 토큰으로 회원 권한 검사 **/
    public void checkMemberValid(Long memberId) throws InvalidMemberException {
        // JWT 에서 email 추출
        String email = jwtTokenProvider.extractEmail();
        Optional<Member> member = memberRepository.findByEmail(email);

        // memberId와 접근한 회원이 같은지 확인
        if (!Objects.equals(memberId, member.get().getMemberId())) {
            throw new InvalidMemberException();
        } else {
            System.out.println("memberId와 접근한 회원이 동일합니다.");
        }

    }
}
