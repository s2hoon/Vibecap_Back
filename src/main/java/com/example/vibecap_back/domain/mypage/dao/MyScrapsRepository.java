package com.example.vibecap_back.domain.mypage.dao;

import com.example.vibecap_back.domain.mypage.dto.response.GetMyScrapsResponse;
import com.example.vibecap_back.domain.post.domain.Scrap.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MyScrapsRepository extends JpaRepository<Scrap, Long> {
    @Query("select s.post.postId from Scrap s where s.member.memberId = :memberId")
    List<Long> findPostIdByMemberId(@Param("memberId") Long memberId);

    @Query("select new com.example.vibecap_back.domain.mypage.dto.response.GetMyScrapsResponse(s.id, p.postId, p.vibe.vibeId, v.vibeImage) " +
            "from Scrap s left join Post p on s.post.postId in :postId and s.post.postId = p.postId left join Vibe v on s.member.memberId = :memberId and p.vibe.vibeId = v.vibeId " +
            "where s.member.memberId = :memberId order by s.id desc")
    List<GetMyScrapsResponse> findMyScrapsById(@Param("postId") List<Long> postId, @Param("memberId") Long memberId);
}
