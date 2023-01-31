package com.example.vibecap_back.domain.model;

/**
 * 알림이 전송되게 된 원인
 */
public enum NoticeEvent {

    COMMENT("COMMENT"),
    SUB_COMMENT("SUB_COMMENT"),
    LIKE("LIKE");

    private String type;

    private NoticeEvent(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
