package io.sobok.SobokSobok.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // common
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "잘못된 Request body입니다."),
    BAD_REQUEST_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 형식의 요청입니다."),
    FILE_SAVE_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "파일 생성에 실패했습니다."),

    // auth
    UNREGISTERED_USER(HttpStatus.NOT_FOUND, "등록되지 않은 사용자입니다."),
    UNREGISTERED_TOKEN(HttpStatus.NOT_FOUND, "등록되지 않은 토큰입니다."),
    NOT_LOGGED_IN_USER(HttpStatus.NOT_FOUND, "로그인되지 않은 사용자입니다."),
    ALREADY_EXISTS_USER(HttpStatus.CONFLICT, "이미 회원가입이 완료된 사용자입니다."),
    ALREADY_USING_USERNAME(HttpStatus.CONFLICT, "이미 사용중인 username입니다."),

    // jwt
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 형식의 토큰입니다"),
    NULL_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),

    // pill
    EXCEEDED_PILL_COUNT(HttpStatus.BAD_REQUEST, "약 개수가 초과됐습니다."),
    INVALID_PILL_REQUEST_DATA(HttpStatus.BAD_REQUEST, "허용되지 않은 약 추가 요청 데이터입니다."),
    UNREGISTERED_PILL(HttpStatus.NOT_FOUND, "등록되지 않은 약입니다."),
    UNAUTHORIZED_PILL(HttpStatus.FORBIDDEN, "접근 권한이 없는 약입니다."),
    NOT_SEND_PILL(HttpStatus.NOT_FOUND, "전송된 적이 없는 약입니다."),


    // friend
    INVALID_SELF_ADD_FRIEND(HttpStatus.BAD_REQUEST, "자신에게 캘린더 공유 요청을 할 수 없습니다."),
    ALREADY_FRIEND(HttpStatus.CONFLICT, "이미 캘린더 공유 요청이 되었습니다."),
    NOT_FRIEND(HttpStatus.FORBIDDEN, "친구 관계가 아닙니다."),

    // notice
    NON_EXISTS_NOTICE(HttpStatus.NOT_FOUND, "존재하지 않는 알림입니다."),
    NOT_PILL_NOTICE(HttpStatus.BAD_REQUEST, "약 정보 알림이 아닙니다."),
    ALREADY_COMPLETE_NOTICE(HttpStatus.BAD_REQUEST, "이미 처리된 알림입니다."),

    // external
    INVALID_EXTERNAL_REQUEST_DATA(HttpStatus.BAD_REQUEST, "외부 API 요청에 잘못된 데이터가 전달됐습니다."),
    ;

    private final HttpStatus code;
    private final String message;
}
