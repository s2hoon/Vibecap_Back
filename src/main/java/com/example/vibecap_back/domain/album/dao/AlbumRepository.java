package com.example.vibecap_back.domain.album.dao;

import com.example.vibecap_back.domain.vibe.domain.Vibe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Vibe, Long> {
    @Query("select v from Vibe v where v.memberId = :memberId order by v.vibeId desc")
    List<Vibe> findByMemberId(@Param("memberId") Long memberId);

    @Query("select v.vibeId from Vibe v where v.memberId = :memberId")
    List<Long> findVibeIdByMemberId(@Param("memberId") Long memberId);
}
