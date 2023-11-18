package io.sobok.SobokSobok.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialInfo {

    @Column(nullable = false)
    private String socialId;

    @Column
    private String socialProfileImage;
}
