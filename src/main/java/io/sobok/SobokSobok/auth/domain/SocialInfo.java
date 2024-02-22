package io.sobok.SobokSobok.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialInfo {

    @Column(nullable = false)
    private String socialId;

    public void removeSocialInfo() {
        this.socialId = "";
    }

    @Builder
    public SocialInfo(String socialId) {
        this.socialId = socialId;
    }
}
