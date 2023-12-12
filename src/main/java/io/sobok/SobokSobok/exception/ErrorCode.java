package io.sobok.SobokSobok.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // common
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "잘못된 Request body입니다."),

    // auth
    UNREGISTERED_USER(HttpStatus.NOT_FOUND, "등록되지 않은 사용자입니다."),
    ALREADY_EXISTS_USER(HttpStatus.CONFLICT, "이미 회원가입이 완료된 사용자입니다."),
    ;

    private final HttpStatus code;
    private final String message;
}
