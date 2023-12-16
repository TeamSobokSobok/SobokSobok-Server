package io.sobok.SobokSobok.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialType {

    KAKAO("카카오"),
    APPLE("애플"),
    ;

    private final String value;
}
