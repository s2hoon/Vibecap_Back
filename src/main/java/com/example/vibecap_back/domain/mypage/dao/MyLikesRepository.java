package com.example.vibecap_back.domain.mypage.dao;

import com.example.vibecap_back.domain.mypage.dto.response.GetMyLikesResponse;
import com.example.vibecap_back.domain.post.domain.Like.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MyLikesRepository extends JpaRepository<Likes, Long> {
    @Query("select l.post.postId from Likes l where l.member.memberId = :memberId")
    List<Long> findPostIdByMemberId(@Param("memberId") Long memberId);

    @Query("select new com.example.vibecap_back.domain.mypage.dto.response.GetMyLikesResponse(l.id, p.postId, p.vibe.vibeId, v.vibeImage) " +
            "from Likes l left join Posts p on l.post.postId in :postId and l.post.postId = p.postId left join Vibe v on l.member.memberId = :memberId and p.vibe.vibeId = v.vibeId " +
            "where l.member.memberId = :memberId order by l.id desc")
    List<GetMyLikesResponse> findMyLikesById(@Param("postId") List<Long> postId, @Param("memberId") Long memberId);

}
