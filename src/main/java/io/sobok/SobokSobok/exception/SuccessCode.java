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

    // user
    NICKNAME_CHECK_SUCCESS(HttpStatus.OK, "닉네임 중복 확인에 성공했습니다."),

    // pill
    ADD_PILL_SUCCESS(HttpStatus.CREATED, "약 추가에 성공했습니다."),
    GET_PILL_COUNT_SUCCESS(HttpStatus.OK, "약 개수 조회에 성공했습니다."),
    SEND_PILL_SUCCESS(HttpStatus.CREATED, "약 전송에 성공했습니다."),

    // friend
    ADD_FRIEND_SUCCESS(HttpStatus.OK, "공유 요청에 성공했습니다."),
    ;

    private final HttpStatus code;
    private final String message;
}
