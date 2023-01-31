package com.example.vibecap_back.domain.post.application;

import com.example.vibecap_back.domain.comment.dao.CommentRepository;
import com.example.vibecap_back.domain.comment.dao.SubCommentRepository;
import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.mypage.exception.InvalidMemberException;
import com.example.vibecap_back.domain.post.dao.PostsLikeRepository;
import com.example.vibecap_back.domain.post.dao.PostsRepository;
import com.example.vibecap_back.domain.post.dao.PostsScrapRepository;
import com.example.vibecap_back.domain.post.domain.Like.Likes;
import com.example.vibecap_back.domain.post.domain.Post;
import com.example.vibecap_back.domain.post.domain.Scrap.Scrap;
import com.example.vibecap_back.domain.post.dto.Response.*;
import com.example.vibecap_back.domain.post.dto.Request.PostSaveRequestDto;
import com.example.vibecap_back.domain.post.dto.Request.PostUpdateRequestDto;
import com.example.vibecap_back.domain.post.exception.PostNotFound;
import com.example.vibecap_back.domain.vibe.dao.VibeRepository;
import com.example.vibecap_back.global.common.response.BaseException;
import com.example.vibecap_back.global.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private final VibeRepository vibeRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final CommentRepository commentRepository;
    private final SubCommentRepository subCommentRepository;

    @Autowired
    public PostService(MemberRepository memberRepository, PostsLikeRepository postsLikeRepository,
                       PostsRepository postsRepository, PostsScrapRepository postsScrapRepository,
                       VibeRepository vibeRepository, JwtTokenProvider jwtTokenProvider,
                       CommentRepository commentRepository, SubCommentRepository subCommentRepository) throws InvalidMemberException {
        this.memberRepository = memberRepository;
        this.postsLikeRepository = postsLikeRepository;
        this.postsRepository = postsRepository;
        this.postsScrapRepository = postsScrapRepository;
        this.vibeRepository = vibeRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.commentRepository = commentRepository;
        this.subCommentRepository = subCommentRepository;
    }


    /** 게시물 작성 API - 저장 **/
    @Transactional
    public Long save(PostSaveRequestDto requestDto) {
        /**
         * tag_name은 "{사용자가 보낸 태그 문자열} {해당 vibe를 생성할 때 선택한 feeling}" 형태이다.
         * feeling은 요청에 없기 때문에 찾아서 넣어줘야 한다.
         */
        String feeling;
        String vibeKeywords;
        String[] vibeKeywordToken;
        vibeKeywords = vibeRepository.getReferenceById(requestDto.getVibe().getVibeId()).getVibeKeywords();
        vibeKeywordToken = vibeKeywords.split(" ");
        feeling = vibeKeywordToken[vibeKeywordToken.length-1];

        requestDto.setTagName(requestDto.getTagName() + " " + feeling);

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

    /** 게시물 조회 API - 특정 게시물(1개 조회)**/
    @Transactional(readOnly = false)
    public PostResponseDto retrievePosts(Long postId) throws BaseException {
        try{
            Long totalCommentCount = commentRepository.countCommentsByPost_PostId(postId) + subCommentRepository.countSubCommentsByPost_PostId(postId);
            postsRepository.updateCount(totalCommentCount, postId);

            PostResponseDto getPost = postsRepository.findByPost(postId);

            return getPost;
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

    /** 게시물 조회 API - tag별 **/
    @Transactional(readOnly = true)
    public List<PostListResponseDto> findByTag_Name(String tagName) throws BaseException {
        return postsRepository.findByTagName(tagName).stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    /** 게시글 조회 API - db에 존재하는 모든 게시글 **/
    public List<PostListResponseDto> findEveryPost() throws BaseException {
        return postsRepository.selectAllPost().stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    /** 게시글 조회 API - db에 존재하는 모든 게시글 중 생성 날짜가 최근이면서 좋아요 수가 가장 많은 게시물 **/
    public List<PostWeeklyReqDto> findWeeklyPost() throws BaseException {
        return postsRepository.findTop3ByOrderByLikeNumberDesc().stream()
                .map(PostWeeklyReqDto::new)
                .collect(Collectors.toList());
    }

    /** 게시글 조회 API - 페이징 처리 (tag별) **/
    public Page<PostListResponseDto> findByTag_NameByPaging(String tagName, Pageable pageable) throws BaseException {
        List<PostListResponseDto> postListByTagName = postsRepository.findByTagName(tagName).stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());

        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), postListByTagName.size());
        final Page<PostListResponseDto> page = new PageImpl<>(postListByTagName.subList(start, end), pageable, postListByTagName.size());

        return page;
    }

    /*** 게시글 조회 API - 페이징 처리 (db에 존재하는 모든 게시글) **/
    public Page<PostListResponseDto> findEveryPostByPaging(Pageable pageable) throws BaseException {
        List<PostListResponseDto> postList = postsRepository.findAll().stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());

        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), postList.size());
        final Page<PostListResponseDto> page = new PageImpl<>(postList.subList(start, end), pageable, postList.size());

        return page;
    }

    /**
     * 게시물 좋아요 API
     **/
    @Transactional
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
    @Transactional
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
