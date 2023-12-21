package io.sobok.SobokSobok.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    // auth
    SOCIAL_SIGNUP_SUCCESS(HttpStatus.CREATED, "소셜 회원가입에 성공했습니다."),
    SOCIAL_LOGIN_SUCCESS(HttpStatus.OK, "소셜 로그인에 성공했습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃에 성공했습니다."),
    TOKEN_REFRESH_SUCCESS(HttpStatus.OK, "토큰 재발급에 성공했습니다."),
    USER_LEAVE_SUCCESS(HttpStatus.OK, "소복소복 서비스 탈퇴에 성공했습니다."),
    ;

    private final HttpStatus code;
    private final String message;
}
