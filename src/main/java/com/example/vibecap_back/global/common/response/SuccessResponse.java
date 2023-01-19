package com.example.vibecap_back.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/** 나중에 지울 파일 **/
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse<T> {
    private String message;
    private T data;

    public static <T> SuccessResponse<T> success(T data) {
        return SuccessResponse.<T>builder()
                .message("success")
                .data(data)
                .build();
    }
}