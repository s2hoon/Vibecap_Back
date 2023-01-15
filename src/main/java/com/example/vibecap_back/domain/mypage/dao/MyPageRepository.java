package com.example.vibecap_back.domain.mypage.dao;

import com.example.vibecap_back.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyPageRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

}
