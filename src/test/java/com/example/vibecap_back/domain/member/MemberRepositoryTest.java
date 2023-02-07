package com.example.vibecap_back.domain.member;

import com.example.vibecap_back.BoilerPlate;
import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.post.dao.PostsRepository;
import com.example.vibecap_back.domain.post.domain.Post;
import com.example.vibecap_back.domain.vibe.dao.VibeRepository;
import com.example.vibecap_back.domain.vibe.domain.Vibe;
import com.example.vibecap_back.factory.MemberFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

// TODO spring boot 없이 service, dao 객체를 사용한 테스트 작성
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private VibeRepository vibeRepository;
    @Autowired
    private PostsRepository postsRepository;

    /**
     * DB와의 연결이 제대로 되었는지 확인하기 위해 dummy data를 삽입하고 조회.
     */
    @Test
    @DisplayName("DB 동작 확인")
    void insertDummies() {
        IntStream.rangeClosed(1, 10).forEach(i->{
            Member dummy = MemberFactory.getMember(i);
            memberRepository.save(dummy);
        });
    }

    /**
     * 회원 탈퇴시 vibe와 post가 지워지는지 확인
     */
    @Test
    void 회원_탈퇴시_vibe_post_삭제_여부_테스트() {
        // given
        int testMemberNum = 2;
        int vibeAndPostPerMember = 5;
        BoilerPlate mockDB = null;
        try {
            mockDB = new BoilerPlate(testMemberNum, vibeAndPostPerMember);
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
        List<Member> members = mockDB.getMembers();
        Map<Long, List<Post>> posts = mockDB.getPosts();
        Map<Long, List<Vibe>> vibes = mockDB.getVibes();
        prepareTestDBData(members, vibes, posts, vibeAndPostPerMember);
        // when : 회원 탈퇴
        for (Member member : members) {
            memberRepository.delete(member);
            assertThat(member.getVibes().size()).isEqualTo(0);
            assertThat(member.getPosts().size()).isEqualTo(0);
        }

    }

    void prepareTestDBData(List<Member> members,
                           Map<Long, List<Vibe>> vibes, Map<Long, List<Post>> posts, int m) {
        for (Member member : members) {
            memberRepository.save(member);
            for (int i=0;i<m;i++) {
                vibeRepository.save(vibes.get(member.getMemberId()).get(i));
                postsRepository.save(posts.get(member.getMemberId()).get(i));
            }
        }
    }
}
