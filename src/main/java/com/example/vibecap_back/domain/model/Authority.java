package com.example.vibecap_back.domain.model;

public enum Authority {

    // 일반 사용자
    ROLE_MEMBER("ROLE_MEMBER"),

    // 관리자
    ADMIN("ROLE_ADMIN");

    private String role;
    private Authority(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }
}
