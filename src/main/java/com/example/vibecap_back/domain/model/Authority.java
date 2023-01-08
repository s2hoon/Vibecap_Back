package com.example.vibecap_back.domain.model;

public enum Authority {

    // 일반 사용자
    NORMAL("normal"),

    // 관리자
    ADMIN("admin");

    private String role;
    private Authority(String role) {
        this.role = role;
    }
}
