package io.sobok.SobokSobok.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // common
    NOT_EXIST_PARAMETER(HttpStatus.BAD_REQUEST, "필수 파라미터 값이 없습니다."),
    NOT_SUPPORTED_METHOD(HttpStatus.BAD_REQUEST, "사용할 수 없는 HTTP method입니다."),
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "잘못된 Request body입니다."),
    BAD_REQUEST_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 형식의 요청입니다."),
    FILE_SAVE_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "파일 생성에 실패했습니다."),
    FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    INVALID_PLATFORM(HttpStatus.BAD_REQUEST, "허용되지 않은 플랫폼입니다."),

    // auth
    UNREGISTERED_USER(HttpStatus.NOT_FOUND, "등록되지 않은 사용자입니다."),
    UNREGISTERED_TOKEN(HttpStatus.NOT_FOUND, "등록되지 않은 토큰입니다."),
    NOT_LOGGED_IN_USER(HttpStatus.NOT_FOUND, "로그인되지 않은 사용자입니다."),
    ALREADY_EXISTS_USER(HttpStatus.CONFLICT, "이미 회원가입이 완료된 사용자입니다."),
    ALREADY_USING_USERNAME(HttpStatus.CONFLICT, "이미 사용중인 username입니다."),
    EMPTY_DEVICE_TOKEN(HttpStatus.NOT_FOUND, "디바이스 토큰이 존재하지 않습니다."),

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
    UNREGISTERED_PILL_SCHEDULE(HttpStatus.NOT_FOUND, "등록되지 않은 약 일정입니다."),
    UNCONSUMED_PILL(HttpStatus.BAD_REQUEST, "복용하지 않은 약입니다."),


    // friend
    INVALID_SELF_ADD_FRIEND(HttpStatus.BAD_REQUEST, "자신에게 캘린더 공유 요청을 할 수 없습니다."),
    ALREADY_FRIEND(HttpStatus.CONFLICT, "이미 캘린더 공유 요청이 되었습니다."),
    EXCEEDED_FRIEND_COUNT(HttpStatus.CONFLICT, "친구 수가 초과됐습니다."),
    NOT_FRIEND(HttpStatus.FORBIDDEN, "친구 관계가 아닙니다."),

    // notice
    NON_EXISTS_NOTICE(HttpStatus.NOT_FOUND, "존재하지 않는 알림입니다."),
    NOT_PILL_NOTICE(HttpStatus.BAD_REQUEST, "약 정보 알림이 아닙니다."),
    ALREADY_COMPLETE_NOTICE(HttpStatus.BAD_REQUEST, "이미 처리된 알림입니다."),

    // external
    INVALID_EXTERNAL_REQUEST_DATA(HttpStatus.BAD_REQUEST, "외부 API 요청에 잘못된 데이터가 전달됐습니다."),

    //sticker
    UNREGISTERED_STICKER(HttpStatus.NOT_FOUND, "등록되지 않은 스티커입니다."),
    ALREADY_SEND_STICKER(HttpStatus.CONFLICT, "이미 스티커를 전송했습니다."),
    UNREGISTERED_LIKE_SCHEDULE(HttpStatus.NOT_FOUND, "스티커 전송기록이 존재하지 않습니다."),
    NOT_FOUND_SAVE_IMAGE_EXCEPTION(HttpStatus.NOT_FOUND, "이미지 저장에 실패했습니다."),
    NOT_FOUND_IMAGE_EXCEPTION(HttpStatus.NOT_FOUND, "이미지 이름을 찾을 수 없습니다."),
    INVALID_MULTIPART_EXTENSION_EXCEPTION(HttpStatus.BAD_REQUEST, "허용되지 않은 타입의 파일입니다.")
    ;

    private final HttpStatus code;
    private final String message;
}
