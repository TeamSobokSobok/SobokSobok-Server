package io.sobok.SobokSobok.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
    private Platform platform;

    public static SocialInfo newInstance(String socialId, Platform platform) {
        return new SocialInfo(socialId, platform);
    }

    private SocialInfo(String socialId, Platform platform) {
        this.socialId = socialId;
        this.platform = platform;
    }

    public void changeSocialPlatform(Platform platform) {
        this.platform = platform;
    }

    public void removeSocialInfo() {
        this.socialId = "";
    }
}
