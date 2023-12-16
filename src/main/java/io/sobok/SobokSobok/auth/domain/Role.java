package io.sobok.SobokSobok.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("사용자")
    ;

    private final String title;
}
