package com.example.vibecap_back;

import com.example.vibecap_back.domain.comment.domain.Comments;
import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.member.dto.MemberDto;
import com.example.vibecap_back.domain.member.exception.EmailAlreadyExistException;
import com.example.vibecap_back.domain.post.dao.PostsRepository;
import com.example.vibecap_back.domain.post.domain.Post;
import com.example.vibecap_back.domain.vibe.dao.VibeRepository;
import com.example.vibecap_back.domain.vibe.domain.Vibe;
import com.example.vibecap_back.factory.MemberFactory;
import com.example.vibecap_back.factory.PostFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoilerPlate {

    private static final String IMAGE_URL = "https://cloud.google.com/static/vision/docs/images/setagaya_small.jpeg";
    private static final String IMAGE_LABEL ="꽃";
    private static final String VIDEO_LINK ="https://www.youtube.com/watch?v=7b2Nbr34BW4";
    private static final String[] seasons = {"봄", "여름", "가을", "겨울"};
    private static final String[] times = {"아침", "점심", "저녁"};
    private static final String[] feelings ={"신나는", "포근한", "신선한", "낭만적인", "잔잔한", "우울한", "공허한"};
    private static final int LABEL_IDX = 0;
    private static final int SEASON_IDX = 1;
    private static final int TIME_IDX = 2;
    private static final int FEELING_IDX = 3;

    private List<Member> members;   // (배열 인덱스) + 1 = 회원 id
    private Map<Long, List<Post>> posts ;
    private Map<Long, List<Vibe>> vibes;

    public BoilerPlate(int n, int m) throws Exception{
        members = new ArrayList<>(n);
        posts = new HashMap<>();
        vibes = new HashMap<>();
        prepare(n, m);
    }

    /**
     * 준비한 mock data를 DB에 저장
     * @param memberRepository
     * @param vibeRepository
     * @param postsRepository
     * @param vibeAndPostPerMember
     */
    public void persist(MemberRepository memberRepository,
                        VibeRepository vibeRepository, PostsRepository postsRepository,
                        int vibeAndPostPerMember, boolean savePost) {
        for (Member member : members) {
            memberRepository.save(member);
            for (int i=0; i<vibeAndPostPerMember; i++) {
                vibeRepository.save(vibes.get(member.getMemberId()).get(i));
                if (savePost)
                    postsRepository.save(posts.get(member.getMemberId()).get(i));
            }
        }
    }

    /**
     * n명의 회원을 생성하고 각 회원이 만든 m개의 vibe와 그에 대한 게시글을 DB에 넣는다.
     */
    private void prepare(int n, int m) throws Exception{
        createDummyMembers(n);
        createDummyVibesAndPosts(m);
    }

    public List<Member> getMembers() {
        return members;
    }

    public Map<Long, List<Post>> getPosts() {
        return posts;
    }

    public Map<Long, List<Vibe>> getVibes() {
        return vibes;
    }

    /**
     * n명의 dummy member 회원가입
     */
    void createDummyMembers(int n) throws EmailAlreadyExistException {
        Member dummy;
        MemberDto memberDto;
        for (int i=1; i<=n; i++) {
            dummy = MemberFactory.selectMember(i);
            members.add(dummy);
        }
    }

    /**
     * 회원 1명이 n개의 dummy vibe, post 생성
     * @param n
     */
    void createDummyVibesAndPosts(int n) {
        Vibe dummyVibe;
        Post dummyPost;
        // "{label} {season} {time} {feeling}"
        String[] keywords = new String[4];
        keywords[LABEL_IDX] = IMAGE_LABEL;
        for (Member member : members) {
            List<Post> postOfMember = new ArrayList<>();
            List<Vibe> vibeOfMember = new ArrayList<>();
            for (int i=1; i<=n; i++) {
                keywords[SEASON_IDX] = seasons[(i-1) % (seasons.length)];
                keywords[TIME_IDX] = times[(i-1) % (times.length)];
                keywords[FEELING_IDX] = feelings[(i-1) % (feelings.length)];
                dummyVibe = Vibe.builder()
                        .member(member)
                        .vibeImage(IMAGE_URL)
                        .youtubeLink(VIDEO_LINK)
                        .vibeKeywords(String.join(" ", keywords))
                        .build();
                // vibeRepository.save(dummyVibe);
                vibeOfMember.add(dummyVibe);
                dummyPost = PostFactory.getPost(member, dummyVibe, i);
                dummyPost.setTagName(keywords[FEELING_IDX]);
                postOfMember.add(dummyPost);
                // postsRepository.save(dummyPost);
            }
            posts.put(member.getMemberId(), postOfMember);
            vibes.put(member.getMemberId(), vibeOfMember);

        }
    }

    void insertDummyComments(int n) {
        Comments comment;
        //
    }

}
