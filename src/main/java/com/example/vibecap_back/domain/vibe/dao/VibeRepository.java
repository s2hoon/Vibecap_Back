package com.example.vibecap_back.domain.vibe.dao;

import com.example.vibecap_back.domain.vibe.domain.Vibe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VibeRepository extends JpaRepository<Vibe, Long> {
}
