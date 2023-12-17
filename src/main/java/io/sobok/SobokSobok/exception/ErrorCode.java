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
    ALREADY_USING_USERNAME(HttpStatus.CONFLICT, "이미 사용중인 username입니다."),

    // external
    INVALID_EXTERNAL_REQUEST_DATA(HttpStatus.BAD_REQUEST, "외부 API 요청에 잘못된 데이터가 전달됐습니다."),
    ;

    private final HttpStatus code;
    private final String message;
}