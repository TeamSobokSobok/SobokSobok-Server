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
    GET_USERNAME_SUCCESS(HttpStatus.OK, "유저 이름 조회에 성공했습니다."),

    // pill
    ADD_PILL_SUCCESS(HttpStatus.CREATED, "약 추가에 성공했습니다."),
    GET_PILL_COUNT_SUCCESS(HttpStatus.OK, "약 개수 조회에 성공했습니다."),
    SEND_PILL_SUCCESS(HttpStatus.CREATED, "약 전송에 성공했습니다."),
    DELETE_PILL_SUCCESS(HttpStatus.OK, "약 삭제에 성공했습니다."),

    // friend
    ADD_FRIEND_SUCCESS(HttpStatus.OK, "공유 요청에 성공했습니다."),
    GET_FRIEND_LIST_SUCCESS(HttpStatus.OK, "친구 리스트 조회에 성공했습니다."),
    HANDLE_FRIEND_REQUEST_SUCCESS(HttpStatus.OK, "공유 응답에 성공했습니다."),
    UPDATE_FRIEND_NAME_SUCCESS(HttpStatus.OK, "멤버 이름 수정에 성공했습니다."),

    // notice
    GET_NOTICE_LIST_SUCCESS(HttpStatus.OK, "알림 리스트 조회에 성공했습니다."),
    GET_RECEIVE_PILL_INFO_SUCCESS(HttpStatus.OK, "전달받은 약 정보 조회에 성공했습니다."),
    COMPLETE_PILL_NOTICE(HttpStatus.OK, "약 알림 처리를 완료했습니다."),
    ;

    private final HttpStatus code;
    private final String message;
}
