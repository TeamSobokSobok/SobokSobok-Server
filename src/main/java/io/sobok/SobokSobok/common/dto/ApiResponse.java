package io.sobok.SobokSobok.common.dto;

import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private final int code;
    private final String message;
    private T data;

    public static <T> ApiResponse<T> success(SuccessCode successCode) {
        return new ApiResponse<>(successCode.getCode().value(), successCode.getMessage(), null);
    }

    public static <T> ApiResponse<T> success(SuccessCode successCode, T data) {
        return new ApiResponse<>(successCode.getCode().value(), successCode.getMessage(), data);
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getCode().value(), errorCode.getMessage(), null);
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, String message) {
        return new ApiResponse<>(errorCode.getCode().value(), message, null);
    }
}
