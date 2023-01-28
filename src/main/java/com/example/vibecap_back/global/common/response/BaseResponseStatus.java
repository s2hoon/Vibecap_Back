package com.example.vibecap_back.global.common.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    /**
     * 1000 : Successed
     */
    SUCCESS(true, 1000, "요청에 성공했습니다."),
    //NOT_EXISTS_POST(true,3012,"게시물이 존재하지 않습니다."),

    /**
     * 2XXX : Request 내용 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    FILE_SAVE_ERROR(false, 2001, "파일 저장에 실패하였습니다."),
    FILE_DELETE_ERROR(false, 2002, "파일 삭제에 실패하였습니다."),

    // album
    NO_ACCESS_TO_VIBE(false, 2100, "해당 바이브에 대한 접근 권한이 없습니다."),

    // member

    // mypage
    INVALID_MEMBER_JWT(false,2300,"권한이 없는 회원의 접근입니다."),
    EMPTY_PROFILE_IMAGE(false, 2301, "프로필 이미지를 입력해주세요."),

    // post
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),
    POST_POSTS_INVALID_TITLE(false, 2011, "제목의 글자수를 확인해주세요."),
    POST_POSTS_INVALID_BODY(false, 2012, "내용의 글자수를 확인해주세요."),

    // vibe


    /**
     * 3XXX : 내부 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는 데 실패하였습니다."),

    // album

    // member
    EMAIL_ALREADY_EXIST(false, 3200, "이미 가입된 이메일 주소입니다."),
    WRONG_EMAIL(false, 3201, "잘못된 이메일 주소입니다."),
    WRONG_PASSWORD(false, 3202, "잘못된 비밀번호입니다."),
    DISABLED_MEMBER(false, 3203, "탈퇴한 회원입니다."),
    NO_SUCH_MEMBER_EXIST(false, 3303, "존재하지 않는 회원입니다."),

    // mypage

    // post
    DELETE_FAIL_POST(false, 3010, "게시물 삭제에 실패하였습니다."),
    FAILED_GET_POST(false,3011,"게시물 조회에 실패하였습니다."),
    NOT_EXISTS_TAG_NAME_POST(true,3011,"해당 태그를 가진 게시물이 없습니다."),
    NOT_EXISTS_POST(false,3012,"게시물이 존재하지 않습니다."),

    // vibe
    SAVE_TEMPORARY_FILE_FAILED(false, 3500, "이미지 파일 전달 실패. 요청을 다시 전송해주세요."),
    EMPTY_IMAGE(false, 3501, "이미지를 보내 주세요"),
    EXTERNAL_API_FAILED(false, 3502,"외부 API 호출 실패"),
    NO_PROPER_VIDEO(false, 3503, "적절한 음악이 없습니다. (주어진 정보가 너무 복잡한 경우 발생) "),

    /**
     * 4XXX : DB, server 오류
     */
    DBCONN_ERROR(false, 4000, "데이터베이스 연결 오류"),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),;
    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
