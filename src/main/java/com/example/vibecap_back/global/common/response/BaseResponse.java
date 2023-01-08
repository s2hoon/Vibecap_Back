package com.example.vibecap_back.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import static com.example.vibecap_back.global.common.response.BaseResponseStatus.*;

@Getter
@JsonPropertyOrder({"isSuccess", "code", "message", "body"})
public class BaseResponse<T> {

    @JsonProperty("is_success")
    private final Boolean isSuccess;

    private final int code;

    private final String message;

    // 해당 필드가 null인 경우 JSON에 표현되지 않는다.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    // 요청 성공
    public BaseResponse(T result) {
        this.isSuccess = SUCCESS.isSuccess();
        this.code = SUCCESS.getCode();
        this.message = SUCCESS.getMessage();
        this.result = result;
    }

    // 요청에 실패한 경우
    public BaseResponse(BaseResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.code = status.getCode();
        this.message = status.getMessage();
    }
}
