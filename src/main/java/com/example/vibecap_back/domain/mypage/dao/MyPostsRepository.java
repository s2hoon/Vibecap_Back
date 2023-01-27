package com.example.vibecap_back.domain.mypage.dao;

import com.example.vibecap_back.domain.post.domain.Posts;
import com.example.vibecap_back.domain.mypage.dto.response.GetMyPostsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MyPostsRepository extends JpaRepository<Posts, Long> {
    @Query("select new com.example.vibecap_back.domain.mypage.dto.response.GetMyPostsResponse(p.postId, p.vibe.vibeId, v.vibeImage) " +
            "from Posts p left join Vibe v on p.member.memberId = :memberId where p.vibe.vibeId = v.vibeId order by p.postId desc")
    List<GetMyPostsResponse> findMyPostsByMemberId(@Param("memberId") Long memberId);
}
