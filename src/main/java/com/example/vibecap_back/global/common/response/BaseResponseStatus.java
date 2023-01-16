package com.example.vibecap_back.global.common.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    /**
     * 1000 : Successed
     */
    SUCCESS(true, 1000, "요청에 성공했습니다."),

    /**
     * 2XXX : Request 내용 오류
     */
    // Common

    // album

    // member

    // mypage

    // post
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // vibe


    /**
     * 3XXX : 내부 로직 오류
     */
    // Common

    // album

    // member
    EMAIL_ALREADY_EXIST(false, 3200, "이미 가입된 이메일 주소입니다."),
    WRONG_EMAIL(false, 3201, "잘못된 이메일 주소입니다."),
    WRONG_PASSWORD(false, 3202, "잘못된 비밀번호입니다."),
    NO_SUCH_MEMBER_EXIST(false, 3303, "존재하지 않는 회원입니다."),

    // mypage

    // post

    // vibe

    /**
     * 4XXX : DB, server 오류
     */
    DBCONN_ERROR(false, 4000, "데이터베이스 연결 오류"),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다.");
    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
