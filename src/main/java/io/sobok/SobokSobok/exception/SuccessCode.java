package io.sobok.SobokSobok.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    // auth
    SOCIAL_SIGNUP_SUCCESS(HttpStatus.CREATED, "소셜 회원가입에 성공했습니다."),
    SOCIAL_LOGIN_SUCCESS(HttpStatus.OK, "소셜 로그인에 성공했습니다.")
    ;

    private final HttpStatus code;
    private final String message;
}
