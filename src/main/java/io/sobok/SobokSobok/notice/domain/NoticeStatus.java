package io.sobok.SobokSobok.notice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeStatus {

    ACCEPT,
    WAITING,
    REFUSE,
    ;
}
