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
    INVALID_MEMBER_JWT(false,2300,"권한이 없는 회원의 접근입니다."),

    // post

    // vibe

    /**
     * 3XXX : 내부 오류
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
    SAVE_TEMPORARY_FILE_FAILED(false, 3500, "이미지 파일 전달 실패"),
    EMPTY_IMAGE(false, 3501, "이미지를 보내 주세요"),
    EXTERNAL_API_FAILED(false, 3502,"외부 API 호출 실패"),

    /**
     * 4XXX : DB 오류
     */
    DBCONN_ERROR(false, 4000, "데이터베이스 연결 오류");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
