package com.example.vibecap_back.domain.model;

public enum MemberStatus {

    // 활성화된 계정
    ACTIVE("active"),

    // 휴면 계정
    SLEEP("sleep"),

    // 탈퇴 계정
    QUIT("quit");
    private String status;

    private MemberStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
